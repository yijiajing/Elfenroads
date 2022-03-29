package gamemanager;

import commands.*;
import domain.*;
import enums.ELRoundPhaseType;
import enums.GameVariant;
import enums.RoundPhaseType;
import loginwindow.*;
import networking.*;
import gamescreen.GameScreen;
import utils.GameRuleUtils;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * A Singleton class that controls the main game loop
 */
public class ELGameManager extends GameManager {

    private final static Logger LOGGER = Logger.getLogger("Game Manager");

    /**
     * Constructor is called when "join" is clicked
     * If the User is starting a new game, then loadedState == null
     * If the User is loading a previous game, then loadedState != null
     */
    ELGameManager(Optional<GameState> loadedState, String pSessionID, GameVariant variant) {
        super(loadedState, pSessionID, variant);
    }

    @Override
    protected void setUpNewGame() {
        // put 5 counters face up, these are shared across peers
        for (int i = 0; i < 5; i++) {
            this.gameState.addFaceUpCounterFromPile();
        }

        // give all players (each peer) an obstacle
        thisPlayer.getHand().addUnit(new Obstacle(MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900));

        // assign each player a destination town if applicable
        if (gameState.getGameVariant() == GameVariant.ELFENLAND_DESTINATION) {
            TownCardDeck townCardDeck = new TownCardDeck(sessionID);
            for (int i = 0; i < gameState.getNumOfPlayers(); i++) {
                gameState.getPlayers().get(i).setDestinationTown(townCardDeck.getComponents().get(i).getTown());
            }
        }

        for (Player p : gameState.getPlayers()) {
            System.out.println(p.getDestinationTown());
        }
    }

    /**
     * PHASE 1
     */
    @Override
    public void setUpRound() {
        gameState.setCurrentPhase(ELRoundPhaseType.DEAL_CARDS);
        gameState.setToFirstPlayer();
        gameState.getTravelCardDeck().shuffle(); // only shuffle once at the beginning of each round

        // Triggered only on one instance (the first player)
        if (isLocalPlayerTurn()) {
            distributeTravelCards(); // distribute cards to each player (PHASE 1)
            GameScreen.getInstance().updateAll();
        }
    }

    /**
     * PHASE 1
     * Distribute travel cards to each Player by popping from the TravelCardDeck
     * Fills the Player's hand to have 8 travel cards
     */
    public void distributeTravelCards() {

        LOGGER.info("Distributing travel cards...");
        LOGGER.info("Local player turn: " + isLocalPlayerTurn());

        if (!(isLocalPlayerTurn() && gameState.getCurrentPhase() == ELRoundPhaseType.DEAL_CARDS)) return;

        int numCards = thisPlayer.getHand().getNumTravelCards();
        for (int i = numCards; i < 8; i++) {
            thisPlayer.getHand().addUnit(gameState.getTravelCardDeck().draw());
        }
        LOGGER.info("Added " + (8 - numCards) + " travel cards...");
        LOGGER.info(getThisPlayer().getHand().getCards().toString());

        int numDrawn = 8 - numCards;
        DrawCardCommand drawCardCommand = new DrawCardCommand(numDrawn, null);
        try {
            coms.sendGameCommandToAllPlayers(drawCardCommand);
        } catch (IOException e) {
            LOGGER.info("There was a problem sending the command to draw cards!");
            e.printStackTrace();
        }
        endTurn();
    }


    /**
     * PHASE 2
     * Distribute 1 face-down transportation counter to each Player by popping from the CounterPile
     */
    public void distributeHiddenCounter() {
        if (!(isLocalPlayerTurn() && gameState.getCurrentPhase() == ELRoundPhaseType.DEAL_HIDDEN_COUNTER)) return;

        // add the counter to our hand
        CounterUnit counter = gameState.getCounterPile().draw();
        counter.setOwned(true);
        counter.setSecret(true);
        thisPlayer.getHand().addUnit(counter);

        // tell the other peers to remove the counter from the pile
        try {
            coms.sendGameCommandToAllPlayers(new DrawCounterCommand(counter, false));
        } catch (IOException e) {
            System.out.println("Error: there was a problem sending the DrawCounterCommand to the other peers.");
        }

        endTurn();
    }

