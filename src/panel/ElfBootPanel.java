package panel;

import domain.ElfBoot;
import domain.Town;

import javax.swing.*;
import java.awt.*;
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

        this.addMouseListener(new ElfBootController(gameScreen, this));

        gameScreen.addObserverPanel(this);
    }

    public void addBootToPanel(ElfBoot boot) {
        this.bootsOnPanel.add(boot);
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
}
