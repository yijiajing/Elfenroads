package panel;

import domain.Town;
import domain.TownPiece;
import loginwindow.MainFrame;
import networking.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class TownPanel extends JPanel implements ObserverPanel {

    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private GameScreen gameScreen;
    private ElfBootPanel elfBootPanel;
    private Town town;

    public TownPanel(String pName, int x, int y, int pWidth, int pHeight, GameScreen pGameScreen, Town pTown) {
        this.name = pName;
        this.x = x;
        this.y = y;
        this.width = pWidth;
        this.height = pHeight;
        this.gameScreen = pGameScreen;
        this.town = pTown;
        this.elfBootPanel = new ElfBootPanel(town, x-10, y+height, MainFrame.getInstance().getWidth()*13/144, MainFrame.getInstance().getHeight()*1/35, pGameScreen);

        this.setBounds(this.x, this.y, this.width, this.height);
        this.setOpaque(false);
        //this.setBorder(BorderFactory.createLineBorder(Color.WHITE));

//        this.addMouseListener(new ElfBootController(gameScreen, this));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionManager.getInstance().setSelectedTown(TownPanel.this.town);
            }
        });
        drawTownPieces();

        gameScreen.addObserverPanel(this);
    }

    public ElfBootPanel getElfBootPanel() { return this.elfBootPanel; }

    public void drawTownPieces() {
        ArrayList<TownPiece> pieces = town.getTownPieces();

        for ( TownPiece piece : pieces ) {
            add(piece.getImage());
        }

        repaint();
        revalidate();
    }

    public Town getTown() {
        return this.town;
    }

    public void resetPanel() {
        removeAll();
        repaint();
        revalidate();
    }

    @Override
    public void updateView() {
        resetPanel();
        drawTownPieces();
    }
}

