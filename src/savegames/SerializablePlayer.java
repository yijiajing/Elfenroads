package savegames;

import domain.*;
import enums.Colour;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SerializablePlayer implements Serializable {

    private String name;

    private String currentTownName;
    private ArrayList<String> visitedTownNames;
    private String destinationTownName;

    private Colour color;

    private ArrayList<SerializableCardUnit> cards;
    private ArrayList<SerializableCounterUnit> counters; // in Elfengold, the obstacles will be in here
    private SerializableObstacle obstacle; // could be null (optional is not serializable)

    public SerializablePlayer (Player original)
    {
        name = original.getName();
        // will turn a Player object into a serializable version which can be saved to a file
        currentTownName = original.getCurrentTownName();
        visitedTownNames = getVisitedTownNames(original);
        destinationTownName = original.getDestinationTown().getName();
        color = original.getColour();

        // add the counters, cards, and obstacles
        counters = getCounters(original);
        obstacle = getObstacle(original); // could be null

    }

    private static ArrayList<String> getVisitedTownNames (Player original)
    {
        Set<Town> visited = original.getTownsVisited();
        ArrayList<String> visitedNames = new ArrayList<String>();

        for (Town cur : visited)
        {
            visitedNames.add(cur.getName());
        }

        return visitedNames;
    }

    private void addCounters(Player original)
    {
        List<CounterUnit> origCounters = original.getHand().getCounters();
        ArrayList<SerializableCounterUnit> out = new ArrayList<SerializableCounterUnit>();

        for (CounterUnit cur : origCounters)
        {
            counters.add(new SerializableCounterUnit(cur));
        }

    }

    private void getObstacle(Player original)
    {
        // for Elfenland, check if the player has an obstacle

        if (original.hasObstacle())
        {
            obstacle = new SerializableObstacle(original.getHand().getObstacle());
        }

    }

    private void addCards(Player original)
    {
        List <CardUnit> origCards = original.getHand().getCards();
        List <SerializableCardUnit> out = new ArrayList<SerializableCardUnit>();

        for (CardUnit cur : origCards)
        {
            cards.add(new SerializableCardUnit(cur));
        }
    }

    public String getCurrentTownName() {
        return currentTownName;
    }

    public ArrayList<String> getVisitedTownNames() {
        return visitedTownNames;
    }

    public String getDestinationTownName() {
        return destinationTownName;
    }

    public Colour getColor() {
        return color;
    }

    public ArrayList<SerializableCardUnit> getCards() {
        return cards;
    }

    public ArrayList<SerializableCounterUnit> getCounters() {
        return counters;
    }

    public SerializableObstacle getObstacle() {
        return obstacle;
    }

    public String getName() {
        return name;
    }
}
