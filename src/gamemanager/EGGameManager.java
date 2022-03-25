package gamemanager;

import domain.*;
import enums.EGRoundPhaseType;
import enums.GameVariant;
import gamescreen.GameScreen;
import networking.GameState;
import utils.GameRuleUtils;

import java.util.Optional;
import java.util.logging.Logger;

//TODO: modify everything for Elfengold
public class EGGameManager extends GameManager {

    private final static Logger LOGGER = Logger.getLogger("Game Manager");

    EGGameManager(Optional<GameState> loadedState, String sessionID, GameVariant variant) {
        super(loadedState, sessionID, variant);
        assert GameRuleUtils.isElfengoldVariant(variant);
    }

    @Override
    protected void setUpNewGame() {
        // put 5 counters face up, these are shared across peers
        for (int i = 0; i < 5; i++) {
            //TODO: delete this
            this.gameState.addFaceUpCounterFromPile();
        }

        //TODO: put 3 cards face up, similar to above.

        // initial preparation: deal five cards to each player
        for (Player p: gameState.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                p.getHand().addUnit(gameState.getTravelCardDeck().draw());
            }
        }
    }

    @Override
    public void setUpRound() {
        gameState.setToFirstPlayer();
        gameState.getTravelCardDeck().shuffle(); // only shuffle once at the beginning of each round
        if (gameState.getCurrentRound() == 1) {
            // Draw Card phase is ignored in the first round
            gameState.setCurrentPhase(EGRoundPhaseType.CHOOSE_FACE_UP);
            // Triggered only on one instance (the first player)
            if (isLocalPlayerTurn()) {
                chooseFaceUpCounter();
                GameScreen.getInstance().updateAll();
            }
        } else {
            gameState.setCurrentPhase(EGRoundPhaseType.DRAW_CARD_ONE);
            // Triggered only on one instance (the first player)
            if (isLocalPlayerTurn()) {
                drawTravelCard();
                GameScreen.getInstance().updateAll();
            }
        }

    }

    public void drawTravelCard() {

    }

    public void chooseFaceUpCounter() {
        if (gameState.getCurrentPhase() != EGRoundPhaseType.CHOOSE_FACE_UP) {
            return;
        }

        // distribute gold coins (beginning with the second round)
        if (gameState.getCurrentRound() > 1) {
            for (Player p: gameState.getPlayers()) {
                p.addGoldCoins(2);
            }
        }
        // distribute two items from the face-down counter pile
        CounterUnit counter1 = gameState.getCounterPile().draw();
        CounterUnit counter2 = gameState.getCounterPile().draw();

        // let the player choose which counter to place face-up (hence the other one is face-down)
        GameScreen.displayMessage("""
                It is time to decide which counter you would like to reveal to other players. Click on 
                the counter you wish to place face-up and the other counter will be placed face-down.
                """);
        //TODO: show ChooseCounterWindow
    }

    @Override
    public void returnCounter(CounterUnit toKeep) {

    }

    @Override
    public void endTurn() {

    }

    @Override
    public void endPhase() {

    }

    @Override
    public void endRound() {

    }

    @Override
    public void endGame() {

    }
}
