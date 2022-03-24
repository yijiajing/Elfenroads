package gamemanager;

import commands.DrawCardCommand;
import domain.*;
import enums.GameVariant;
import enums.RoundPhaseType;
import gamescreen.GameScreen;
import loginwindow.MainFrame;
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
        // put 5 counters face up, these are shared across peers
        for (int i = 0; i < 5; i++) {
            this.gameState.addFaceUpCounterFromPile();
        }

        // give all players (each peer) an obstacle
        thisPlayer.getHand().addUnit(new Obstacle(MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900));

    }

    @Override
    public void setUpRound() {
        gameState.setCurrentPhase(RoundPhaseType.DEAL_CARDS);
        gameState.setToFirstPlayer();
        gameState.getTravelCardDeck().shuffle(); // only shuffle once at the beginning of each round

        // Triggered only on one instance (the first player)
        if (isLocalPlayerTurn()) {
            distributeTravelCards(); // distribute cards to each player (PHASE 1)
            GameScreen.getInstance().updateAll();
        }
    }

    public void distributeTravelCards() {

        LOGGER.info("Distributing travel cards...");
        LOGGER.info("Local player turn: " + isLocalPlayerTurn());

        if (!(isLocalPlayerTurn() && gameState.getCurrentPhase() == RoundPhaseType.DEAL_CARDS)) return;

        int numCards = thisPlayer.getHand().getNumTravelCards();
        for (int i = numCards; i < 8; i++) {
            thisPlayer.getHand().addUnit(gameState.getTravelCardDeck().draw());
        }
        LOGGER.info("Added " + (8 - numCards) + " travel cards...");
        LOGGER.info(getThisPlayer().getHand().getCards().toString());

        int numDrawn = 8 - numCards;
        DrawCardCommand drawCardCommand = new DrawCardCommand(numDrawn);
        try {
            coms.sendGameCommandToAllPlayers(drawCardCommand);
        } catch (IOException e) {
            LOGGER.info("There was a problem sending the command to draw cards!");
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
