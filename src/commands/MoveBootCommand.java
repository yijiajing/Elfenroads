package commands;

import domain.*;
import enums.Colour;
import networking.GameState;
import panel.ElfBootPanel;
import panel.GameScreen;

import java.io.Serializable;

public class MoveBootCommand implements GameCommand, Serializable {

    // use town names instead of panel objects because panel stuff is not serializable over the network (and neither is a town, since it contains Panel stuff)
    // Elf Boot can also not be serialized since it contains GUI elements
    private final String start;
    private final String destination;
    private final Colour colorBootMoved;
    private final String senderName;

    public MoveBootCommand (ElfBootPanel pStart, ElfBootPanel pDestination, ElfBoot pMoved, String senderName)
    {
        colorBootMoved = pMoved.getColour();
        start = pStart.getTown().getName();
        destination = pDestination.getTown().getName();
        this.senderName = senderName;
    }


    @Override
    /**
     * @pre the move has been validated by the ActionManager
     */
    public void execute()
    {
        GameState gameState = GameState.instance();
        GameMap map = GameMap.getInstance();
        Town startTown = map.getTown(start);
        Town destinationTown = map.getTown(destination);
        ElfBootPanel startPanel = startTown.getElfBootPanel();
        ElfBootPanel destinationPanel = destinationTown.getElfBootPanel();

        // update the current town of the player who moved
        Player sender = gameState.getPlayerByName(senderName);
//      1.  sender.setCurrentTownAndIncrementScore(destinationTown);

        // update current panel of the boot
        ElfBoot moved = GameState.instance().getBootByColour(colorBootMoved);
        moved.setCurPanel(destinationPanel); // this method already does the three things marked with numbers

//        // actually move the boot
//      2.  startPanel.removeBootFromPanel(moved);
//      3.  destinationPanel.addBootToPanel(moved); // removes the town piece

        startPanel.updateView();
        destinationPanel.updateView();
        GameScreen.getInstance().notifyObservers();
    }

    public String getStart() {
        return start;
    }

    public String getDestination() {
        return destination;
    }

    public Colour getColorBootMoved() {
        return colorBootMoved;
    }
}
