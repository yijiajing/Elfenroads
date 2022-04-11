package commands;


// when someone in the game leaves, they will force everyone to save the game

import gamemanager.GameManager;
import savegames.Savegame;

import javax.swing.*;
import java.util.logging.Logger;

public class SaveGameCommand implements GameCommand {

    String saverName;

    public SaveGameCommand ()
    {
        // tell everyone who forced the save
        saverName = GameManager.getInstance().getThisPlayer().getName();
    }

    @Override
    public void execute() {
        // save the game, and notify about who forced the exit
        JOptionPane.showMessageDialog(null, saverName + " has saved the game, which has automatically saved it on your computer as well.");
        try {Savegame.saveGameToFile();}
        catch (Exception e)
        {
            Logger.getGlobal().info("There was a problem saving the game.");
            e.printStackTrace();
        }
    }
}
