package commands;

import enums.EGRoundPhaseType;
import enums.ELRoundPhaseType;
import gamemanager.EGGameManager;
import gamemanager.ELGameManager;
import gamemanager.GameManager;
import enums.RoundPhaseType;
import networking.GameState;
import gamescreen.GameScreen;
import utils.GameRuleUtils;

import java.util.logging.Logger;

/**
 * This command should be sent to a specific player (the current player).
 * On execution, it starts the turn of the recipient player within the specified phase.
 */
public class NotifyTurnCommand implements GameCommand {

    private static final long serialVersionUID = 6529685098267757690L;

    private final RoundPhaseType phase;

    public NotifyTurnCommand(RoundPhaseType phase) {
        this.phase = phase;
    }

    @Override
    public void execute() {

        // ELFENGOLD
        if (GameRuleUtils.isElfengoldVariant(GameManager.getInstance().getVariant())) {
            EGGameManager gameManager = (EGGameManager) GameManager.getInstance();
            Logger.getGlobal().info("It is now my turn");
            GameState.instance().setCurrentPlayer(gameManager.getThisPlayer());
            Logger.getGlobal().info("Phase is " + phase);
            GameState.instance().setCurrentPhase(phase);

            switch ((EGRoundPhaseType) phase) {
                case DRAW_CARD_ONE: case DRAW_CARD_TWO: case DRAW_CARD_THREE:
                    gameManager.drawTravelCard();
                    break;
                case CHOOSE_FACE_UP:
                    gameManager.chooseFaceUpCounter();
                    break;
                case AUCTION:
                    gameManager.getAuctionFrame().takeAction();
                    break;
                case PLAN_ROUTES:
                    gameManager.planTravelRoutes();
                    break;
                case MOVE:
                    gameManager.moveOnMap();
                    break;
                case RETURN_COUNTERS:
                    gameManager.returnCountersPhase();
                    break;
            }
        }

        // ELFENLAND
        else {
            ELGameManager gameManager = (ELGameManager) GameManager.getInstance();
            Logger.getGlobal().info("It is now my turn");
            GameState.instance().setCurrentPlayer(gameManager.getThisPlayer());
            Logger.getGlobal().info("Phase is " + phase);
            GameState.instance().setCurrentPhase(phase);

            switch ((ELRoundPhaseType) phase) {
                case DEAL_CARDS:
                    gameManager.distributeTravelCards();
                    break;
                case DEAL_HIDDEN_COUNTER:
                    gameManager.distributeHiddenCounter();
                    break;
                case DRAW_COUNTER_ONE: case DRAW_COUNTER_TWO: case DRAW_COUNTER_THREE:
                    if (gameManager.getThisPlayer().getHand().getCounters().size() >= 5) {
                        GameScreen.displayMessage("You already have 5 counters in hand. You cannot draw more.");
                        gameManager.endTurn();
                    } else {
                        gameManager.drawCounters();
                    }
                    break;
                case PLAN_ROUTES:
                    gameManager.planTravelRoutes();
                    break;
                case MOVE:
                    gameManager.moveOnMap();
                    break;
                case RETURN_COUNTERS:
                    gameManager.returnCountersPhase();
                    break;
            }
        }

        GameScreen.getInstance().updateAll();
    }
}
