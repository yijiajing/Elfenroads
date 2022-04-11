package test;

import domain.CardUnit;
import domain.CounterUnit;
import domain.Player;
import enums.Colour;
import enums.GameVariant;
import gamemanager.GameManager;
import gamescreen.GameScreen;
import networking.GameSession;
import networking.GameState;
import networking.User;
import savegames.Savegame;
import utils.NetworkUtils;
import windows.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class TestLoadedGame {

    /**
     * A test class that displays the GameScreen only, without having to login and create a session
     *
     * IMPORTANT: Comment out the MainFrame constructor and uncomment the test constructor
     *
     * The game screen won't show up immediately. Wait until the message "Showing game screen" is logged to the console.
     * At this point, if the screen is still white, try adjusting the size of the frame. The game screen should show up now.
     */

    public static void main(String[] args) {
        MainFrame mainFrame = MainFrame.getInstance();

        try
        {
            JFileChooser f = new JFileChooser("./out/saves");
            FileNameExtensionFilter filter = new FileNameExtensionFilter ("Savegame files", ".elf");
            f.addChoosableFileFilter(filter);
            f.setFileSelectionMode(JFileChooser.FILES_ONLY);//.DIRECTORIES_ONLY);
            //f.setFileFilter(filter);
            int selection = f.showOpenDialog(null);
            // if the user cancelled without selecting something, go back to the lobby window
            if (selection == JFileChooser.CANCEL_OPTION)
            {

            }
            else
            {
                // the user has selected a .elf savegame file
                File saveFile = f.getSelectedFile(); // this file is a .elf file representing a savegame object
                Savegame saved = Savegame.read(f.getSelectedFile());
                System.out.println(saved.getSaveGameID());
                System.out.println(saved.getPlayers());
        }



        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}