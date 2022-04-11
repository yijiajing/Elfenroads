package gamemanager;

import commands.*;
import domain.*;
import enums.EGRoundPhaseType;
import enums.ELRoundPhaseType;
import enums.ObstacleType;
import enums.TravelCardType;
import enums.*;
import gamescreen.EGGameScreen;
import networking.GameState;
import panel.ElfBootPanel;
import gamescreen.GameScreen;
import utils.GameRuleUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Selection handling:
 * counter unit + road + PLANROUTES phase -> place a counter unit on the road
 * - card + town + MOVE phase -> requested player move to a town
 */
public class ActionManager {

    private static ActionManager INSTANCE;

    private final static Logger LOGGER = Logger.getLogger("Action Manager");

    private final GameState gameState;
    private GameManager gameManager;

    private Road selectedRoad;
    private boolean inExchange;
    private boolean inExternalWindow;
    private CounterUnit selectedCounter;
    private final List<TravelCard> selectedCards = new ArrayList<>();
    private Town selectedTown;
    private int cardsToBeDrawn = 0; // used when a player chooses to take 2 cards after moving to a new town in elfengold, or when Player choose not to make a move and draw 2 cards
    private boolean bootMoved = false; //identify whether a player has made a move in current Move phase.

    
    public static ActionManager init(GameState gameState, GameManager gameManager) {
        if (INSTANCE == null) {
            INSTANCE = new ActionManager(gameState, gameManager);
        }
        return INSTANCE;
    }

    private ActionManager(GameState gameState, GameManager gameManager) {

        this.gameState = gameState;
        this.gameManager = gameManager;
    }

    public static ActionManager getInstance() {
        return INSTANCE;
    }

    public Road getSelectedRoad() {
        return selectedRoad;
    }

