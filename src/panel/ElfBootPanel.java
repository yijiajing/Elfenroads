package panel;

import domain.ElfBoot;
import domain.Town;
import domain.TownPiece;
import networking.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ElfBootPanel extends JPanel implements ObserverPanel {

    private Town town;
    private int x;
    private int y;
    private int width;
    private int height;
    private GameScreen gameScreen;
    private ArrayList<ElfBoot> bootsOnPanel;

    public ElfBootPanel(Town pTown, int x, int y, int pWidth, int pHeight, GameScreen pGameScreen) {
        this.town = pTown;
        this.x = x;
        this.y = y;
        this.width = pWidth;
        this.height = pHeight;
        this.gameScreen = pGameScreen;
        this.bootsOnPanel = new ArrayList<>();

        this.setBounds(this.x, this.y, this.width, this.height);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionManager.getInstance().setSelectedTown(ElfBootPanel.this.town);
            }
        });
//        this.addMouseListener(new ElfBootController(gameScreen, this));

        gameScreen.addObserverPanel(this);
    }

    /**
     * Adds the boot to the panel and removes the town piece with that colour if it exists
     * @param boot
     */
    public void addBootToPanel(ElfBoot boot) {
        this.bootsOnPanel.add(boot);

        ArrayList<TownPiece> pieces = this.town.getTownPieces();

        for (TownPiece p : pieces) {
            if (p.getColour().equals(boot.getColour())) {
                this.town.removeTownPieceByColour(p.getColour());
                return;
            }
        }
    }

    public void removeBootFromPanel(ElfBoot boot) {
        this.bootsOnPanel.remove(boot);
    }

    public void resetPanel() {
        removeAll();
        repaint();
        revalidate();
    }


    @Override
    public void updateView() {
        resetPanel();

        for (ElfBoot b : bootsOnPanel) {
            add(b.getImage());
        }

        repaint();
        revalidate();
    }

    public Town getTown() {
        return this.town;
    }

    /**
     * takes 2 panels and determines if they are equivalent
     * we will use this to sort of "translate" MoveBootCommand as it is sent across the network
     * called match() instead of equals() since equals() is not a static method and I didn't want to create any confusion
     * @param p1
     * @param p2
     * @return we return true iff the two panels correspond to towns with the same name
     */
    public static boolean match (ElfBootPanel p1, ElfBootPanel p2)
    {
        String p1TownName = p1.getTown().getName();
        String p2TownName = p2.getTown().getName();

        if (p1TownName.equals(p2TownName))
        {
            return true;
        }

        else return false;

    }

    // TODO: should we return a shallow copy instead of passing reference?
    public ArrayList<ElfBoot> getBootsOnPanel() {
        return bootsOnPanel;
    }
}
