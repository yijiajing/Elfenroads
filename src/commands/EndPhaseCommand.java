package commands;

import gamemanager.GameManager;

import java.util.logging.Logger;

public class EndPhaseCommand implements GameCommand {


    public EndPhaseCommand() {
        // doesn't need to encode any information
    }


    @Override
    public void execute() {
        Logger.getGlobal().info("Executing the end phase command.");
        GameManager.getInstance().endPhase();
    }
}
