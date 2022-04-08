package gamescreen;

import domain.*;
import enums.GameVariant;
import gamemanager.GameManager;
import networking.GameState;
import panel.ScoreBoardPanel;
import windows.ChooseCounterPopup;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//TODO: modify everything for Elfengold.
public class EGGameScreen extends GameScreen {

    protected final JPanel panelForDeckOfTravelCards = new JPanel();
    protected final JPanel backgroundPanel_ForFaceUpTravelCards = new JPanel();
    protected final JPanel panelForGoldCardDeck = new JPanel();
    protected final JPanel[] panelForFaceUpTravelCards = new JPanel[3];


    EGGameScreen(JFrame frame, GameVariant variant) {
        super(frame, variant);
    }

    @Override
    public void updateAll() {
        updateTransportationCounters(); // updates the player's counter area
        updateCards(); // update's the player's cards
        updateFaceUpTravelCards(); // updates the face-up transportation counters
        notifyObservers(); // updates elf boots and town pieces
        updateLeaderboard();
    }

    @Override
    public void initialization()
    {
        Logger.getGlobal().info("Initializing...");
        initializeMapImage();
        initializeRoundCardImage(1);
        initializeTransportationCounters();
        initializeBackgroundPanels();
        initializeCardPanels();
        initializeInformationCardImage();
        initializeFaceUpTravelCards();
        initializeDeckOfTravelCards();
        initializeLeaderboard();
        initializeEndTurnButton();

        initializeMenuButton();
        initializeMenu();
        initializeChat();

        updateAll();
    }


    public void initializeBackgroundPanels() {
        // Set Bounds for background Player's Transportation Counter zone
        backgroundPanel_ForTransportationCounters.setBounds(width * 0 / 1440, height * 623 / 900, width * 900 / 1440, height * 70 / 900);
        backgroundPanel_ForTransportationCounters.setBackground(Color.DARK_GRAY);

        // Set Bounds for background Image zone
        backgroundPanel_ForMap.setBounds(width * 0 / 1440, height * 0 / 900, width * 1150 / 1440, height * 625 / 900);
        backgroundPanel_ForMap.setBackground(Color.BLUE);

        // Set Bounds for background Round zone
        backgroundPanel_ForRound.setBounds(width * 1005 / 1440, height * 34 / 900, width * 86 / 1440, height * 130 / 900);
        backgroundPanel_ForRound.setOpaque(false);

        // Set Bounds for background Cards zone
        backgroundPanel_ForCards.setBounds(width * 0 / 1440, height * 690 / 900, width * 1150 / 1440, height * 3 / 9);
        backgroundPanel_ForCards.setBackground(Color.WHITE);

        // Set Bounds for background Information zone
        backgroundPanel_ForInformationCard.setBounds(width * 1150 / 1440, height * 565 / 900, width * 290 / 1440, height * 330 / 900);
        backgroundPanel_ForInformationCard.setBackground(Color.WHITE);

        // Set Bounds for background Face Up Travel Card zone
        backgroundPanel_ForFaceUpTravelCards.setBounds(width * 1150 / 1440, height * 251 / 900, width * 290 / 1440, height * 311 / 900);
        backgroundPanel_ForFaceUpTravelCards.setBackground(Color.DARK_GRAY);

        backgroundPanel_ForLeaderboard.setBounds(width * 1150 / 1440, height * 0 / 900, width * 290 / 1440, height * 250 / 900);
        backgroundPanel_ForLeaderboard.setBackground(Color.DARK_GRAY);
    }

    public void initializeDeckOfTravelCards()
    {
        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);

