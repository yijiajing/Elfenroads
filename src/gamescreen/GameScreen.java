package gamescreen;

import domain.*;
import enums.CounterType;
import enums.GameVariant;
import gamemanager.GameManager;
import windows.ChatBoxGUI;
import networking.GameState;
import org.json.JSONObject;
import panel.EndTurnButton;
import panel.MenuButton;
import panel.ObserverPanel;
import panel.ScoreBoardPanel;
import utils.GameRuleUtils;
import windows.ChooseCounterPopup;
import windows.DoubleMagicSpellPopup;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class GameScreen extends JPanel implements Serializable {

    private static GameScreen INSTANCE;

    protected JFrame mainframe;
    protected static Integer width;
    protected static Integer height;

    protected JLayeredPane boardGame_Layers;

    protected Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);

    protected JLabel roundImage_TopLayer;
    protected JLabel mapImage_BottomLayer;
    protected JLabel informationCardImage_TopLayer;

    protected final JPanel backgroundPanel_ForMap = new JPanel();
    protected final JPanel backgroundPanel_ForRound = new JPanel();
    protected final JPanel backgroundPanel_ForCards = new JPanel();
    protected final JPanel backgroundPanel_ForTransportationCounters = new JPanel();
    protected final JPanel backgroundPanel_ForInformationCard = new JPanel();
    protected final JPanel backgroundPanel_ForLeaderboard = new JPanel();

    protected final JPanel[] panelForPlayerTransportationCounters = new JPanel[5];
    protected final JPanel[] panelForPlayerCards = new JPanel[8];

    protected ArrayList<ObserverPanel> observerPanels = new ArrayList<>();

    protected GameMap gameMap;

    private static String prevMessage;
    private static ChatBoxGUI chat;

    protected GameScreen(JFrame frame, GameVariant variant) {
        // layout is necessary for JLayeredPane to be added to the JPanel
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Get dimensions of the full screen
        mainframe = frame;
        width = mainframe.getWidth();
        height = mainframe.getHeight();

        // Set Bounds for entire Board Game screen and do the initialization of the structure for the UI
        boardGame_Layers = new JLayeredPane();
        boardGame_Layers.setBounds(0,0,width,height);

        // initialize town and road panels
        gameMap = GameMap.init(this, variant);
    }

    public static GameScreen init(JFrame frame, GameVariant variant) {
        if (INSTANCE == null) {
            if (GameRuleUtils.isElfengoldVariant(variant)) {
                INSTANCE = new EGGameScreen(frame, variant);
            } else {
                INSTANCE = new ELGameScreen(frame, variant);
            }
        }
        return INSTANCE;
    }

    /**
     * @return the Singleton instance of the GameScreen
     */
    public static GameScreen getInstance() {
        return INSTANCE;
    }

    /**
     * Draws all of the UI components to the screen
     */
    public void draw() {
        initialization();

        // Add the images to their corresponding JPanel
        addImages();

        // Add the JPanels to the main JLayeredPane with their corresponding layer
        addPanelToScreen();

        // Add the entire structure of the UI to the panel
        this.add(boardGame_Layers);
    }

    public void update(JPanel panel)
    {
        panel.repaint();
        panel.revalidate();
    }

    public abstract void updateAll();

    public abstract void initialization();

    public abstract void addImages();

    public abstract void addPanelToScreen();

    public void drawTownPieces() {
        // put town pieces on every town except for Elvenhold
        for (Town t : GameMap.getInstance().getTownList()) {
            if (!t.getName().equalsIgnoreCase("Elvenhold")) {
                t.initializeTownPieces();
            }
        }
    }

    public void initializeMapImage() {
        ImageIcon mapImage = new ImageIcon("./assets/sprites/map.png");
        Image map = mapImage.getImage();
        Image mapResized = map.getScaledInstance(width * 1140 / 1440, height * 625 / 900, java.awt.Image.SCALE_SMOOTH);
        mapImage = new ImageIcon(mapResized);
        mapImage_BottomLayer = new JLabel(mapImage);
    }

    public void initializeCardPanels() {
        int xCoordinate = width * 2 / 1440;

        for (int i = 0; i < 8; i++) {
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            //panel.setBorder(whiteLine);
            panel.setBounds(xCoordinate, height * 690 / 900, width * 144 / 1440, height * 3 / 9);
            panelForPlayerCards[i] = panel;
            xCoordinate += width * 144 / 1440;
            boardGame_Layers.add(panel, 0);
        }
    }

    //delete and re-initialize scoreboards
    public void updateLeaderboard() {
        backgroundPanel_ForLeaderboard.removeAll();
        this.initializeLeaderboard();
        backgroundPanel_ForLeaderboard.revalidate();
        backgroundPanel_ForLeaderboard.repaint();
    }

    public void updateCards() {
        // clear the previous cards from the screen
        for (JPanel panel : panelForPlayerCards) {
            if (panel != null) {
                panel.removeAll();
                panel.repaint();
                panel.revalidate();
            }
        }

        List<CardUnit> myCards = GameManager.getInstance().getThisPlayer().getHand().getCards();

        // draw the cards to the screen
        for (int p = 0; p < myCards.size(); p++) {
            JPanel panel = panelForPlayerCards[p];
            if (panel != null) {
                CardUnit card = myCards.get(p);
                panel.add(card.getDisplay());
                panel.repaint();
                panel.revalidate();
            }
        }
    }

    public abstract void initializeLeaderboard();

    public void initializeRoundCardImage(int round)
    {
        ImageIcon roundImage = new ImageIcon("./assets/sprites/R" + round + ".png");
        Image Round = roundImage.getImage();
        Image RoundResized = Round.getScaledInstance(width*90/1440, height*130/900,  java.awt.Image.SCALE_SMOOTH);
        roundImage = new ImageIcon(RoundResized);
        roundImage_TopLayer = new JLabel(roundImage);
        backgroundPanel_ForRound.removeAll();
        backgroundPanel_ForRound.add(roundImage_TopLayer);
    }

    public void initializeInformationCardImage()
    {
        ImageIcon gridImage = new ImageIcon("./assets/sprites/grid.png");
        Image grid = gridImage.getImage();
        Image gridResized = grid.getScaledInstance(width*290/1440, height*325/900,  java.awt.Image.SCALE_SMOOTH);
        gridImage = new ImageIcon(gridResized);
        informationCardImage_TopLayer = new JLabel(gridImage);
    }

    public void initializeEndTurnButton() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(width*1000/1440, height*625/900, width*100/1440, height*65/900);
        buttonPanel.setOpaque(false);
        boardGame_Layers.add(buttonPanel);

        JButton endTurn = new EndTurnButton();
        endTurn.setBounds(width*1000/1440, height*625/900, width*100/1440, height*65/900);
        buttonPanel.add(endTurn);
    }

    public void initializeMenuButton() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(width*1000/1440, height*625/900+30, width*100/1440, height*65/900);
        buttonPanel.setOpaque(false);
        boardGame_Layers.add(buttonPanel);

        JButton menu = new MenuButton();
        menu.setBounds(width*1000/1440, height*625/900+30, width*100/1440, height*65/900);
        buttonPanel.add(menu);
    }

    public void initializeMenu(){
        JMenu menu, submenu;
        JMenuItem i1, i2, i3, i4, i5, i6, i7;
        JMenuBar mb = new JMenuBar();
        menu = new JMenu("Menu");
        submenu = new JMenu("Rules");

        i1 = new JMenuItem("Save");
        i2 = new JMenuItem("Load");
        i3 = new JMenuItem("Chat");
        i4 = new JMenuItem("Elfenland");
        i5 = new JMenuItem("Elfengold");
        i6 = new JMenuItem("Exit to menu");
        i7 = new JMenuItem("Exit to desktop");

        i1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                GameState gamestateToSave = GameState.instance();
                JSONObject serialized = gamestateToSave.serialize();

            }

        });

        i3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                chat.setVisible(true);
            }
        });

        i4.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {

                        File myFile = new File("./assets/rules/Elfenland Rules.pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
            }});

        i5.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {

                        File myFile = new File("./assets/rules/Elfengold Rules.pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
            }});

        i6.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);

            }});

        i7.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
                //remove(background_elvenroads);
                //MainFrame.mainPanel.add(new LobbyWindow(), "lobby");
                //MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");

            }

        });
        menu.add(i1);
        menu.add(i2);
        menu.add(i3);
        submenu.add(i4);
        submenu.add(i5);
        menu.add(i6);
        menu.add(i7);
        menu.add(submenu);
        mb.add(menu);
        mainframe.setJMenuBar(mb);
    }

    public void initializeChat()
    {
        chat = ChatBoxGUI.init();
    }

    public void addElement(JPanel panel) {
        boardGame_Layers.add(panel);
        mainframe.repaint();
        mainframe.revalidate();
    }

    public void addObserverPanel(ObserverPanel pPanel) {
        this.observerPanels.add(pPanel);
    }

    public void notifyObservers() {
        for ( ObserverPanel observer : observerPanels ) {
            observer.updateView();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public static void displayMessage(String message)
    {
        prevMessage = message;
        JOptionPane.showMessageDialog(null, message);
    }

    public static String getPrevMessage()
    {
        return prevMessage;
    }

}
