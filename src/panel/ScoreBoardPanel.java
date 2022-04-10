package panel;

import domain.CounterUnit;
import domain.Player;
import gamemanager.GameManager;
import gamescreen.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

//This panel represent the score of one single player. 

public class ScoreBoardPanel extends JPanel implements ObserverPanel {

    private GameScreen aScreen;
    private Player aPlayer;
    private JLabel score;
    private JLabel gold;
    private JLabel destination;
    private JPanel scoreCard;
    private JPanel countersCard;


    public ScoreBoardPanel(GameScreen pScreen, Player pPlayer, boolean includeGold) {
        aScreen = pScreen;
        aPlayer = pPlayer;

        this.setPreferredSize(new Dimension(aScreen.getWidth() * 290 / 1440, aScreen.getHeight() * 40 / 900));
        this.setBorder(BorderFactory.createLineBorder(aPlayer.getColor(), 5));

        final CardLayout ObjCl = new CardLayout();
        this.setLayout(ObjCl);

        //card1: the score card
        scoreCard = new JPanel();
        scoreCard.setLayout(new FlowLayout());
        scoreCard.add(new JLabel(pPlayer.getName()));

        score = new JLabel(Integer.toString(aPlayer.getScore()) + " pts");
        scoreCard.add(score);
        scoreCard.add(new JLabel(aPlayer.getColour().toString()));

        if (pPlayer.getDestinationTown() != null && aPlayer.equals(GameManager.getInstance().getThisPlayer())) {
            destination = new JLabel(aPlayer.getDestinationTown().getName());
            scoreCard.add(destination);
        }

        if (includeGold) {
            gold = new JLabel(aPlayer.getGoldCoins() + " gold coins");
            scoreCard.add(gold);
        }

        //Card2: the card showing counters owned by other players
        countersCard = new JPanel();
        countersCard.setLayout(new FlowLayout());
        List<CounterUnit> counters = pPlayer.getHand().getCounters();
        for (CounterUnit c : counters) {
            //display a black square if c is secret.
            if (c.isSecret()) {
                JLabel display = new JLabel();
                display.setForeground(Color.BLACK);
                display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
                display.setPreferredSize(new Dimension(aScreen.getWidth() * 20 / 1440, aScreen.getHeight() * 20 / 900));//TODO: adjust size

                countersCard.add(display);
            }
            //else if c is not secret, display it.
            else {
                countersCard.add(new JLabel(c.getType().toString()));
            }
        }

        //Then add the two cards to the scoreBoardPanel
        this.add(scoreCard);
        this.add(countersCard);

        //Switch to the counters card when hovering on this panel
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ObjCl.last(ScoreBoardPanel.this);//the last card is the counters card
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ObjCl.first(ScoreBoardPanel.this);//the first card is the score card
            }
        });

        aScreen.addObserverPanel(this);
    }


    @Override
    public void updateView() {
        scoreCard.remove(score);
        score = new JLabel(Integer.toString(aPlayer.getScore()) + " pts");
        scoreCard.add(score);
        this.revalidate();
        this.repaint();

    }
}