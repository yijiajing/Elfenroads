package commands;

import gamemanager.GameManager;
import domain.Player;
import enums.Colour;
import networking.GameSession;
import networking.GameState;

import java.io.IOException;
import java.util.logging.Logger;

public class AddPlayerCommand implements GameCommand{

    // encodes the info of the player that the receiving computer needs to add to his game
    private Colour color;
    private String name;

    public AddPlayerCommand(String playerName, Colour playerColor)
    {
        name = playerName;
        color = playerColor;
    }

    public void execute() {
        Logger.getGlobal().info("Received AddPlayerCommand with name " + name + " and color " + color + " , executing...");
        GameState gameState = GameState.instance();
        GameManager gameManager = GameManager.getInstance();

        // just save the player on the receiving computer
        gameState.addPlayer(new Player(color, name));
        try {
            int numPlayers = GameSession.getPlayerNames(GameManager.getInstance().getSessionID()).size();
            if (gameState.getNumOfPlayers() == numPlayers) {
                // we have added all the players to the list, start by setting up the round
                gameManager.launch();
            }
        } catch (IOException e) {
            Logger.getGlobal().info("There was a problem sending the AddPlayerCommand");
            e.printStackTrace();
        }
    }
}