    /**
     * If the player selects a transportation counter before selecting the road, a place transportation counter
     * command is triggered.
     * If the player selects an obstacle before selecting the road, a place obstacle command is triggered.
     *
     * Preconditions: the current phase is PLAN ROUTES and the requested player is the current player
     *
     * @param road the road that the player clicks on
     */
    public void setSelectedRoad(Road road) {
        LOGGER.info("Road on " + road.getRegionType() + " selected");

        if (inExternalWindow) {
            LOGGER.info("In external window, cannot click on a road");
            return;
        }

        if (!((gameState.getCurrentPhase() == EGRoundPhaseType.PLAN_ROUTES || gameState.getCurrentPhase() == ELRoundPhaseType.PLAN_ROUTES)
                && gameManager.isLocalPlayerTurn())) {
            return;
        }
        LOGGER.info("Before removing the counter, counters in hand: " +
                GameManager.getInstance().getThisPlayer().getHand().getCounters().toString());

        // the player wish to exchange the transportation counters on the previously selected
        // road and the newly selected road
        if (inExchange) {
            assert selectedCounter != null;
            assert selectedCounter.getType() == MagicSpellType.EXCHANGE;
            LOGGER.info("In exchange");
            if (selectedRoad.exchangeWith(road)) {
                gameManager.getThisPlayer().getHand().removeUnit(selectedCounter);
                selectedCounter.setOwned(false);
                gameState.getCounterPile().addDrawable(selectedCounter);
                GameCommand exchangeCommand = new ExchangeCommand(selectedRoad, road, selectedCounter.isSecret());
                try {
                    gameManager.getComs().sendGameCommandToAllPlayers(exchangeCommand);
                    GameScreen.getInstance().updateAll();
                } catch (IOException e) {
                    LOGGER.severe("The was a problem sending the command to place the exchange magic spell!");
                    e.printStackTrace();
                }
            } else {
                clearSelection();
                GameScreen.displayMessage("The exchanged transportation counters are not legal on the roads they are " +
                        "exchanged to. Please try again. ");
            }
            inExchange = false;
            clearSelection();
            gameManager.endTurn();
            return;
        }

        selectedRoad = road;

        // Player intends to place an obstacle
        if (selectedCounter instanceof Obstacle) {
            if (selectedRoad.placeObstacle((Obstacle) selectedCounter)) {
                gameManager.getThisPlayer().getHand().removeUnit(selectedCounter);
                selectedCounter.setOwned(false);
                LOGGER.info("Just removed obstacle, obstacle presence: " + gameManager.getThisPlayer().getHand().getObstacle());
                PlaceObstacleCommand toSendOverNetwork = new PlaceObstacleCommand(selectedRoad, (ObstacleType) selectedCounter.getType());

                try {
                    gameManager.getComs().sendGameCommandToAllPlayers(toSendOverNetwork);
                    GameScreen.getInstance().updateAll();
                } catch (IOException e) {
                    LOGGER.severe("The was a problem sending the command to place the obstacle!");
                    e.printStackTrace();
                }
                gameManager.endTurn();
            } else { // Invalid move
                GameScreen.displayMessage("You cannot place an obstacle here. Please try again.");
            }
        }

        // Player intends to place a transportation counter
        else if (selectedCounter instanceof TransportationCounter) {
            TransportationCounter counter = (TransportationCounter) selectedCounter;
            if (selectedRoad.setTransportationCounter(counter)) {
                // remove this transportation counter from hand
                gameManager.getThisPlayer().getHand().removeUnit(counter);
                counter.setOwned(false);

                LOGGER.info("Just removed " + counter.getType() +
                        ", current counters in hand: " +
                        GameManager.getInstance().getThisPlayer().getHand().getCounters().toString());

                PlaceTransportationCounterCommand toSendOverNetwork = new PlaceTransportationCounterCommand(selectedRoad, counter);
                try {
                    gameManager.getComs().sendGameCommandToAllPlayers(toSendOverNetwork);
                    GameScreen.getInstance().updateAll();
                } catch (IOException e) {
                    LOGGER.severe("The was a problem sending the command to place the transportation counter!");
                    e.printStackTrace();
                }
                gameManager.endTurn();
            } else { // Invalid move
                GameScreen.displayMessage("You cannot place a transportation counter here. Please try again.");
            }
        }

        // Player intends to place a gold piece
        else if (selectedCounter instanceof GoldPiece) {
            GoldPiece counter = (GoldPiece) selectedCounter;
            if (selectedRoad.placeGoldPiece(counter)) {
                gameManager.getThisPlayer().getHand().removeUnit(selectedCounter);
                selectedCounter.setOwned(false);
                LOGGER.info("Just removed gold piece");
                GameCommand toSendOverNetwork = new PlaceCounterUnitCommand(selectedRoad, counter);

                try {
                    gameManager.getComs().sendGameCommandToAllPlayers(toSendOverNetwork);
                    GameScreen.getInstance().updateAll();
                } catch (IOException e) {
                    LOGGER.severe("The was a problem sending the command to place the gold piece!");
                    e.printStackTrace();
                }
                gameManager.endTurn();
            } else { // Invalid move
                GameScreen.displayMessage("You cannot place a gold piece here. Please try again.");
            }
        }

        // Player intends to place a magic spell
        else if (selectedCounter instanceof MagicSpell) {
            MagicSpell counter = (MagicSpell) selectedCounter;
            if (counter.getType() == MagicSpellType.EXCHANGE) {
                if (road.getRegionType() == RegionType.RIVER || road.getRegionType() == RegionType.LAKE
                        || road.hasDouble() || road.numOfTransportationCounter() == 0) {
                    GameScreen.displayMessage("You cannot place an Exchange here. Please try again.");
                } else {
                    LOGGER.info("Started an exchange, just selected a road on " + selectedRoad.getRegionType());
                    inExchange = true;
                    GameScreen.displayMessage("""
                            You just started an Exchange! Please click on another road with which you would like to 
                            exchange the transportation counter on the road you just selected. You must make certain 
                            that the exchanged transportation counters are legal on the roads they are exchanged to.
                            """);
                }
            } else if (counter.getType() == MagicSpellType.DOUBLE) {
                if (selectedRoad.placeDouble(counter)) {
                    gameManager.getThisPlayer().getHand().removeUnit(selectedCounter);
                    selectedCounter.setOwned(false);
                    GameCommand toSendOverNetwork = new PlaceCounterUnitCommand(selectedRoad, counter);
                    try {
                        gameManager.getComs().sendGameCommandToAllPlayers(toSendOverNetwork);
                        GameScreen.getInstance().updateAll();
                    } catch (IOException e) {
                        LOGGER.severe("The was a problem sending the command to place the double magic spell!");
                        e.printStackTrace();
                    }
                    inExternalWindow = true;
                    ((EGGameScreen) GameScreen.getInstance()).showDoubleMagicSpellPopup();
                } else {
                    GameScreen.displayMessage("You cannot place a Double here. Please try again.");
                }
            }
        }

        if (!(selectedCounter instanceof MagicSpell)) {
            clearSelection();
        }
    }

