package windows;

import gamemanager.GameManager;
import savegames.Savegame;
import utils.NetworkUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
//import java.lang.System.Logger;
import java.awt.*;

public class LoadGameWindow extends JPanel {

    LoadGameWindow()
    {
        MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        JFileChooser f = new JFileChooser("./out/saves");
        FileNameExtensionFilter filter = new FileNameExtensionFilter ("Savegame files", ".elf");
        f.addChoosableFileFilter(filter);
        //f.setFileFilter(filter);
        f.showOpenDialog(this);
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);//.DIRECTORIES_ONLY); 
        //f.showSaveDialog(null);

        // the user has selected a .elf savegame file
        File saveFile = f.getSelectedFile(); // this file is a .elf file representing a savegame object
        try
        {
            Savegame loaded = Savegame.read(saveFile);
            String localIP = NetworkUtils.getLocalIPAddPort();
            GameManager.init(Optional.of(loaded), loaded.getSessionID(), loaded.getGameVariant(), localIP);
        }
        catch (Exception e)
        {
            Logger.getGlobal().severe("There was a problem reading in the savegame from " + saveFile.getAbsolutePath());
            e.printStackTrace();
        }


        

    }
}
