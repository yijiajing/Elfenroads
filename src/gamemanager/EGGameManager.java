package gamemanager;

import commands.DrawCounterCommand;
import commands.NotifyTurnCommand;
import commands.ReturnCounterUnitCommand;
import domain.*;
import enums.EGRoundPhaseType;
import enums.ELRoundPhaseType;
import enums.GameVariant;
import gamescreen.EGGameScreen;
import gamescreen.GameScreen;
import networking.GameState;
import networking.ActionManager;
import savegames.Savegame;
import utils.GameRuleUtils;
import windows.AuctionFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


public class EGGameManager extends GameManager {

    private final static Logger LOGGER = Logger.getLogger("Game Manager");
    private CounterUnit prevCounterKept;
    private AuctionFrame auctionFrame;


    EGGameManager(Optional<Savegame> savegame, String sessionID, GameVariant variant, String pLocalAddress) {
        super(savegame, sessionID, variant, pLocalAddress);

        assert GameRuleUtils.isElfengoldVariant(variant);
    }

    @Override
    public void setUpNewGame() {
        // randomly assign gold value tokens to towns
        if (gameState.getGameVariant() == GameVariant.ELFENGOLD_RANDOM_GOLD) {
            GoldTokenDeck goldTokenDeck = new GoldTokenDeck(sessionID);
            for (Town town : GameMap.getInstance().getTownList()) {
                if (town.getName().equals("Elvenhold")) continue;
                town.setGoldValueToken(goldTokenDeck.draw());
            }
        }
        // initial preparation: deal five cards to each player; give each player 7 gold coins
        for (Player p: gameState.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                p.getHand().addUnit(gameState.getTravelCardDeck().draw());
            }
            p.addGoldCoins(7);
        }

        // add gold cards later so that the initially assigned cards are all travel cards
        gameState.getTravelCardDeck().initGoldCards();

        // put 3 cards face up, these are shared across peers
        for (int j = 0; j < 3; j++) {
            this.gameState.addFaceUpCardFromDeck();
        }

        LOGGER.fine(gameState.getTravelCardDeck().toString());

        // assign each player a destination town if applicable
        if (gameState.getGameVariant() == GameVariant.ELFENGOLD_DESTINATION) {
            TownCardDeck townCardDeck = new TownCardDeck(sessionID);
            for (int i = 0; i < gameState.getNumOfPlayers(); i++) {
                gameState.getPlayers().get(i).setDestinationTown(townCardDeck.getComponents().get(i).getTown());
            }
        }

