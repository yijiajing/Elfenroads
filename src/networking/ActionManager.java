package networking;

import commands.*;
import domain.*;
import enums.RoundPhaseType;
import panel.ElfBootPanel;
import panel.GameScreen;
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
    private CounterUnit selectedCounter;
    private final List<TravelCard> selectedCards = new ArrayList<>();
    private Town selectedTown;
    boolean obstacleSelected = false;

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
     * @param road the road that the player clicks on
     */
    public void setSelectedRoad(Road road) {
        LOGGER.info("Road on " + road.getRegionType() + " selected");
        LOGGER.info("Before removing the counter, counters in hand: " +
                GameManager.getInstance().getThisPlayer().getHand().getCounters().toString());
        selectedRoad = road;

        if (!(GameRuleUtils.isPlanRoutesPhase() && gameManager.isLocalPlayerTurn())) {
            return;
        }

        // Player intends to place an obstacle
        if (selectedCounter instanceof Obstacle) {
            if (selectedRoad.placeObstacle((Obstacle) selectedCounter)) {
                gameManager.getThisPlayer().getHand().removeUnit(selectedCounter);
                Logger.getGlobal().info("Just removed obstacle, obstacle presence: " + gameManager.getThisPlayer().getHand().getObstacle());
                PlaceObstacleCommand toSendOverNetwork = new PlaceObstacleCommand(selectedRoad);
                
                try {
                    gameManager.getComs().sendGameCommandToAllPlayers(toSendOverNetwork);
                    GameScreen.getInstance().updateAll();
                } catch (IOException e) {
                    LOGGER.info("There was a problem sending the command to place the obstacle!");
                    e.printStackTrace();
                }
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

                Logger.getGlobal().info("Just removed " + counter.getType() +
                        ", current counters in hand: " +
                        GameManager.getInstance().getThisPlayer().getHand().getCounters().toString());
                
                PlaceTransportationCounterCommand toSendOverNetwork = new PlaceTransportationCounterCommand(selectedRoad, counter);
                try {
                    gameManager.getComs().sendGameCommandToAllPlayers(toSendOverNetwork);
                    GameScreen.getInstance().updateAll();
                } catch (IOException e) {
                    LOGGER.info("There was a problem sending the command to place the transportation counter!");
                    e.printStackTrace();
                }
            } else { // Invalid move
                GameScreen.displayMessage("You cannot place a transportation counter here. Please try again.");
            }
        }
        selectedCounter = null;
        selectedRoad = null;
    }

    public CounterUnit getSelectedCounter() {
        return selectedCounter;
    }

    public void setSelectedCounter(CounterUnit pCounter) {
     
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
     * @param town the town that the player clicks on
     */
    public void setSelectedTown(Town town) {
        LOGGER.info("Town " + town.getName() + " selected");
        selectedTown = town;

        if (!(gameState.getCurrentPhase() == RoundPhaseType.MOVE
                && !selectedCards.isEmpty()
                && gameManager.isLocalPlayerTurn())) {
            return;
        }

        if (GameRuleUtils.validateMove(GameMap.getInstance(), gameState.getCurrentPlayer().getCurrentTown(), selectedTown, selectedCards)) {
            // remove cards from the local player's hand
            gameState.getCurrentPlayer().getHand().removeUnits(selectedCards);
            // add cards back to local deck
            gameState.getTravelCardDeck().addDrawables(selectedCards);

            // add cards to other peers' remote decks
            try {
                gameManager.getComs().sendGameCommandToAllPlayers(new ReturnTravelCardsCommand(selectedCards));
            } catch (IOException e) {
                LOGGER.info("There was a problem sending the ReturnTravelCardsCommand!");
                e.printStackTrace();
            }

            GameScreen.getInstance().addCards(); // draws updated hand to the screen

            // Move Boot
            // gameState.getCurrentPlayer().setCurrentTown(selectedTown);
            // MoveBootCommand.execute() does the above line now. I just left this here for reference
            ElfBoot boot = gameState.getCurrentPlayer().getBoot();
            ElfBootPanel startForCommand = boot.getCurPanel();
            ElfBoot bootForCommand = boot;
            ElfBootPanel destinationForCommand = selectedTown.getPanel().getElfBootPanel();
            // boot.setCurPanel(destinationForCommand);
            // leaving that here for reference as well.

            // TODO: remove this. just for testing
            if (startForCommand == null || destinationForCommand == null || bootForCommand == null) {
                System.out.println("Something went wrong! The fields in the command to send were not determined correctly!");
            }

            // boot has been successfully moved and is no longer selected
            boot.setSelected(false);

            // now, construct a command
            MoveBootCommand cmd = new MoveBootCommand(startForCommand, destinationForCommand, bootForCommand);

            // execute locally
            cmd.execute();

            // send the command using the CommunicationsManager
            try {
                gameManager.getComs().sendGameCommandToAllPlayers(cmd);
            } catch (IOException e) {
                LOGGER.info("There was a problem sending the command to move the boot!");
                e.printStackTrace();
            }
        } else { // Move Boot fails
            GameScreen.displayMessage("You cannot move to the destination town with the selected cards. Please try again.");
        }
        selectedTown = null;
        assert selectedCards.stream().allMatch(CardUnit::isSelected);

        selectedCards.forEach(c -> c.setSelected(false));
        selectedCards.clear();
    }

    /**
     * Clears all selection states.
     * Whenever a new selection state is added to GameState, remember to clear it here.
     */
    public void clearSelection() {
        selectedRoad = null;
        selectedCounter = null;
        selectedCards.forEach(c -> c.setSelected(false));
        selectedCards.clear();
        selectedTown = null;
        obstacleSelected = false;
    }

    public GameState getGameState() {
        return gameState;
    }
}