    public CounterUnit getSelectedCounter() {
        return selectedCounter;
    }

    public void setSelectedCounter(CounterUnit pCounter) {
        if (!(gameState.getCurrentPhase() == ELRoundPhaseType.PLAN_ROUTES
                || gameState.getCurrentPhase() == EGRoundPhaseType.PLAN_ROUTES)) {
            return;
        }

        LOGGER.info("Counter of type " + pCounter.getType() + " selected");

        if (inExchange) {
            GameScreen.displayMessage("You just placed an Exchange Magic Spell. Please select another road instead.");
            return;
        }

        if (inExternalWindow) {
            LOGGER.info("In external window, cannot click on a counter");
            return;
        }

        if (pCounter.equals(selectedCounter)) {
            //if clicked twice, deselect the counter
            pCounter.setSelected(false);
            selectedCounter = null;
        } else {
            if (pCounter instanceof Obstacle) {
                LOGGER.info("Obstacle selected");
            } else if (pCounter instanceof TransportationCounter) {
                LOGGER.info("Counter " + ((TransportationCounter) pCounter).getType() + " selected");
            }
            pCounter.setSelected(true);
            selectedCounter = pCounter;
        }
    }

    public List<TravelCard> getSelectedCards() {
        return selectedCards;
    }

    /**
     * Add the selected card to all selected cards.
     * If the card is already selected, we de-select it. i.e. remove from selected cards.
     *
     * @param card the card that the player clicks on
     */
    public void addSelectedCard(TravelCard card) {
        LOGGER.info("Card " + card.getType() + " selected");

        if (inExternalWindow) {
            LOGGER.info("In external window, cannot click on a card");
            return;
        }

        if (inExchange) {
            LOGGER.info("In exchange, cannot click on a card");
        }

        if (!(gameState.getCurrentPhase() == EGRoundPhaseType.MOVE || gameState.getCurrentPhase() == ELRoundPhaseType.MOVE)) {
            return;
        }
        if (selectedCards.contains(card)) {
            // if clicked twice, then deselect this card
            selectedCards.remove(card);
            assert card.isSelected();
            card.setSelected(false);
        } else {
            // add to selected cards
            selectedCards.add(card);
            assert !card.isSelected();
            card.setSelected(true);
            LOGGER.info("Added a card. Cards: " + selectedCards.toString());
        }
    }

    public void setSelectedCards(List<TravelCard> selectedCards) {
        selectedCards.forEach(this::addSelectedCard);
    }

    public Town getSelectedTown() {
        return selectedTown;
    }

