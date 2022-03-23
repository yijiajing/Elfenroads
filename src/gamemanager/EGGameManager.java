package gamemanager;

import domain.TransportationCounter;
import enums.GameVariant;
import networking.GameState;
import utils.GameRuleUtils;

import java.util.Optional;

public class EGGameManager extends GameManager {

    EGGameManager(Optional<GameState> loadedState, String sessionID, GameVariant variant) {
        super(loadedState, sessionID, variant);
        assert GameRuleUtils.isElfengoldVariant(variant);
    }

    @Override
    protected void setUpNewGame() {

    }

    @Override
    public void setUpRound() {

    }

    @Override
    public void returnCounter(TransportationCounter toKeep) {

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