    /**
     * PHASE 3
     */
    public void drawCounters() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && GameRuleUtils.isDrawCountersPhase()
                && isLocalPlayerTurn()) {

            updateGameState();
            System.out.println("Current phase: DRAW COUNTERS");

            if (gameState.getGameVariant() == GameVariant.ELFENLAND_LONG) {
                GameScreen.displayMessage("This is the long variant of Elfenland. You will go through 4 rounds instead of 3.");
            }

            if (gameState.getGameVariant() == GameVariant.ELFENLAND_DESTINATION && gameState.getCurrentPhase() == ELRoundPhaseType.DRAW_COUNTER_ONE) {
                GameScreen.displayMessage("Your destination Town is " +
                        thisPlayer.getDestinationTown().getName() + ". Please collect town pieces and have your travel " +
                        "route end in a town as close as possible to the destination at the end of the game.");
            }

            // display message to let the user know that they need to select a counter
            GameScreen.displayMessage("Please select a transportation counter to add to your hand. You may choose one of " +
                    "the face-up counters or a counter from the deck, shown on the right side of the screen.");

            // all logic is implemented in the mouse listeners of the counters
        }
    }

    /**
     * PHASE 4
     */

    /**
     * PHASE 5
     * Travel on the map (inside GameManager superclass)
     */


    /**
     * PHASE 6
     * Return all counters except one
     */
    @Override
    public void returnCountersPhase() {
        if (!(isLocalPlayerTurn() && gameState.getCurrentPhase() == ELRoundPhaseType.RETURN_COUNTERS)) return;

        // no need to return the counters if we are at the end of the game
        if (gameState.getCurrentRound() == gameState.getTotalRounds()
                || thisPlayer.getHand().getCounters().size() <= 1) {
            LOGGER.info("Did not return counters because there is no counter or the end of the game");
            endTurn();
            return;
        }

        actionManager.clearSelection();

        GameScreen.displayMessage("""
                The round is over! All of your transportation counters must be returned except for one. 
                Please select the transportation counter from your hand that you wish to keep.
                """);

        // once the player clicks a transportation counter it will call returnAllCountersExceptOne()
    }

    /**
     * Part of phase 6, called when a transportation counter from the player's hand is clicked
     * Return all counters except one
     *
     * @param toKeep
     */
    @Override
    public void returnCounter(CounterUnit toKeep) {
        assert toKeep instanceof TransportationCounter; // can only be transportation counter for Elfenland
        List<TransportationCounter> myCounters = thisPlayer.getHand().getCounters();

        for (TransportationCounter c : myCounters) {
            if (c.equals(toKeep)) {
                continue;
            }

            GameState.instance().getCounterPile().addDrawable(c); // put counter back in the deck

            try {
                LOGGER.info("Sending ReturnTransportationCounterCommand to all players");
                coms.sendGameCommandToAllPlayers(new ReturnTransportationCounterCommand(c));
            } catch (IOException e) {
                System.out.println("There was a problem sending the ReturnTransportationCounterCommand to all players.");
                e.printStackTrace();
            }
        }

        // clear all counters and then add toKeep back, otherwise we get concurrent modification exception
        thisPlayer.getHand().getCounters().clear();
        thisPlayer.getHand().addUnit(toKeep);

        endTurn();
    }

    @Override
    public void endGame() {
        LOGGER.info("Game ends in " + gameState.getCurrentRound() + " rounds");
        gameState.setCurrentPhase(null);
        List<Player> players = gameState.getPlayers();
        List<Player> winners = new ArrayList<>();
        winners.add(players.get(0));

        // calculate final score of each player according to the destination town variant rule
        if (gameState.getGameVariant() == GameVariant.ELFENLAND_DESTINATION) {
            for (Player p : players) {
                int townsAway = GameMap.getInstance().getDistanceBetween(p.getCurrentTown(), p.getDestinationTown()) - 1;
                int newScore = p.getScore() - townsAway;
                p.setScore(newScore);
            }
        }
        //update scoreboard
        GameScreen.getInstance().updateAll();
        for (Player p : players) {
            if (p.getScore() > winners.get(0).getScore()) {
                winners.clear();
                winners.add(p);
            } else if (p.getScore() == winners.get(0).getScore()) {
                if (p.getHand().getNumTravelCards() < winners.get(0).getHand().getNumTravelCards()) {
                    winners.clear();
                    winners.add(p);
                } else if (p.getHand().getNumTravelCards() == winners.get(0).getHand().getNumTravelCards() && p != winners.get(0)) {
                    winners.add(p);
                }
            }
        }

        String destinations = "\n";
        if (gameState.getGameVariant() == GameVariant.ELFENLAND_DESTINATION) {
            for (int i = 0; i < gameState.getNumOfPlayers(); i++) {
                String dest = gameState.getPlayers().get(i).getDestinationTown().getName();
                String name = gameState.getPlayers().get(i).getName();
                destinations += name + "'s destination is " + dest + ".\n";
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

}