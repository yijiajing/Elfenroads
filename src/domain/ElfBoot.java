package domain;

import networking.GameState;
import panel.ElfBootController;
import panel.ElfBootPanel;
import panel.GameScreen;

import javax.swing.*;
import java.awt.*;

public class ElfBoot extends JLabel {

    private String filepathToRepo = ".";
    private String colour;
    private int width;
    private int height;
    private boolean selected = false;
    private ElfBootPanel curPanel;
    private JLabel bootImage;
    private GameScreen gameScreen;

    public ElfBoot(String pColour, int pWidth, int pHeight, ElfBootPanel pCurPanel, GameScreen pGameScreen) {

        this.colour = pColour;
        this.width = pWidth;
        this.height = pHeight;
        this.curPanel = pCurPanel;
        this.curPanel.addBootToPanel(this);
        this.gameScreen = pGameScreen;

        ImageIcon bootIcon = new ImageIcon(filepathToRepo + "/assets/boppels-and-boots/boot-" + this.colour + ".png");
        Image bootImage = bootIcon.getImage();
        Image bootResized = bootImage.getScaledInstance(width*15/1440, height*15/900,  java.awt.Image.SCALE_SMOOTH);
        this.bootImage = new JLabel(new ImageIcon(bootResized));

        this.bootImage.addMouseListener(new ElfBootController(pGameScreen, this));
    }

    public boolean isSelected() { return this.selected; }

    public void setSelected(boolean pSelected) {
        this.selected = pSelected;
    }

    public ElfBootPanel getCurPanel() { return this.curPanel; }

    public void setCurPanel(ElfBootPanel pCurPanel) {
        this.curPanel.removeBootFromPanel(this);
        this.curPanel = pCurPanel;
        this.curPanel.addBootToPanel(this);

        // if there is a town piece at the new location of the elf boot, remove it and update player's score
        this.curPanel.getTown().removeTownPieceByColour(this.colour);
        GameState.instance(gameScreen).getPlayerByColour(this.colour).setCurrentTown(curPanel.getTown());

        GameScreen.getInstance(new JFrame()).notifyObservers();
    }

    public JLabel getImage() { return this.bootImage; }
}