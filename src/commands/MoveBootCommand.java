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
    private String start;
    private String destination;
    private Colour colorBootMoved;

    public MoveBootCommand (ElfBootPanel pStart, ElfBootPanel pDestination, ElfBoot pMoved)
    {
        // start = pStart;
        // destination = pDestination;
        colorBootMoved = pMoved.getColour();
        start = pStart.getTown().getName();
        destination = pDestination.getTown().getName();
    }

    public void execute(GameManager manager)
    {
        GameMap map = GameScreen.getInstance().getGameMap();
        GameState state = manager.getGameState();
        // ElfBoot moved = state.getBootByColour(colorBootMoved);
        // TODO: uncomment that line and remove the below one. just using this one for testing
        ElfBoot moved = state.getElfBoots().get(0);
        if (moved.equals(null))
        {
            System.out.println("Execute method failed. Could not find a boot in the list of boots.");
        }


        Town startTown = map.getTown(start);
        Town destinationTown = map.getTown(destination);

        ElfBootPanel startPanel = startTown.getElfBootPanel();
        ElfBootPanel destinationPanel = destinationTown.getElfBootPanel();

        startPanel.removeBootFromPanel(moved);
        destinationPanel.addBootToPanel(moved);

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
