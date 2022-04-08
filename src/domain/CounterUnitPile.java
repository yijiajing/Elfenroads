package domain;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import commands.DrawCounterCommand;
import enums.*;
import gamemanager.GameManager;
import windows.MainFrame;
import networking.GameState;
import gamescreen.GameScreen;
import utils.GameRuleUtils;

/**
 * A face-down pile of Counter Units
 * For Elfenland variant, this pile only contains Transportation Counters
 * For Elfengold variant, the pile contains additional Counters, Obstacles, Magic Spells, and Gold Pieces
 */

public class CounterUnitPile extends Deck<CounterUnit> {

    private JLabel deckImage;
    private GameVariant variant;

    public CounterUnitPile(String sessionID, GameVariant variant) {

        super(sessionID);

        this.variant = variant;
        addCountersToDeck();

        // initialize the deck image
        ImageIcon imageIcon = new ImageIcon("./assets/sprites/counter_deck.png");
        Image image = imageIcon.getImage();
        Image imageResized = image.getScaledInstance(GameScreen.getInstance().getWidth() * 70 / 1440, GameScreen.getInstance().getHeight() * 70 / 900, java.awt.Image.SCALE_SMOOTH);
        this.deckImage = new JLabel(new ImageIcon(imageResized));

        initializeMouseListener();

        shuffle();
    }
    
    private void addCountersToDeck() {
    	int width = MainFrame.instance.getWidth() * 67 / 1440;
    	int height = MainFrame.instance.getHeight() * 60 / 900;


        if (GameRuleUtils.isElfengoldVariant(this.variant)) {
            for(int i = 0; i < 9; i++) {
                if (i < 2) {
                    components.add(new GoldPiece(GoldPieceType.GOLDPIECE, width, height));
                    components.add(new Obstacle(ObstacleType.EGTREE, width, height));
                    components.add(new Obstacle(ObstacleType.SEAMONSTER, width, height));
                    components.add(new MagicSpell(MagicSpellType.DOUBLE, width, height));
                    components.add(new MagicSpell(MagicSpellType.EXCHANGE, width, height));
                }
                if (i < 4) {
                    components.add(new TransportationCounter(CounterType.DRAGON, width, height));
                    components.add(new TransportationCounter(CounterType.MAGICCLOUD, width, height));
                }
                if(i < 5) {
                    components.add(new TransportationCounter(CounterType.UNICORN, width,height));
                }
                if(i < 8) {
                    components.add(new TransportationCounter(CounterType.ELFCYCLE, width,height));
                    components.add(new TransportationCounter(CounterType.TROLLWAGON, width,height));
                }
                components.add(new TransportationCounter(CounterType.GIANTPIG, width,height));
            }
        } else {
        	for (CounterType type : CounterType.values()) {
                for (int i = 0; i < 8; i++) {
                    components.add(new TransportationCounter(type, width, height));
                }
            }
        }
    }
    
    public JLabel getImage() {
        return this.deckImage;
    }

    private void initializeMouseListener() {
        this.deckImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GameRuleUtils.isDrawCountersPhase() && GameManager.getInstance().isLocalPlayerTurn()) {
                    CounterUnit drawn = GameState.instance().getCounterPile().draw(); // draw a counter
                    GameManager.getInstance().getThisPlayer().getHand().addUnit(drawn); // add to player's hand
                    drawn.setOwned(true);
                    GameScreen.getInstance().updateAll(); // update GUI

                    // tell the other peers to remove the counter from the pile
                    try {
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(new DrawCounterCommand(drawn, false));
                    } catch (IOException err) {
                        System.out.println("Error: there was a problem sending the DrawCounterCommand to the other peers.");
                    }

                    GameManager.getInstance().endTurn();
                }
            }
        });
    }
}