        JPanel panel = panelForDeckOfTravelCards;
        panel.setBounds(width*1185/1440, height*257/900, width*100/1440, height/6);
        panel.setOpaque(false);
        //panel.setBorder(whiteLine);
        boardGame_Layers.add(panel, 0);
    }

    public void initializeFaceUpTravelCards()
    {
        Logger.getGlobal().info("Initializing face up travel cards");
        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
        JPanel panel2 = new JPanel();
        panel2.setBounds(width*1307/1440, height*257/900, width*100/1440, height/6);
        panel2.setOpaque(false);
        //panel2.setBorder(whiteLine);
        panelForFaceUpTravelCards[0] = panel2;
        boardGame_Layers.add(panel2, 0);

        JPanel panel3 = new JPanel();
        panel3.setBounds(width*1185/1440, height*392/900, width*100/1440, height/6);
        panel3.setOpaque(false);
        //panel3.setBorder(whiteLine);
        panelForFaceUpTravelCards[1] = panel3;
        boardGame_Layers.add(panel3, 0);

        JPanel panel4 = new JPanel();
        panel4.setBounds(width*1307/1440, height*392/900, width*100/1440, height/6);
        panel4.setOpaque(false);
        //panel4.setBorder(whiteLine);
        panelForFaceUpTravelCards[2] = panel4;
        boardGame_Layers.add(panel4, 0);
        
        //panel for num of gold card deck
        JPanel panel5 = panelForGoldCardDeck;
        panel5.setBounds(width*1200/1440, height*20/900, width*250/1440, height*20/900);
        panel5.setLayout(new GridBagLayout());
        JLabel goldCardDeck = new JLabel("Gold Cards: " + GameState.instance().getGoldCardDeckCount());
        panel5.add(goldCardDeck);

    }

    public void initializeTransportationCounters()
    {
        int xCoordinate = width*10/1440;

        // Transportation Counters
        for (int i = 0; i < 5; i++)
        {
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setBorder(whiteLine);
            panel.setBounds(xCoordinate, height*625/900, width*70/1440, height*65/900);
            panelForPlayerTransportationCounters[i] = panel;
            xCoordinate += width*100/1440;
            boardGame_Layers.add(panel, 0);
        }
    }

    @Override
    public void initializeLeaderboard() {
        List<Player> aPlayers = GameState.instance().getPlayers();
        // for debugging. feel free to delete
        Logger.getGlobal().info("Player list size in leaderboard init: " + aPlayers.size());
        for (Player cur : aPlayers)
        {
            Logger.getGlobal().info("In leaderboard initialization: " + cur.getName());
        }

        backgroundPanel_ForLeaderboard.setLayout(new BoxLayout(backgroundPanel_ForLeaderboard, BoxLayout.Y_AXIS));
        backgroundPanel_ForLeaderboard.setAlignmentX(CENTER_ALIGNMENT);
        for (Player player : aPlayers) {
            backgroundPanel_ForLeaderboard.add(new ScoreBoardPanel(this, player, true));
            backgroundPanel_ForLeaderboard.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }

    @Override
    public void addImages()
    {
        backgroundPanel_ForMap.add(mapImage_BottomLayer);
        backgroundPanel_ForInformationCard.add(informationCardImage_TopLayer);
        panelForDeckOfTravelCards.add(GameState.instance().getTravelCardDeck().getImage());

        drawTownPieces();
        drawGoldValueTokens();
        notifyObservers();
    }

    @Override
    public void addPanelToScreen()
    {
        boardGame_Layers.add(backgroundPanel_ForRound, 0);
        boardGame_Layers.add(panelForDeckOfTravelCards,0);
        boardGame_Layers.add(backgroundPanel_ForTransportationCounters, -1);
        boardGame_Layers.add(backgroundPanel_ForMap, -1);
        boardGame_Layers.add(backgroundPanel_ForCards, -1);
        boardGame_Layers.add(backgroundPanel_ForInformationCard, -1);
        boardGame_Layers.add(backgroundPanel_ForFaceUpTravelCards, -1);
        boardGame_Layers.add(backgroundPanel_ForLeaderboard,-1);

        for (Town town: gameMap.getTownList()) {
            boardGame_Layers.add(town.getPanel(), 0);
            boardGame_Layers.add(town.getElfBootPanel(), 0);

            if (town.getTokenPanel() != null) {
                boardGame_Layers.add(town.getTokenPanel(), 0);
            }
        }
    }

    public void updateFaceUpTravelCards()
    {
        ArrayList<TravelCard> faceUpCards = GameState.instance().getFaceUpCards();

        // clear the previous counters from the screen
        for (JPanel panel : panelForFaceUpTravelCards) {
            if (panel != null) {
                panel.removeAll();
                panel.repaint();
                panel.revalidate();
            }
        }

        for (int i = 0; i < 3; i++) {
            JPanel panel = panelForFaceUpTravelCards[i];
            CardUnit card = faceUpCards.get(i);
            panel.add(card.getMiniDisplay());
            panel.repaint();
            panel.revalidate();
        }
        
        //update goldcard deck
        panelForGoldCardDeck.removeAll();
        panelForGoldCardDeck.repaint();
        panelForGoldCardDeck.revalidate();
        panelForGoldCardDeck.setLayout(new GridBagLayout());
        JLabel goldCardDeck = new JLabel("Gold Cards: " + GameState.instance().getGoldCardDeckCount());
        panelForGoldCardDeck.add(goldCardDeck); 
        panelForGoldCardDeck.repaint();
        panelForGoldCardDeck.revalidate();
    }

    public void updateTransportationCounters()
    {
        // remove a counter if it was already there
        for (JPanel panel : panelForPlayerTransportationCounters) {
            if (panel != null) {
                panel.removeAll();
                panel.repaint();
                panel.revalidate();
            }
        }

        List<CounterUnit> counters = GameManager.getInstance().getThisPlayer().getHand().getCounters();

        // draw the counters to the screen
        for (int c = 0; c < counters.size(); c++) {
            JPanel panel = panelForPlayerTransportationCounters[c];
            CounterUnit counter = counters.get(c);
            panel.add(counter.getDisplay());
            panel.repaint();
            panel.revalidate();
        }
    }

    private void drawGoldValueTokens() {
        // put gold value token on every town
        for (Town t : GameMap.getInstance().getTownList()) {
            if (t.getTokenPanel() != null && !t.getName().equals("Elvenhold")) {
                t.getTokenPanel().drawGoldValueToken();
            }
        }
    }

    public void hideCounterPopup() {
        boardGame_Layers.remove(counterPopup);
    }
}
