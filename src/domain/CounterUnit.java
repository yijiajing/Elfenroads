package domain;

import javax.swing.*;

import commands.DrawCounterCommand;
import enums.RoundPhaseType;
import loginwindow.MP3Player;
import loginwindow.MainFrame;
import networking.ActionManager;
import networking.GameState;
import panel.GameScreen;
import utils.GameRuleUtils;
import enums.CounterType;
import enums.CounterUnitType;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

public abstract class CounterUnit extends Drawable{

    private Road placedOn;
    boolean owned;
    boolean isSecret;
    boolean aSelected;
    JLabel miniDisplay; // for when the counter is on the map
    JLabel superMiniDisplay;// for obstacle on the map
    private CounterUnitType aType;
    private MP3Player track1 = new MP3Player("./assets/Music/0000171.mp3");
    
    
    CounterUnit(int resizeWidth, int resizeHeight, int imageNumber) {

        // find the picture of the card based on what type it is
        // since the images are named similarly and ordered the same way as they are in the enum declaration,
        // we can get the filepath just by using the type
        super("./assets/sprites/M0" + imageNumber + ".png");
        owned = false; // default value
        isSecret = false;
        // String filepath = ("./assets/sprites/M0" + imageNumber + ".png");
        Image toResize = icon.getImage();
        Image resized = toResize.getScaledInstance(resizeWidth, resizeHeight,  java.awt.Image.SCALE_SMOOTH);
        Image resized_mini = toResize.getScaledInstance(resizeWidth/2, resizeHeight/2,  java.awt.Image.SCALE_SMOOTH);
        Image resized_supermini = toResize.getScaledInstance(resizeWidth/3, resizeHeight/3,  java.awt.Image.SCALE_SMOOTH);
        display = new JLabel(new ImageIcon(resized));
        miniDisplay = new JLabel(new ImageIcon(resized_mini));
        superMiniDisplay = new JLabel (new ImageIcon(resized_supermini));
    }

    public Road getPlacedOn() {
        return placedOn;
    }

    public void setPlacedOn(Road placedOn) {
        this.placedOn = placedOn;
    }

    public boolean isOwned() {
        return this.owned;
    }

    public void setOwned(boolean b) {
        this.owned = b;
    }
    
    public boolean isSecret() {
    	return this.isSecret;
    }
    
    public void setSecret(boolean b) {
    	this.isSecret = b;
    }

    public JLabel getMiniDisplay() {
        return this.miniDisplay;
    }
    
    public JLabel getSuperMiniDisplay() {
    	return this.superMiniDisplay;
    }
    
    public boolean isSelected() {
    	return this.aSelected;
    }
    
    public void setSelected(boolean pSelected) {
        if (pSelected && !this.aSelected) {
            // this counter is selected
            display.setBorder(BorderFactory.createLineBorder(Color.yellow));
        } else if (!pSelected && this.aSelected) {
            // this counter is deselected
            display.setBorder(BorderFactory.createEmptyBorder());
        }
    	aSelected = pSelected;
    }
    
    protected void initializeMouseListener() {
        this.getDisplay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!GameManager.getInstance().isLocalPlayerTurn()) {
                    return;
                }

                // DRAW COUNTERS PHASE, counter is face-up and available to be chosen
                if (!isOwned() && GameRuleUtils.isDrawCountersPhase()) {
                    // adding the counter to my hand
                    track1.play();
                    GameState.instance().getFaceUpCounters().remove(CounterUnit.this); // remove the counter from the face-up pile
                    GameManager.getInstance().getThisPlayer().getHand().addUnit(CounterUnit.this);
                    GameState.instance().addFaceUpCounterFromPile(); // replenish the face-up counters with one from the pile
                    GameScreen.getInstance().updateAll(); // update GUI
                    CounterUnit.this.owned = true;
                    CounterUnit.this.setSecret(false);
                    Logger.getGlobal().info("Just added " + CounterUnit.this.getType() +
                            ", current counters in hand: " +
                            GameManager.getInstance().getThisPlayer().getHand().getCounters().toString());

                    // tell the other peers to remove the counter
                    try {
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(
                                new DrawCounterCommand(CounterUnit.this, true));
                    } catch (IOException err) {
                        System.out.println("Error: there was a problem sending the DrawCounterCommand to the other peers.");
                    }

                    GameManager.getInstance().endTurn();

                }
                // RETURN COUNTERS PHASE
                else if (GameState.instance().getCurrentPhase() == RoundPhaseType.RETURN_COUNTERS) {
                    GameManager.getInstance().returnAllCountersExceptOne(CounterUnit.this);
                    track1.play();
                }

                // PLAN TRAVEL ROUTES PHASE
                else if (GameRuleUtils.isPlanRoutesPhase()) {
                    if (getPlacedOn() == null) {
                        ActionManager.getInstance().setSelectedCounter(CounterUnit.this);
                    } else {
                        // If the counter is placed on a road, then the user's intention is to click on the road
                        ActionManager.getInstance().setSelectedRoad(getPlacedOn());
                    }
                    track1.play();
                }
            }
        });
    }
    public CounterUnitType getType() {
    	return aType;
    }
    
    //This method should be hidden by all subclasses.
    public static CounterUnit getNew(CounterUnitType pType) {
    	return null;
    }
}
