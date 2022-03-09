package commands;

import domain.*;
import enums.Colour;
import networking.GameState;
import panel.ElfBootPanel;
import panel.GameScreen;

import java.io.Serializable;
import java.util.logging.Logger;

public class MoveBootCommand implements GameCommand, Serializable {

    // use town names instead of panel objects because panel stuff is not serializable over the network (and neither is a town, since it contains Panel stuff)
    // Elf Boot can also not be serialized since it contains GUI elements
    private final String start;
    private final String destination;
    private final Colour colorBootMoved;

    public MoveBootCommand (ElfBootPanel pStart, ElfBootPanel pDestination, ElfBoot pMoved)
    {
        colorBootMoved = pMoved.getColour();
        start = pStart.getTown().getName();
        destination = pDestination.getTown().getName();
    }


    @Override
    /**
     * @pre the move has been validated by the ActionManager
     */
    public void execute()
    {
        Logger.getGlobal().info("Executing MoveBootCommand, start: " + start + ", dest: " + destination + ", color: " + colorBootMoved);
        GameState gameState = GameState.instance();
        GameMap map = GameMap.getInstance();
        Town startTown = map.getTown(start);
        Town destinationTown = map.getTown(destination);
        ElfBootPanel startPanel = startTown.getElfBootPanel();
        ElfBootPanel destinationPanel = destinationTown.getElfBootPanel();
        Player sender = gameState.getPlayerByColour(colorBootMoved);

        // update the current town of the player who moved
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
        GameScreen.getInstance().updateLeaderboard();
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
