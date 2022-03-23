package commands;

import networking.GameState;

public class PassTurnCommand implements GameCommand {

    @Override
    public void execute() {
        GameState.instance().incrementPassedPlayerCount();
    }
}
