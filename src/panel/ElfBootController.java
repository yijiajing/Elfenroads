package panel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import commands.MoveBootCommand;
import domain.ElfBoot;
import domain.GameManager;
import enums.Colour;
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
        GameState state = GameManager.getInstance().getGameState();
        ArrayList<ElfBoot> elfBoots = state.getElfBoots();

        // gathering information to construct the MoveBootCommand to send over the network
        ElfBootPanel startForCommand = null; // need to intialize these null so the compiler won't complain
        ElfBootPanel destinationForCommand = null;
        ElfBoot bootForCommand = null;

        for ( ElfBoot boot : elfBoots ) {
            if (boot.isSelected()) {
                startForCommand = boot.getCurPanel();
                bootForCommand = boot;
                // update model with new spot of boot
                if (itemClicked instanceof ElfBootPanel) {
                    boot.setCurPanel((ElfBootPanel) itemClicked);
                    destinationForCommand = ((ElfBootPanel) itemClicked);
                } else if (itemClicked instanceof TownPanel) {
                    ElfBootPanel elfBootPanel = ((TownPanel) itemClicked).getElfBootPanel();
                    boot.setCurPanel(elfBootPanel);
                    destinationForCommand = ((TownPanel) itemClicked).getElfBootPanel();
                }

                // TODO: remove this. just for testing
                if (startForCommand.equals(null) || destinationForCommand.equals(null) || bootForCommand.equals(null))
                {
                    System.out.println("Something went wrong! The fields in the command to send were not determined correctly!");
                }

                // boot has been successfully moved and is no longer selected
                boot.setSelected(false);

                // now, construct a command and notify the CommunicationsManager so that it can send the movement to other players in the game
                MoveBootCommand toSendOverNetwork = new MoveBootCommand(startForCommand, destinationForCommand, bootForCommand);
                try {
                    GameManager.getInstance().getComs().sendGameCommandToAllPlayers(toSendOverNetwork);
                } catch (IOException e) {
                    System.out.println("There was a problem sending the command to move the boot!");
                    e.printStackTrace();
                }

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
