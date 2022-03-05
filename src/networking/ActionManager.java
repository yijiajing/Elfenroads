package networking;

import commands.MoveBootCommand;
import commands.PlaceObstacleCommand;
import commands.PlaceTransportationCounterCommand;
import domain.*;
import enums.RoundPhaseType;
import panel.ElfBootPanel;
import panel.GameScreen;
import utils.GameRuleUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ActionManager {

    private static ActionManager INSTANCE;

    private final static Logger LOGGER = Logger.getLogger("game state");

    private final GameState gameState;
    private final GameManager gameManager = GameManager.getInstance();

    private Road selectedRoad;
    private CounterUnit selectedCounter;
    private final List<TravelCard> selectedCards = new ArrayList<>();
    private Town selectedTown;
    boolean obstacleSelected = false;

    public static ActionManager init(GameState gameState) {
        if (INSTANCE == null) {
            INSTANCE = new ActionManager(gameState);
        }
        return INSTANCE;
    }

    private ActionManager(GameState gameState) {
        this.gameState = gameState;
    }

    public static ActionManager getInstance() {
        return INSTANCE;
    }

    /*
    Selection handling:
    - counter unit + road + PLANROUTES phase -> place a counter unit on the road
    - card + town + MOVE phase -> requested player move to a town
    */

    public Road getSelectedRoad() {
        return selectedRoad;
    }

    public void setSelectedRoad(Road road) {
        LOGGER.info("Road on " + road.getRegionType() + " selected");
        selectedRoad = road;

        if (!(gameState.getCurrentPhase() == RoundPhaseType.PLANROUTES
                && gameManager.isLocalPlayerTurn())) {
            return;
        }

        // Player intends to place an obstacle
        if (selectedCounter instanceof Obstacle) {
            if (selectedRoad.placeObstacle((Obstacle) selectedCounter)) {
                PlaceObstacleCommand toSendOverNetwork = new PlaceObstacleCommand(selectedRoad);
                try {
                    gameManager.getComs().sendGameCommand(toSendOverNetwork);
                } catch (IOException e) {
                    LOGGER.info("There was a problem sending the command to place the obstacle!");
                    e.printStackTrace();
                }
            } else { // Invalid move
                GameScreen.displayMessage("""
                You cannot place an obstacle here. Please try again.
                """, false, false);
                selectedCounter = null;
            }
        }

        // Player intends to place a transportation counter
        else if (selectedCounter instanceof TransportationCounter) {
            if (selectedRoad.setTransportationCounter((TransportationCounter) selectedCounter)) {
                PlaceTransportationCounterCommand toSendOverNetwork = new PlaceTransportationCounterCommand(selectedRoad, (TransportationCounter) selectedCounter);
                try {
                    gameManager.getComs().sendGameCommand(toSendOverNetwork);
                } catch (IOException e) {
                    LOGGER.info("There was a problem sending the command to place the transportation counter!");
                    e.printStackTrace();
                }
            } else { // Invalid move
                GameScreen.displayMessage("""
                        You cannot place a transportation counter here. Please try again.
                        """, false, false);
                selectedCounter = null;
            }
        }
    }

    public CounterUnit getSelectedCounter() {
        return selectedCounter;
    }

    public void setSelectedCounter(CounterUnit selectedCounter) {
        if (selectedCounter instanceof Obstacle) {
            LOGGER.info("Obstacle selected");
        } else if (selectedCounter instanceof TransportationCounter) {
            LOGGER.info("Counter " + ((TransportationCounter) selectedCounter).getType() + " selected");
        }

        this.selectedCounter = selectedCounter;
    }

    public List<TravelCard> getSelectedCards() {
        return selectedCards;
    }

    public void addSelectedCard(TravelCard selectedCard) {
        LOGGER.info("Card " + selectedCard.getType() + " selected");
        if (selectedCards.contains(selectedCard)) {
            // if clicked twice, then deselect this card
            this.selectedCards.remove(selectedCard);
            selectedCard.setSelected(false);
        } else {
            // add to selected cards
            this.selectedCards.add(selectedCard);
            assert !selectedCard.isSelected();
            selectedCard.setSelected(true);
        }
    }

    public void setSelectedCards(List<TravelCard> selectedCards) {
        selectedCards.forEach(this::addSelectedCard);
    }

    public Town getSelectedTown() {
        return selectedTown;
    }

    public void setSelectedTown(Town selectedTown) {
        LOGGER.info("Town " + selectedTown.getName() + " selected");
        this.selectedTown = selectedTown;

        if (!(gameState.getCurrentPhase() == RoundPhaseType.MOVE
                && !selectedCards.isEmpty()
                && gameManager.isLocalPlayerTurn())) {
            return;
        }

        if (GameRuleUtils.validateMove(GameMap.getInstance(), gameState.getCurrentPlayer().getCurrentTown(), selectedTown, selectedCards)) {
            // Move Boot
            gameState.getCurrentPlayer().setCurrentTown(selectedTown);
            ElfBoot boot = gameState.getCurrentPlayer().getBoot();
            ElfBootPanel startForCommand = boot.getCurPanel();
            ElfBoot bootForCommand = boot;
            ElfBootPanel destinationForCommand = this.selectedTown.getPanel().getElfBootPanel();
            boot.setCurPanel(destinationForCommand);
            // TODO: remove this. just for testing
            if (startForCommand == null || destinationForCommand == null || bootForCommand == null)
            {
                System.out.println("Something went wrong! The fields in the command to send were not determined correctly!");
            }

            // boot has been successfully moved and is no longer selected
            boot.setSelected(false);

            // now, construct a command and notify the CommunicationsManager so that it can send the movement to other players in the game
            MoveBootCommand toSendOverNetwork = new MoveBootCommand(startForCommand, destinationForCommand, bootForCommand);
            try {
                gameManager.getComs().sendGameCommand(toSendOverNetwork);
            } catch (IOException e) {
                LOGGER.info("There was a problem sending the command to move the boot!");
                e.printStackTrace();
            }
        } else { // Move Boot fails
            GameScreen.displayMessage("""
            You cannot move to the destination town with the selected cards. Please try again.
            """, false, false);
            this.selectedTown = null;
            assert this.selectedCards.stream().allMatch(CardUnit::isSelected);
            this.selectedCards.forEach(this::clearSelectedCard);
        }

    }

    public void clearSelectedCard(TravelCard card) {
        assert selectedCards.contains(card);
        assert card.isSelected();
        selectedCards.remove(card);
        card.setSelected(false);
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
