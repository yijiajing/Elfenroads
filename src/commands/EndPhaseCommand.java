package commands;

import domain.GameManager;
import networking.GameState;

public class EndPhaseCommand implements GameCommand{


    public EndPhaseCommand()
    {
        // doesn't need to encode any information
    }


    @Override
    public void execute()
    {
        GameManager.getInstance().endPhase();
    }
}
