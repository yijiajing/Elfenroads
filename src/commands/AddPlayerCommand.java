package commands;

import domain.Player;
import enums.Colour;
import networking.GameState;

public class AddPlayerCommand implements GameCommand{

    // encodes the info of the player that the receiving computer needs to add to his game
    private Colour color;
    private String name;

    public AddPlayerCommand(String playerName, Colour playerColor)
    {
        name = playerName;
        color = playerColor;
    }

    public void execute()
    {
        // just save the player on the receiving computer
        GameState.instance().addPlayer(new Player(color, name));
    }


}
