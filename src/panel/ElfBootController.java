package panel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import domain.ElfBoot;
import networking.*;

/**
 * Controller for ElfBoot movement.
 * Reacts to mouse events on ElfBoots, TownPanels, and ElfBootPanels by updating the model
 */
public class ElfBootController implements MouseListener {

    private GameScreen gameScreen;
    private JComponent itemClicked;

    public ElfBootController(GameScreen pGameScreen, JComponent pItemClicked) {
        gameScreen = pGameScreen;
        itemClicked = pItemClicked;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (itemClicked instanceof ElfBoot) {
            ((ElfBoot) itemClicked).setSelected(true);
        }
        else {
            moveBoot();
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

    public void moveBoot() {
        GameState state = gameScreen.getGameState();
        ArrayList<ElfBoot> elfBoots = state.getElfBoots();

        for ( ElfBoot boot : elfBoots ) {
            if (boot.isSelected()) {

                // update model with new spot of boot
                if (itemClicked instanceof ElfBootPanel) {
                    boot.setCurPanelAndSpot((ElfBootPanel) itemClicked);
                } else if (itemClicked instanceof TownPanel) {
                    ElfBootPanel elfBootPanel = ((TownPanel) itemClicked).getElfBootPanel();
                    boot.setCurPanelAndSpot(elfBootPanel);
                }

                // boot has been successfully moved and is no longer selected
                boot.setSelected(false);

                return;
            }
        }
    }

    public void update(JPanel panel)
    {
        panel.repaint();
        panel.revalidate();
    }
}
