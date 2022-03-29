package networking;

import domain.*;
import enums.Colour;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SerializablePlayer implements Serializable {

    private String currentTownName;
    private ArrayList<String> visitedTownNames;
    private String destinationTownName;

    private Colour color;
    // TODO: write code for the special versions of counters and cards and have a list of each as a part of this class

    private ArrayList<SerializableCardUnit> cards;
    private ArrayList<SerializableCounterUnit> counters;
    private SerializableObstacle obstacle; // could be null (optional is not serializable)

    public SerializablePlayer (Player original)
    {
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

    private static ArrayList<SerializableCounterUnit> getCounters(Player original)
    {
        List<CounterUnit> counters = original.getHand().getCounters();
        ArrayList<SerializableCounterUnit> out = new ArrayList<SerializableCounterUnit>();

        for (CounterUnit cur : counters)
        {
            out.add(new SerializableCounterUnit(cur));
        }

        return out;
    }

    // TODO: update hand class and then fix this
    private static SerializableObstacle getObstacle(Player original)
    {
        // for Elfenland, check if the player has an obstacle

        if (original.hasObstacle())
        {
            return new SerializableObstacle(original.getHand().getObstacle());
        }

        else return null;
    }

    private static List<SerializableCardUnit> addCards(Player original)
    {
        List <CardUnit> cards = original.getHand().getCards();
        List <SerializableCardUnit> out = new ArrayList<SerializableCardUnit>();

        for (CardUnit cur : cards)
        {
            out.add(new SerializableCardUnit(cur));
        }

        return out;
    }




}
