package panel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import domain.ElfBoot;
import networking.*;

/**
 * Controller for ElfBoot movement.
 * Reacts to mouse events on TownPanels and ElfBootPanels and moves the selected ElfBoot accordingly. 
 */
public class ElfBootController implements MouseListener {

    private GameScreen gameScreen;
    private JPanel panelClicked;

    public ElfBootController(GameScreen pGameScreen, JPanel pPanelClicked) {
        gameScreen = pGameScreen;
        panelClicked = pPanelClicked;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        GameState state = gameScreen.getGameState();
        ArrayList<ElfBoot> elfBoots = state.getElfBoots();

        for ( ElfBoot boot : elfBoots ) {
            if (boot.isSelected()) {

                // remove boot from previous spot
                JPanel panelSpot = boot.getCurSpotInPanel();
                panelSpot.remove(boot.getImage());
                update(panelSpot);

                // move boot to new spot
                if (panelClicked instanceof ElfBootPanel) {
                    boot.setCurPanelAndSpot((ElfBootPanel) panelClicked);
                } else if (panelClicked instanceof TownPanel) {
                    ElfBootPanel elfBootPanel = ((TownPanel) panelClicked).getElfBootPanel();
                    boot.setCurPanelAndSpot(elfBootPanel);
                }

                JPanel newPanelSpot = boot.getCurSpotInPanel();
                newPanelSpot.add(boot.getImage());
                update(newPanelSpot);

                // boot has been successfully moved and is no longer selected
                boot.setSelected(false);

                return;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void update(JPanel panel)
    {
        panel.repaint();
        panel.revalidate();
    }
}
