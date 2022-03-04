package commands;

import domain.ElfBoot;
import domain.GameManager;
import domain.GameMap;
import domain.Town;
import enums.Colour;
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

    public void execute(GameManager game)
    {
        GameMap map = game.getScreen().getGameMap();
        GameState state = game.getGameState();
        ElfBoot moved = state.getBootByColour(colorBootMoved);

        Town startTown = map.getTown(start);
        Town destinationTown = map.getTown(destination);

        ElfBootPanel startPanel = startTown.getBootPanel();
        ElfBootPanel destinationPanel = destinationTown.getBootPanel();

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
