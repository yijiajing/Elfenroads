package panel;

import domain.ElfBoot;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ElfBootPanel extends JPanel implements ObserverPanel {

    private TownPanel townPanel;
    private int x;
    private int y;
    private int width;
    private int height;
    private JPanel[] spots;
    private boolean[] spotsAvailability;
    private GameScreen gameScreen;
    private ElfBoot[] bootsOnPanel;

    public ElfBootPanel(TownPanel pTownPanel, int x, int y, int pWidth, int pHeight, GameScreen pGameScreen) {
        this.townPanel = pTownPanel;
        this.x = x;
        this.y = y;
        this.width = pWidth;
        this.height = pHeight;
        this.spots = new JPanel[6];
        this.gameScreen = pGameScreen;
        this.bootsOnPanel = new ElfBoot[6];

        this.setBounds(this.x, this.y, this.width, this.height);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        initializeSpotsOnPanel();

        this.addMouseListener(new ElfBootController(gameScreen, this));

        gameScreen.addObserverPanel(this);
    }

    private void initializeSpotsOnPanel() {

        // set 6 spots for the elf boots
        int spotHeight = this.height;
        int spotWidth = this.width / 6;

        for (int i=0; i<spots.length; i++) {
            spots[i] = new JPanel();
        }

        // setting bounds of the six spots - 2 x 3 rectangle
        for (int s=0; s<spots.length; s++) {
            this.spots[s].setBounds(x + spotWidth * s, y, spotWidth, spotHeight);
            this.spots[s].setOpaque(false);
            }

        // set all spot availabilities to true (they are empty)
        spotsAvailability = new boolean[6];
        Arrays.fill(spotsAvailability, true);
    }

    public JPanel fillFirstAvailableSpot(ElfBoot boot) {
        JPanel availSpot = null;

        for (int i=0; i<spotsAvailability.length; i++) {
            if (spotsAvailability[i]) {
                availSpot = spots[i];
                spotsAvailability[i] = false;
                addBootToPanel(boot, i);
                return availSpot;
            }
        }

        return availSpot; // should never be null, there are 6 spots for 6 boots
    }

    public JPanel getSpotByNumber(int spot) {
        return spots[spot];
    }

    public void setSpotAvailability(int spot, boolean availability) {
        this.spotsAvailability[spot] = availability;
    }

    public void setSpotAvailability(JPanel spot, boolean availability) {
        for (int s=0; s<spots.length; s++) {
            if (spots[s].equals(spot)) {
                this.spotsAvailability[s] = availability;
            }
        }
    }

    private void addBootToPanel(ElfBoot boot, int index) {
        this.bootsOnPanel[index] = boot;
    }

    public void removeBootFromPanel(ElfBoot boot) {
        for (int i=0; i<bootsOnPanel.length; i++) {
            if (bootsOnPanel[i] != null && bootsOnPanel[i].equals(boot)) {
                bootsOnPanel[i] = null;
                setSpotAvailability(i, true);
            }
        }
    }

    public void resetPanel() {
        for ( JPanel spot : spots ) {
            spot.removeAll();
            spot.repaint();
            spot.revalidate();
        }
    }


    @Override
    public void updateView() {
        resetPanel();

        for (int i=0; i<spots.length; i++) {
            JPanel spot = spots[i];

            if (bootsOnPanel[i] != null) {
                spot.add(bootsOnPanel[i].getImage());
                spot.repaint();
                spot.revalidate();
            }
        }
    }
}
