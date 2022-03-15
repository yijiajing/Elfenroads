package commands;

import domain.GameManager;
import enums.RoundPhaseType;
import networking.GameState;
import panel.GameScreen;

import java.util.logging.Logger;

/**
 * This command should be sent to a specific player (the current player).
 * On execution, it starts the turn of the recipient player within the specified phase.
 */
public class NotifyTurnCommand implements GameCommand {

    private final RoundPhaseType phase;

    public NotifyTurnCommand(RoundPhaseType phase) {
        this.phase = phase;
    }

    @Override
    public void execute() {

        GameManager gameManager = GameManager.getInstance();
        Logger.getGlobal().info("It is now my turn");
        GameState.instance().setCurrentPlayer(gameManager.getThisPlayer());
        Logger.getGlobal().info("Phase is " + phase);
        GameState.instance().setCurrentPhase(phase);

        switch (phase) {
            case DEAL_CARDS:
                gameManager.distributeTravelCards();
                break;
            case DEAL_HIDDEN_COUNTER:
                gameManager.distributeHiddenCounter();
                break;
            case DRAW_COUNTER_ONE: case DRAW_COUNTER_TWO: case DRAW_COUNTER_THREE:
                gameManager.drawCounters();
                break;
            case PLAN_ROUTES_ONE: case PLAN_ROUTES_TWO: case PLAN_ROUTES_THREE: case PLAN_ROUTES_FOUR:
                case PLAN_ROUTES_FIVE: case PLAN_ROUTES_SIX:
                gameManager.planTravelRoutes();
                break;
            case MOVE:
                gameManager.moveOnMap();
                break;
            case RETURN_COUNTERS:
                gameManager.returnCountersPhase();
                break;
        }
        GameScreen.getInstance().updateAll();
    }
}
