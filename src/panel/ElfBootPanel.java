package panel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;

public class ElfBootPanel extends JPanel {

    private TownPanel townPanel;
    private int x;
    private int y;
    private int width;
    private int height;
    private JPanel[] spots;
    private boolean[] spotsAvailability;


    public ElfBootPanel(TownPanel pTownPanel, int x, int y, int pWidth, int pHeight) {
        this.townPanel = pTownPanel;
        this.x = x;
        this.y = y;
        this.width = pWidth;
        this.height = pHeight;
        this.spots = new JPanel[6];

        this.setBounds(this.x, this.y, this.width, this.height);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        initializeSpotsOnPanel();
    }

    private void initializeSpotsOnPanel() {

        // set 6 spots for the elf boots
        int spotHeight = this.height / 2;
        int spotWidth = this.width / 3;

        for (int i=0; i<spots.length; i++) {
            spots[i] = new JPanel();
        }

        // setting bounds of the six spots - 2 x 3 rectangle
        for (int s=0; s<spots.length; s++) {
            if (s > 2) {
                this.spots[s].setBounds(x + spotWidth * (s%3), y + spotHeight, spotWidth, spotHeight);
            } else {
                this.spots[s].setBounds(x + spotWidth * s, y, spotWidth, spotHeight);
                    }

            this.spots[s].setOpaque(false);
            }

        // set all spot availabilities to true (they are empty)
        spotsAvailability = new boolean[6];
        Arrays.fill(spotsAvailability, true);
    }

    public JPanel fillFirstAvailableSpot() {
        JPanel availSpot = null;

        for (int i=0; i<spotsAvailability.length; i++) {
            if (spotsAvailability[i]) {
                availSpot = spots[i];
                spotsAvailability[i] = false;
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


}
