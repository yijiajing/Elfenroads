package gamemanager;

import commands.DrawCounterCommand;
import domain.*;
import enums.EGRoundPhaseType;
import enums.GameVariant;
import gamescreen.EGGameScreen;
import gamescreen.GameScreen;
import networking.GameState;
import utils.GameRuleUtils;

import java.io.IOException;
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
        // initial preparation: deal five cards to each player
        for (Player p: gameState.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                p.getHand().addUnit(gameState.getTravelCardDeck().draw());
            }
        }

        // put 3 cards face up, these are shared across peers
        for (int j = 0; j < 3; j++) {
            this.gameState.addFaceUpCardFromDeck();
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

    /**
     * PHASE 4
     */
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
        thisPlayer.getHand().getCounters().add((TransportationCounter) counter1);
        thisPlayer.getHand().getCounters().add((TransportationCounter) counter2);

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
        DrawCounterCommand cmd1 = new DrawCounterCommand(counter1, !counter1.isSecret());
        DrawCounterCommand cmd2 = new DrawCounterCommand(counter2, !counter2.isSecret());
        try {
            coms.sendGameCommandToAllPlayers(cmd1);
            coms.sendGameCommandToAllPlayers(cmd2);
        } catch (IOException e) {
            LOGGER.info("There was a problem sending the command to draw counters!");
            e.printStackTrace();
        }

        endTurn();
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
