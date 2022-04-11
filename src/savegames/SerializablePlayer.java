package savegames;

import com.sun.jdi.connect.Transport;
import domain.*;
import enums.Colour;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SerializablePlayer implements Serializable, Comparable<Player> {

    private String name;
    private Colour color;

    private String currentTownName;
    private ArrayList<String> visitedTownNames;
    private String destinationTownName;

    private int goldCoins;
    private int score;

    private ArrayList<SerializableCardUnit> cards;
    private ArrayList<SerializableCounterUnit> counters; // in Elfengold, the obstacles will be in here
    private SerializableObstacle obstacle; // could be null (optional is not serializable)

    public SerializablePlayer (Player original)
    {
        name = original.getName();
        // will turn a Player object into a serializable version which can be saved to a file
        currentTownName = original.getCurrentTownName();
        saveVisitedTownNames(original);

        if (original.getDestinationTown() != null) // we are saving a destination town game
        {
            destinationTownName = original.getDestinationTown().getName();
        }

        goldCoins = original.getGoldCoins(); // TODO: make sure this doesn't give us a nullpointer for Elfenland versions


        color = original.getColour();
        score = original.getScore();

        // add the counters, cards, and obstacles
        saveCounters(original);
        saveCards(original);
        saveObstacle(original);

    }

    private void saveVisitedTownNames (Player original)
    {
        Set<Town> visited = original.getTownsVisited();
        ArrayList<String> visitedNames = new ArrayList<String>();
        visitedTownNames = new ArrayList<>();

        for (Town cur : visited)
        {
            visitedTownNames.add(cur.getName());
        }
    }

    private void saveCounters(Player original)
    {
        List<CounterUnit> origCounters = original.getHand().getCounters();
        counters = new ArrayList<>();

        for (CounterUnit cur : origCounters)
        {
            if (cur instanceof TransportationCounter)
            {
                TransportationCounter curDowncasted = (TransportationCounter) cur;
                counters.add(new SerializableTransportationCounter(curDowncasted));
            }
            else if (cur instanceof GoldPiece) {
                GoldPiece curDowncasted = (GoldPiece) cur;
                counters.add(new SerializableGoldPiece(curDowncasted));
            }
            else if (cur instanceof MagicSpell) {
                MagicSpell curDowncasted = (MagicSpell) cur;
                counters.add(new SerializableMagicSpell(curDowncasted));
            }

            else // for Elfengold, cur could also be an obstacle
            {
                Obstacle curDowncasted = (Obstacle) cur;
                counters.add(new SerializableObstacle(curDowncasted));
            }
        }

    }

    private void saveObstacle(Player original)
    {
        // for Elfenland, check if the player has an obstacle

        if (original.hasObstacle())
        {
            obstacle = new SerializableObstacle(original.getHand().getObstacle());
        }

    }

    private void saveCards(Player original)
    {
        List <CardUnit> origCards = original.getHand().getCards();
        cards = new ArrayList<>();

        for (CardUnit cur : origCards)
        {
            if (cur instanceof TravelCard)
            {
                TravelCard curDowncasted = (TravelCard) cur;
                cards.add(new SerializableTravelCard(curDowncasted));
            }
            else // cur is a gold card
            {
                GoldCard curDowncasted = (GoldCard) cur;
                cards.add(new SerializableGoldCard(curDowncasted));
            }

            // do we do anything with gold cards?
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

    public int getGoldCoins() {
        return goldCoins;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Player o) {
        return name.compareTo(o.getName());
    }
}
