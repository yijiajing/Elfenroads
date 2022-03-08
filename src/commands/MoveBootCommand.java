package commands;

import domain.ElfBoot;
import domain.GameManager;
import domain.GameMap;
import domain.Town;
import enums.Colour;
import networking.ActionManager;
import networking.GameState;
import panel.ElfBootPanel;
import panel.GameScreen;
import panel.TownPanel;

import java.io.Serializable;

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
        // update the current town of the player who moved
        GameState.instance().getCurrentPlayer().setCurrentTown(ActionManager.getInstance().getSelectedTown());

        GameMap map = GameMap.getInstance();
        Town startTown = map.getTown(start);
        Town destinationTown = map.getTown(destination);

        // update current panel of the boot
        ElfBoot moved = GameState.instance().getBootByColour(colorBootMoved);
        moved.setCurPanel(GameMap.getInstance().getTownByName(destination).getElfBootPanel());

        ElfBootPanel startPanel = startTown.getElfBootPanel();
        ElfBootPanel destinationPanel = destinationTown.getElfBootPanel();

        // actually move the boot
        startPanel.removeBootFromPanel(moved);
        destinationPanel.addBootToPanel(moved); // removes the town piece

        startPanel.updateView();
        destinationPanel.updateView();
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
