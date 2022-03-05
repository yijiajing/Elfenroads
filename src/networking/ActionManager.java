package networking;

import domain.*;
import enums.RoundPhaseType;
import utils.GameRuleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ActionManager {

    private static ActionManager INSTANCE;

    private final static Logger LOGGER = Logger.getLogger("game state");

    private GameState gameState;

    private Road selectedRoad;
    private TransportationCounter selectedCounter;
    private List<TravelCard> selectedCards = new ArrayList<>();
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

    public void setSelectedRoad(Road selectedRoad) {
        LOGGER.info("Road on " + selectedRoad.getRegionType() + " selected");
        this.selectedRoad = selectedRoad;

        if (gameState.getCurrentPhase() == RoundPhaseType.PLANROUTES) {
            // Player intends to place an obstacle
            if (obstacleSelected) {
                if (!selectedRoad.placeObstacle()) {
                    //TODO: display failure message and deselect obstacle in UI
                }
            }
            // Player intends to place a transportation counter
            if (!selectedRoad.setTransportationCounter(selectedCounter)) {
                //TODO: display failure message and deselect counter in UI
            }
        }
    }

    public void obstacleSelected() {
        LOGGER.info("Obstacle selected");
        obstacleSelected = !obstacleSelected;
        selectedCounter = null;
    }

    public TransportationCounter getSelectedCounter() {
        return selectedCounter;
    }

    public void setSelectedCounter(TransportationCounter selectedCounter) {
        LOGGER.info("Counter " + selectedCounter.getType() + " selected");
        this.selectedCounter = selectedCounter;
    }

    public List<TravelCard> getSelectedCards() {
        return selectedCards;
    }

    public void addSelectedCard(TravelCard selectedCard) {
        LOGGER.info("Card " + selectedCard.getType() + " selected");
        if (selectedCards.contains(selectedCard)) {
            this.selectedCards.remove(selectedCard);
        }
        this.selectedCards.add(selectedCard);
    }

    public void setSelectedCards(List<TravelCard> selectedCards) {
        this.selectedCards = selectedCards;
    }

    public Town getSelectedTown() {
        return selectedTown;
    }

    public void setSelectedTown(Town selectedTown) {
        LOGGER.info("Town " + selectedTown.getName() + " selected");
        this.selectedTown = selectedTown;

        if (gameState.getCurrentPhase() == RoundPhaseType.MOVE && !selectedCards.isEmpty()) {
            if (!GameRuleUtils.validateMove(GameMap.getInstance(), gameState.getCurrentPlayer().getCurrentTown(), selectedTown, selectedCards)) {
                gameState.getCurrentPlayer().setCurrentTown(selectedTown);
            } else {
                //TODO: in UI
                // - deselect all currently selected cards in UI
                // - display message that prompts Player for re-selection
                this.selectedTown = null;
                this.selectedCards.clear();
            }
        }
    }

    /**
     * Clears all selection states.
     * Whenever a new selection state is added to GameState, remember to clear it here.
     */
    public void clearSelection() {
        selectedRoad = null;
        selectedCounter = null;
        selectedCards.clear();
        selectedTown = null;
        obstacleSelected = false;
    }

    public GameState getGameState() {
        return gameState;
    }
}
