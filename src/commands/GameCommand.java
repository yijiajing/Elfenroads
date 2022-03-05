package commands;

import domain.GameManager;
import networking.ActionManager;
import panel.GameScreen;

import java.io.Serializable;

public interface GameCommand extends Serializable {

    // we need to provide some information to the execute method so that it knows which parts of the UI to mess with
    // we cannot have the UI elements as part of the command itself because we cannot serialize them and cannot send them over the network
    void execute(GameManager manager);

}