        for (Player p : gameState.getPlayers()) {
            System.out.println(p.getDestinationTown());
        }
    }

    @Override
    public void setUpRound() {
        gameState.setToFirstPlayer();
        gameState.getTravelCardDeck().shuffle(); // only shuffle once at the beginning of each round
        if (gameState.getCurrentRound() == 1) {
            // Draw Card phase is ignored in the first round
            LOGGER.info("In the first round, go directly to choose face-up counter");
            gameState.setCurrentPhase(EGRoundPhaseType.CHOOSE_FACE_UP);

            // inform player of their destination town
            if (gameState.getGameVariant() == GameVariant.ELFENGOLD_DESTINATION) {
                GameScreen.displayMessage("Your destination Town is " +
                        thisPlayer.getDestinationTown().getName() + ". Please collect town pieces and have your travel " +
                        "route end in a town as close as possible to the destination at the end of the game.");
            }

            // Triggered only on one instance (the first player)
            if (isLocalPlayerTurn()) {
                chooseFaceUpCounter();
                GameScreen.getInstance().updateAll();
            }
        } else {
            LOGGER.info("In round " + gameState.getCurrentRound() + ", go to draw card phase");
            GameScreen.displayMessage("""
                    New Round Start! You can get 2 coins.
                    """);
            //distribute 2 gold coins at the beginning of all rounds except for the first
            gameState.getCurrentPlayer().addGoldCoins(2);
            GameRuleUtils.updateRemoteGoldCoins(2);
            GameScreen.getInstance().updateAll();
            
            gameState.setCurrentPhase(EGRoundPhaseType.DRAW_CARD_ONE);
            // Triggered only on one instance (the first player)
            if (isLocalPlayerTurn()) {
                drawTravelCard();
            }
        }

    }

    /**
     * PHASE 3
     */
    public void drawTravelCard() {
        if (!(gameState.getCurrentRound() <= gameState.getTotalRounds()
                && GameRuleUtils.isDrawCardsPhase()
                && isLocalPlayerTurn())) {
            return;
        }
        updateGameScreen();

        GameScreen.displayMessage("""
                Please select a card to add to your hand. You may choose one of the face-up cards, 
                a card from the deck or take the entire gold card deck, shown on the right side of the screen.
                """);

        // all logic is implemented in the mouse listeners of the cards
    }

    /**
     * PHASE 4
     */
    public void chooseFaceUpCounter() {
        Logger.getGlobal().info(Integer.toString(gameState.getCurrentRound()));
        Logger.getGlobal().info(gameState.getCurrentPhase().toString());
        Logger.getGlobal().info(Boolean.toString(gameState.getCurrentPhase() == EGRoundPhaseType.CHOOSE_FACE_UP));

        if (!(gameState.getCurrentRound() <= gameState.getTotalRounds()
                && gameState.getCurrentPhase() == EGRoundPhaseType.CHOOSE_FACE_UP
                && isLocalPlayerTurn())) {
            return;
        }

        Logger.getGlobal().info("Preparing to show the counter popup window.");

        // distribute gold coins (beginning with the second round)
        if (gameState.getCurrentRound() > 1) {
            for (Player p: gameState.getPlayers()) {
                p.addGoldCoins(2);
            }
        }
        // distribute two items from the face-down counter pile
        CounterUnit counter1 = gameState.getCounterPile().draw();
        CounterUnit counter2 = gameState.getCounterPile().draw();
        thisPlayer.getHand().getCounters().add(counter1);
        thisPlayer.getHand().getCounters().add(counter2);

        // by default, both are hidden
        // once a player clicks one of them, it will change secret to false
        counter1.setSecret(true);
        counter2.setSecret(true);

        // let the player choose which counter to place face-up (hence the other one is face-down)
        ((EGGameScreen) GameScreen.getInstance()).showCounterPopup(counter1, counter2);
    }

    /**
     * Called after the user chooses their face-up counter from the ChooseCounterPopup window
     */
    public void sendCounters(CounterUnit counter1, CounterUnit counter2) {
        Logger.getGlobal().info("Sending two DrawCounterCommands");
        DrawCounterCommand cmd1 = new DrawCounterCommand(counter1, false);
        DrawCounterCommand cmd2 = new DrawCounterCommand(counter2, false);
        try {
            coms.sendGameCommandToAllPlayers(cmd1);
            coms.sendGameCommandToAllPlayers(cmd2);
        } catch (IOException e) {
            LOGGER.severe("There was a problem sending the command to draw counters!");
            e.printStackTrace();
        }

        endTurn();
    }

    /**
     * PHASE 5
     */
    public void auction() {
        if (!(gameState.getCurrentRound() <= gameState.getTotalRounds()
                && gameState.getCurrentPhase() == EGRoundPhaseType.AUCTION)) {
            // does not need to be local player's turn as the auction frame is displayed for everyone at the start of auction phase
            return;
        }

        Logger.getGlobal().info("Showing the auction frame.");
        this.auctionFrame = new AuctionFrame();;

        CounterUnitPile pile = GameState.instance().getCounterPile();
        int numPlayers = GameState.instance().getNumOfPlayers();
        Logger.getGlobal().info("Num of players is " + numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            auctionFrame.addCounter(pile.draw());
            auctionFrame.addCounter(pile.draw());
        }
        auctionFrame.setVisible(true);

        auctionFrame.setVisible(true);
    }

    /**
     * PHASE 6
     * Plan travel routes (inside GameManager superclass)
     */

    /**
     * PHASE 7
     * Move on the map (inside GameManager superclass)
     */

    /**
     * PHASE 8
     */
    @Override
    public void returnCountersPhase() {
        if (!(gameState.getCurrentRound() <= gameState.getTotalRounds()
                && gameState.getCurrentPhase() == EGRoundPhaseType.RETURN_COUNTERS
                && isLocalPlayerTurn())) {
            return;
        }

        // no need to return the counters if we are at the end of the game
        if (gameState.getCurrentRound() == gameState.getTotalRounds()
                || thisPlayer.getHand().getCounters().size() <= 2) {
            LOGGER.info("Did not return counters because there is no counter or the end of the game, or the player have 2 or less counters");
            endTurn();
            return;
        }

        actionManager.clearSelection();

        GameScreen.displayMessage("""
                The round is over! All of your counters must be returned except for two. Please select two items 
                (any combination of transportation counters, gold pieces, obstacles or magic spells) from your hand 
                that you wish to keep.
                """);

        // once the player clicks a transportation counter it will call returnCounter()
    }

    @Override
    public void returnCounter(CounterUnit toKeep) {
        List<CounterUnit> myCounters = thisPlayer.getHand().getCounters();
        assert myCounters.contains(toKeep);

        if (prevCounterKept == null) {
            prevCounterKept = toKeep;
            prevCounterKept.setSelected(true);
        } else {
            if (toKeep == prevCounterKept) {
                toKeep.setSelected(false);
                prevCounterKept = null;
                return;
            }
            // the player has selected all two counters to keep, return all counters except these two
            LOGGER.info("The player chose to keep " + prevCounterKept.getType() + " and " + toKeep.getType());
            for (CounterUnit c : myCounters) {
                if (c == toKeep || c == prevCounterKept) {
                    continue;
                }

                GameState.instance().getCounterPile().addDrawable(c); // put counter back in the deck

                try {
                    LOGGER.info("Sending ReturnCounterUnitCommand to all players, returning a " + c.getType());
                    coms.sendGameCommandToAllPlayers(new ReturnCounterUnitCommand(c));
                } catch (IOException e) {
                    System.out.println("There was a problem sending the ReturnCounterUnitCommand to all players.");
                    e.printStackTrace();
                }
            }
            // clear all counters and then add toKeep and prevCounterKept back, otherwise we get concurrent modification exception
            thisPlayer.getHand().getCounters().clear();
            toKeep.setSelected(false);
            prevCounterKept.setSelected(false);
            thisPlayer.getHand().addUnit(toKeep);
            thisPlayer.getHand().addUnit(prevCounterKept);
            prevCounterKept = null;

            endTurn();
        }
    }

    @Override
    public void endPhase() {
        actionManager.clearSelection();
        int nextOrdinal = ((EGRoundPhaseType) gameState.getCurrentPhase()).ordinal() + 1;

        // all phases are done, go to the next round
        if (nextOrdinal == EGRoundPhaseType.values().length) {
            endRound();
            gameState.clearPassedPlayerCount();
        }

        // continue with plan routes phase if not all players have passed their turn
        else if (gameState.getCurrentPhase() == EGRoundPhaseType.PLAN_ROUTES
                && gameState.getPassedPlayerCount() < gameState.getNumOfPlayers()) {
            LOGGER.info("Pass turn ct: " + gameState.getPassedPlayerCount() + ", staying at the PLAN ROUTES phase");
            gameState.setToFirstPlayer();
            // the first player will take action
            if (isLocalPlayerTurn()) {
                NotifyTurnCommand notifyTurnCommand = new NotifyTurnCommand(EGRoundPhaseType.PLAN_ROUTES);
                notifyTurnCommand.execute(); // notify themself to take action
            }
            gameState.clearPassedPlayerCount();
        }

        // just enter the auction phase, display auction frame for everyone
        else if (gameState.getCurrentPhase() == EGRoundPhaseType.CHOOSE_FACE_UP
                && EGRoundPhaseType.values()[nextOrdinal] == EGRoundPhaseType.AUCTION) {
            gameState.setCurrentPhase(EGRoundPhaseType.AUCTION);
            LOGGER.info("...Going to the auction phase");
            gameState.setToFirstPlayer();
            auction(); // display auction window for everyone
            if (isLocalPlayerTurn()) {
                NotifyTurnCommand notifyTurnCommand = new NotifyTurnCommand(EGRoundPhaseType.AUCTION);
                notifyTurnCommand.execute(); // notify themself to take action
            } else {
                auctionFrame.displayMessage("It is not your turn. Waiting for other players to bid...");
            }
            gameState.clearPassedPlayerCount();
        }

        // continue with auction phase if not all counters have been auctioned
        else if (gameState.getCurrentPhase() == EGRoundPhaseType.AUCTION && auctionFrame.getNumCountersInAuction() > 0) {
            LOGGER.info("Number of counters not auctioned: " + auctionFrame.getNumCountersInAuction() + ", staying at the AUCTION phase");
            gameState.setToFirstPlayer();
            // the first player will take action
            if (isLocalPlayerTurn()) {
                NotifyTurnCommand notifyTurnCommand = new NotifyTurnCommand(EGRoundPhaseType.AUCTION);
                notifyTurnCommand.execute(); // notify themself to take action
            } else {
                auctionFrame.displayMessage("It is not your turn. Waiting for other players to bid...");
            }
        }

        // go to the next phase within the same round
        else {
            gameState.setCurrentPhase(EGRoundPhaseType.values()[nextOrdinal]);
            LOGGER.info("...Going to the next phase : " + gameState.getCurrentPhase());
            //if just enter Plan_Routes, close the auction window automatically
            if (gameState.getCurrentPhase() == EGRoundPhaseType.PLAN_ROUTES) {
            	auctionFrame.closeAuctionFrame();
            }
            //if just enter the Move Phase, reset the hasMoved parameter to false.
            if(gameState.getCurrentPhase() == EGRoundPhaseType.MOVE) {
                ActionManager.getInstance().setBootMoved(false);
            }
            
            gameState.setToFirstPlayer();
            // the first player will take action
            if (isLocalPlayerTurn()) {
                NotifyTurnCommand notifyTurnCommand = new NotifyTurnCommand(gameState.getCurrentPhase());
                notifyTurnCommand.execute(); // notify themself to take action
            }
            gameState.clearPassedPlayerCount();
        }
    }

    @Override
    public void endGame() {
        LOGGER.info("Game ends in " + gameState.getCurrentRound() + " rounds");
        gameState.setCurrentPhase(null);
        List<Player> players = gameState.getPlayers();
        List<Player> winners = new ArrayList<>();
        winners.add(players.get(0));

        // update scoreboard
        GameScreen.getInstance().updateAll();

        // adjust final score of each player according to the destination town variant rule
        if (gameState.getGameVariant() == GameVariant.ELFENGOLD_DESTINATION) {
            for (Player p : players) {
                int townsAway = GameMap.getInstance().getDistanceBetween(p.getCurrentTown(), p.getDestinationTown()) - 1;
                int newScore = p.getScore() - townsAway;
                p.setScore(newScore);
            }
        }

        // calculate the winner - the player with the most points or most coins (in case of tie)
        for (Player p : players) {
            if (p.getScore() > winners.get(0).getScore()) {
                winners.clear();
                winners.add(p);
            } else if (p.getScore() == winners.get(0).getScore()) {
                if (p.getGoldCoins() > winners.get(0).getGoldCoins()) {
                    winners.clear();
                    winners.add(p);
                } else if (p.getGoldCoins() == winners.get(0).getGoldCoins() && p != winners.get(0)) {
                    winners.add(p);
                }
            }
        }

        String destinations = "\n";
        if (gameState.getGameVariant() == GameVariant.ELFENGOLD_DESTINATION) {
            for (int i = 0; i < gameState.getNumOfPlayers(); i++) {
                Player player = gameState.getPlayers().get(i);
                String dest = player.getDestinationTown().getName();
                String name = player.getName();
                int townsAway = GameMap.getInstance().getDistanceBetween(player.getCurrentTown(), player.getDestinationTown()) - 1;
                destinations += name + "'s destination is " + dest + "[Distance :" + townsAway + "].\n";
            }
        }

        assert winners.size() >= 1;
        if (winners.size() == 1) {
            GameScreen.displayMessage(winners.get(0).getName() + " is the winner!" + destinations);
        } else {
            String winnersNames = "";
            for (Player winner : winners) {
                winnersNames = winnersNames.concat(" " + winner.getName());
            }
            GameScreen.displayMessage("There is a tie. " + winnersNames + " are the winners!" + destinations);
        }
    }

    public AuctionFrame getAuctionFrame() {
        return this.auctionFrame;
    }
}