    /**
     * If the player selects some cards before selecting the road, a move boot command is triggered.
     *
     * Preconditions: the current phase is MOVE, some cards are selected, and the requested player
     * is the current player
     *
     * @param town the town that the player clicks on
     */
    public void setSelectedTown(Town town) {
        LOGGER.info("Town " + town.getName() + " selected");
        selectedTown = town;

        if (inExternalWindow) {
            LOGGER.info("In external window, cannot click on a town");
            return;
        }

        if (inExchange) {
            LOGGER.info("In exchange, cannot click on a town");
        }

        if ((!(gameState.getCurrentPhase() == EGRoundPhaseType.MOVE || gameState.getCurrentPhase() == ELRoundPhaseType.MOVE))
                || selectedCards.isEmpty()
                || !gameManager.isLocalPlayerTurn()) {
            return;
        }

        //if the player only choose one travel card and it's an elven witch, the player intends to make a magic flight
        boolean magicFlightSuccess = false;
        if (selectedCards.size() == 1 && selectedCards.get(0).getType() == TravelCardType.WITCH) {
            if (gameState.getCurrentPlayer().getGoldCoins() >= 3) {
                magicFlightSuccess = true;
                gameState.getCurrentPlayer().removeGoldCoins(3);
                GameRuleUtils.updateRemoteGoldCoins(-3);
                LOGGER.info("The current player intends to make a magic flight. Take away 3 coins");
            } else {
                LOGGER.info("The current player intends to make a magic flight but does not have enough coins.");
            }
        }

        Road road = null;
        //only need to validate the move if a magic flight is not applied.
        if (!magicFlightSuccess) {
            road = GameRuleUtils.validateMove(GameMap.getInstance(), gameState.getCurrentPlayer().getCurrentTown(), selectedTown, selectedCards);
        }
        
        //the move is validated if either the road is not null, or magicFlightSuccess
        if (road != null || magicFlightSuccess) {
            // remove cards from the local player's hand
            gameState.getCurrentPlayer().getHand().removeUnits(selectedCards);
            // add cards back to local deck
            gameState.getTravelCardDeck().addDrawables(selectedCards);

            // add cards to other peers' remote decks
            try {
                gameManager.getComs().sendGameCommandToAllPlayers(new ReturnTravelCardsCommand(selectedCards));
            } catch (IOException e) {
                LOGGER.severe("The was a problem sending the ReturnTravelCardsCommand!");
                e.printStackTrace();
            }

            GameScreen.getInstance().updateCards(); // draws updated hand to the screen

            // Move Boot
            ElfBoot boot = gameState.getCurrentPlayer().getBoot();
            ElfBootPanel startForCommand = boot.getCurPanel();
            ElfBoot bootForCommand = boot;
            ElfBootPanel destinationForCommand = selectedTown.getElfBootPanel();

            // boot has been successfully moved and is no longer selected
            ActionManager.getInstance().setBootMoved(true);
            boot.setSelected(false);

            // now, construct a command
            MoveBootCommand cmd = new MoveBootCommand(startForCommand, destinationForCommand, bootForCommand);

            // execute locally
            cmd.execute();

            // send the command using the CommunicationsManager
            try {
                gameManager.getComs().sendGameCommandToAllPlayers(cmd);
            } catch (IOException e) {
                LOGGER.severe("The was a problem sending the command to move the boot!");
                e.printStackTrace();
            }

            // let the player choose whether they wish to draw cards or take coins for their movement
            if (GameRuleUtils.isElfengoldVariant() && !magicFlightSuccess) {
                ((EGGameScreen) GameScreen.getInstance()).showTravelOptionPopup(selectedTown, road);
            }
            
            bootMoved = true;

        } else { // Move Boot fails
            GameScreen.displayMessage("You cannot move to the destination town with the selected cards. Please try again.");
        }

        clearSelection();
    }

    public void setInExternalWindow(boolean inExternalWindow) {
        this.inExternalWindow = inExternalWindow;
    }
    
    public boolean getInExternalWindow() {
    	return this.inExternalWindow;
    }
    
    public boolean getInExchange() {
    	return this.inExchange;
    }

    /**
     * Clears all selection states.
     * Whenever a new selection state is added to GameState, remember to clear it here.
     */
    public void clearSelection() {
        LOGGER.info("Selection cleared");
        selectedRoad = null;
        selectedCounter = null;
        assert selectedCards.stream().allMatch(CardUnit::isSelected);
        selectedCards.forEach(c -> c.setSelected(false));
        selectedCards.clear();
        selectedTown = null;

        for (CounterUnit c : GameManager.getInstance().getThisPlayer().getHand().getCounters()) {
            c.setSelected(false);
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setCardsToBeDrawn(int numToDraw) {
        cardsToBeDrawn = numToDraw;
    }

    public void decrementCardsToBeDrawn() {
        cardsToBeDrawn--;

        // cant be negative
        if (cardsToBeDrawn < 0) {
            cardsToBeDrawn = 0;
        }
    }

    public int getCardsToBeDrawn() {
        return cardsToBeDrawn;
    }
    
    public boolean getBootMoved() {
    	return bootMoved; 
    }
    
    public void setBootMoved(boolean pBootMoved) {
    	bootMoved = pBootMoved;
    }
}