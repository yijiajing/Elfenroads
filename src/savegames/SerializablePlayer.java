package savegames;

import com.sun.jdi.connect.Transport;
import domain.*;
import enums.Colour;
import gamemanager.GameManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

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

        if (original.equals(GameManager.getInstance().getThisPlayer()))
        {
            saveCounters(original);
            saveCards(original);
            saveObstacle(original);
        }

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
                SerializableTransportationCounter toAdd = new SerializableTransportationCounter(curDowncasted);
                counters.add(toAdd);
                Logger.getGlobal().info("Saving a " + toAdd.getType());
            }
            else if (cur instanceof GoldPiece) {
                GoldPiece curDowncasted = (GoldPiece) cur;
                SerializableGoldPiece toAdd = new SerializableGoldPiece(curDowncasted);
                counters.add(toAdd);
                Logger.getGlobal().info("Saving a " + toAdd.getType());
            }
            else if (cur instanceof MagicSpell) {
                MagicSpell curDowncasted = (MagicSpell) cur;
                SerializableMagicSpell toAdd = new SerializableMagicSpell(curDowncasted);
                counters.add(toAdd);
                Logger.getGlobal().info("Saving a " + toAdd.getType());
            }

            else // for Elfengold, cur could also be an obstacle
            {
                Obstacle curDowncasted = (Obstacle) cur;
                SerializableObstacle toAdd = new SerializableObstacle(curDowncasted);
                counters.add(toAdd);
                Logger.getGlobal().info("Saving a " + toAdd.getType());
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
                SerializableTravelCard toAdd = new SerializableTravelCard(curDowncasted);
                cards.add(toAdd);
                Logger.getGlobal().info("Saving a " + toAdd.getType());
            }
            else // cur is a gold card
            {
                GoldCard curDowncasted = (GoldCard) cur;
                SerializableGoldCard toAdd = new SerializableGoldCard(curDowncasted);
                cards.add(toAdd);
                Logger.getGlobal().info("Saving a gold card");
            }

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
