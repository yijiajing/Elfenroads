package windows;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;
//import java.lang.System.Logger;
import java.awt.*;

public class LoadGameWindow extends JPanel {
    private JLabel background_elvenroads;
    private Box gameInfo;
    private JPanel gamesPanel;
    private JPanel gamePanel;
    private JScrollPane scrollPane;
    private JList<String> list;
    private JButton loadButton;
    private JButton backButton;
    private JLabel fileName;
    private JLabel players;
    private JLabel dateSaved;

    LoadGameWindow()
    {
        MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        JFileChooser f = new JFileChooser("./out/saves");
        FileNameExtensionFilter filter = new FileNameExtensionFilter ("Savegame files", ".elf");
        f.addChoosableFileFilter(filter);
        //f.setFileFilter(filter);
        f.showOpenDialog(null);
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);//.DIRECTORIES_ONLY); 
        //f.showSaveDialog(null);

        File selected = f.getSelectedFile();
        Logger.getGlobal().info("Selected savegame file: " + selected.getAbsolutePath());

        
        System.out.println(f.getCurrentDirectory());
        System.out.println(f.getSelectedFile());

        gamesPanel = new JPanel(new BorderLayout());
        gamePanel = new JPanel(new BorderLayout());
        loadButton = new JButton("Load");
        backButton = new JButton("Back");
        scrollPane = new JScrollPane();
        list = new JList<>();
        list.setModel(new AbstractListModel<String>() {
            String[] elements = {"game1", "game2", "game3", "game4"};
        

            @Override
            public int getSize() {
                
                return elements.length;
            }

            @Override
            public String getElementAt(int index) {
                
                return elements[index];
            }});

        scrollPane.setViewportView(list);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                track1.play();
                remove(background_elvenroads);
                LobbyWindow reinitialized = new LobbyWindow();
                MainFrame.setLobbyWindow(reinitialized);
                MainFrame.mainPanel.add(reinitialized, "lobby");
                MainFrame.cardLayout.show(MainFrame.mainPanel, "lobby");
            }
        });

        gamePanel.add(loadButton, BorderLayout.LINE_START);
        gameInfo = Box.createVerticalBox();
        //add(scrollPane, BorderLayout.CENTER);
        gamePanel.add(scrollPane, BorderLayout.LINE_END);
        
        //gameInfo
        fileName = new JLabel();
        fileName.setText("FILE NAME: AAA");

        players = new JLabel();
        players.setText("PLAYERS: AA; BB; CC; DD");

        dateSaved = new JLabel();
        dateSaved.setText("DATE SAVED: 9/30/2021 21:30");

        gameInfo.add(fileName);
        gameInfo.add(players);
        gameInfo.add(dateSaved);

        gamePanel.add(gameInfo,BorderLayout.CENTER);

        gamesPanel.add(gamePanel, BorderLayout.LINE_START);

        background_elvenroads = MainFrame.getInstance().getElfenroadsBackground();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 1;
        gbc.gridheight= 3;
        background_elvenroads.setLayout(layout);
        background_elvenroads.add(gamesPanel,gbc);
        background_elvenroads.add(backButton);


        add(background_elvenroads);
    }
}
