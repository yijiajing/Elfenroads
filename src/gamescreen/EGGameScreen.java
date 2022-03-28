package gamescreen;

import domain.*;
import enums.GameVariant;
import gamemanager.GameManager;
import networking.GameState;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//TODO: modify everything for Elfengold.
// There is no face up transportation counter or a unique place for obstacle, for example.
public class EGGameScreen extends GameScreen {

    protected final JPanel[] panelForFaceUpTravelCards = new JPanel[3];

    EGGameScreen(JFrame frame, GameVariant variant) {
        super(frame, variant);
    }

    @Override
    public void updateAll() {
        updateTransportationCountersAndObstacle(); // updates the player's counter area
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
        initializeTransportationCountersAndObstacle();
        initializeBackgroundPanels();
        initializeCardPanels();
        initializeInformationCardImage();
        initializeFaceUpTravelCards();
        initializeDeckOfTransportationCounters();
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

        // Set Bounds for background Obstacle zone
        backgroundPanel_ForObstacle.setBackground(Color.RED);
        backgroundPanel_ForObstacle.setBounds(width * 900 / 1440, height * 623 / 900, width * 80 / 1440, height * 70 / 900);

        // Set Bounds for background Image zone
        backgroundPanel_ForMap.setBounds(width * 0 / 1440, height * 0 / 900, width * 1150 / 1440, height * 625 / 900);
        backgroundPanel_ForMap.setBackground(Color.BLUE);

        // Set Bounds for background Round zone
        backgroundPanel_ForRound.setBounds(width * 1000 / 1440, height * 34 / 900, width * 86 / 1440, height * 130 / 900);
        backgroundPanel_ForRound.setOpaque(false);

        // Set Bounds for background Cards zone
        backgroundPanel_ForCards.setBounds(width * 0 / 1440, height * 690 / 900, width * 1150 / 1440, height * 3 / 9);
        backgroundPanel_ForCards.setBackground(Color.WHITE);

        // Set Bounds for background Information zone
        backgroundPanel_ForInformationCard.setBounds(width * 1150 / 1440, height * 565 / 900, width * 290 / 1440, height * 330 / 900);
        backgroundPanel_ForInformationCard.setBackground(Color.WHITE);

        // Set Bounds for background Face Up Transportation Counter zone
        backgroundPanel_ForDeckOfTransportationCounters.setBounds(width * 1150 / 1440, height * 275 / 900, width * 290 / 1440, height * 289 / 900);
        backgroundPanel_ForDeckOfTransportationCounters.setBackground(Color.DARK_GRAY);

        backgroundPanel_ForLeaderboard.setBounds(width * 1150 / 1440, height * 0 / 900, width * 290 / 1440, height * 274 / 900);
        backgroundPanel_ForLeaderboard.setBackground(Color.DARK_GRAY);
    }

    public void initializeDeckOfTransportationCounters()
    {
        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);

        JPanel panel = panelForDeckOfTransportationCounters;
        panel.setBounds(width*1210/1440, height*290/900, width*70/1440, height*65/900);
        panel.setOpaque(false);
        //panel.setBorder(whiteLine);
        boardGame_Layers.add(panel, 0);
    }

    public void initializeFaceUpTravelCards()
    {
        Logger.getGlobal().info("Initializing face up travel cards");
        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
        JPanel panel2 = new JPanel();
        panel2.setBounds(width*1315/1440, height*290/900, width*130/1440, height/5);
        panel2.setOpaque(false);
        //panel2.setBorder(whiteLine);
        panelForFaceUpTravelCards[0] = panel2;
        boardGame_Layers.add(panel2, 0);

        JPanel panel3 = new JPanel();
        panel3.setBounds(width*1210/1440, height*400/900, width*130/1440, height/5);
        panel3.setOpaque(false);
        //panel3.setBorder(whiteLine);
        panelForFaceUpTravelCards[1] = panel3;
        boardGame_Layers.add(panel3, 0);

        JPanel panel4 = new JPanel();
        panel4.setBounds(width*1315/1440, height*400/900, width*130/1440, height/5);
        panel4.setOpaque(false);
        //panel4.setBorder(whiteLine);
        panelForFaceUpTravelCards[2] = panel4;
        boardGame_Layers.add(panel4, 0);
    }

    public void initializeTransportationCountersAndObstacle()
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

        // Obstacle
        panelForObstacle.setOpaque(false);
        //panelForObstacle.setBorder(whiteLine);
        panelForObstacle.setBounds(width*900/1440, height*625/900, width*70/1440, height*65/900);
        boardGame_Layers.add(panelForObstacle,0);
    }

    @Override
    public void addImages()
    {
        backgroundPanel_ForMap.add(mapImage_BottomLayer);
        backgroundPanel_ForInformationCard.add(informationCardImage_TopLayer);
        panelForDeckOfTransportationCounters.add(GameState.instance().getCounterPile().getImage());

        drawTownPieces();
        drawGoldValueTokens();
        notifyObservers();
    }

    @Override
    public void addPanelToScreen()
    {
        boardGame_Layers.add(backgroundPanel_ForRound, 0);
        boardGame_Layers.add(panelForDeckOfTransportationCounters,0);
        boardGame_Layers.add(backgroundPanel_ForTransportationCounters, -1);
        boardGame_Layers.add(backgroundPanel_ForMap, -1);
        boardGame_Layers.add(backgroundPanel_ForObstacle,-1);
        boardGame_Layers.add(backgroundPanel_ForCards, -1);
        boardGame_Layers.add(backgroundPanel_ForInformationCard, -1);
        boardGame_Layers.add(backgroundPanel_ForDeckOfTransportationCounters, -1);
        boardGame_Layers.add(backgroundPanel_ForLeaderboard,-1);

        for (Town town: gameMap.getTownList()) {
            boardGame_Layers.add(town.getPanel(), 0);
            boardGame_Layers.add(town.getElfBootPanel(), 0);
        }
    }

    public void updateFaceUpTravelCards()
    {
        ArrayList<CardUnit> faceUpCards = GameState.instance().getFaceUpCards();

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
            panel.add(card.getDisplay());
            panel.repaint();
            panel.revalidate();
        }
    }

    public void updateTransportationCountersAndObstacle()
    {
        // remove a counter if it was already there
        for (JPanel panel : panelForPlayerTransportationCounters) {
            if (panel != null) {
                panel.removeAll();
                panel.repaint();
                panel.revalidate();
            }
        }

        List<TransportationCounter> counters = GameManager.getInstance().getThisPlayer().getHand().getCounters();

        // draw the counters to the screen
        for (int c = 0; c < counters.size(); c++) {
            JPanel panel = panelForPlayerTransportationCounters[c];
            TransportationCounter counter = counters.get(c);
            panel.add(counter.getDisplay());
            panel.repaint();
            panel.revalidate();
        }

        // Obstacle
        Obstacle o = GameManager.getInstance().getThisPlayer().getHand().getObstacle();

        if (o != null) {
            panelForObstacle.add(o.getDisplay());
        } else {
            panelForObstacle.removeAll();
        }

        panelForObstacle.repaint();
        panelForObstacle.revalidate();
    }

    private void drawGoldValueTokens() {
        // put gold value token on every town
        for (Town t : GameMap.getInstance().getTownList()) {
//            System.out.println(t.getName());
            if (t.getTokenPanel() != null && !t.getName().equals("Elvenhold")) {
                t.getTokenPanel().drawGoldValueToken();
            }
        }
    }
}
