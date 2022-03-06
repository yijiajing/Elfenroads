package commands;

import domain.GameManager;
import enums.RoundPhaseType;
import panel.GameScreen;

public class NotifyTurnCommand implements GameCommand {

    private final RoundPhaseType phase;

    public NotifyTurnCommand(RoundPhaseType phase) {
        this.phase = phase;
    }

    @Override
    public void execute() {
        GameManager gameManager = GameManager.getInstance();
        switch (phase) {
            case DEAL_CARDS:
                gameManager.distributeTravelCards();
                break;
            case DEAL_HIDDEN_COUNTERS:
                gameManager.distributeHiddenCounter();
                break;
            case DRAW_COUNTERS:
                gameManager.drawCounters();
                break;
            case PLAN_ROUTES:
                gameManager.planTravelRoutes();
                break;
            case MOVE:
                gameManager.moveOnMap();
                break;
            case COMPLETED:
                break;
        }
        GameScreen.getInstance().updateAll();
    }
}
