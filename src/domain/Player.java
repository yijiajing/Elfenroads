package domain;

import enums.Colour;
import loginwindow.MainFrame;
import networking.*;
import savegames.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.awt.*;

public class Player implements Comparable<Player> {

    // this represents a player in a game
    // there may be some overlap between this and the networking.User class, but I thought it would be a good idea to keep them separated for now

    // game-specific info
    private Town curTown;
    private Set<Town> townsVisited = new HashSet<>();

    private Colour colour;
    private int score;
    private String name;
    private Town destinationTown;
    
    private Hand hand; //The Hand of this Player, including hand of CardUnit and hand of CounterUnit

    private int goldCoins;

    public Player(Colour pColour, String pName)
    {
        this.curTown = GameMap.getInstance().getTownByName("Elvenhold");
        townsVisited.add(GameMap.getInstance().getTownByName("Elvenhold"));
        this.colour = pColour;
        this.hand = new Hand();
        this.name = pName;
    }

    /**
     * loads a player from a savegame SerializablePlayer object
     * @param loaded the player to load
     */
    public Player (SerializablePlayer loaded)
    {
        curTown = GameMap.getInstance().getTownByName(loaded.getCurrentTownName());
        destinationTown = GameMap.getInstance().getTownByName(loaded.getDestinationTownName());
        colour = loaded.getColor();
        name = loaded.getName();

        loadTownsVisited(loaded);
        loadHand(loaded);
    }

    public String getCurrentTownName() {
        return curTown.getName();
    }

    // called by ElfBoot when it moves to a new town
    public void setCurrentTownAndIncrementScore(Town curTown) {
        this.curTown = curTown;
        if (!townsVisited.contains(curTown)) {
            townsVisited.add(curTown);
            score++;
        }
    }

    public Town getCurrentTown() {
        return curTown;
    }

    public int getScore() {
    	return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Colour getColour() {
        return this.colour;
    }

    public java.awt.Color getColor(){
        java.awt.Color c = null;

        switch(colour){
            case BLACK : return Color.BLACK;
            case BLUE : return Color.BLUE;
            case RED : return Color.RED;
            case YELLOW : return Color.YELLOW;
            case GREEN : return Color.GREEN;
            case PURPLE : return Color.MAGENTA;
            default : return Color.WHITE;

        }
        //RED, YELLOW, BLUE, BLACK, GREEN, PURPLE
        //return null;
        
    }

    public ElfBoot getBoot() {
        for (ElfBoot boot: GameState.instance().getElfBoots()) {
            if (boot.getColour() == colour) {
                return boot;
            }
        }
        return null;
    }

    public Hand getHand() { return this.hand; }

    public boolean hasObstacle () {return this.hand.hasObstacle();}

    public String getName() {
        return name;
    }

    public Town getDestinationTown() {
        return destinationTown;
    }

    public void setDestinationTown(Town destinationTown) {
        this.destinationTown = destinationTown;
    }

    public int getGoldCoins() {
        return goldCoins;
    }

    public void addGoldCoins(int value) {
        goldCoins += value;
    }

    public void removeGoldCoins(int value) {
        goldCoins -= value;
    }

    // need to be able to sort a list of players so that everyone has the same list
    @Override
    public int compareTo(Player o) {
        return name.compareTo(o.getName());
    }

    public String toString() {
        return name + " " + colour + " " + curTown.getName();
    }

    public Set<Town> getTownsVisited() {
        return townsVisited;
    }

    // METHODS USED FOR READING IN A PLAYER FROM A SAVEGAME
    private void loadTownsVisited(SerializablePlayer loaded)
    {
        for (String townName : loaded.getVisitedTownNames())
        {
            townsVisited.add(GameMap.getInstance().getTownByName(townName));
        }
    }

    private void loadHand(SerializablePlayer loaded)
    {
        ArrayList<SerializableCounterUnit> counters = loaded.getCounters();
        ArrayList<SerializableCardUnit> cards = loaded.getCards();
        SerializableObstacle loadedObstacle = loaded.getObstacle();

        // load each part of the hand
        // load counters
        for (SerializableCounterUnit ctr : counters)
        {
            if (ctr instanceof SerializableObstacle)
            {
                SerializableObstacle counterDowncasted = (SerializableObstacle) ctr;
                hand.addUnit(new Obstacle(counterDowncasted));
            }

            else if (ctr instanceof SerializableMagicSpell)
            {
                SerializableMagicSpell counterDowncasted = (SerializableMagicSpell) ctr;
                hand.addUnit(new MagicSpell(counterDowncasted));
            }
            else // if ctr is a transportation counter
            {
                SerializableTransportationCounter counterDowncasted = (SerializableTransportationCounter) ctr;
                hand.addUnit(new TransportationCounter(counterDowncasted));
            }
        }

        // load cards
        // TODO: are there any CardUnits other than TravelCards in the player's hand?
        for (SerializableCardUnit crd : cards)
        {
            if (crd instanceof SerializableTravelCard)
            {
                SerializableTravelCard crdDowncasted = (SerializableTravelCard) crd;
                hand.addUnit(new TravelCard(crdDowncasted));
            }
        }

        // TODO: make sure this is the right check
        if (loadedObstacle != null)
        {
            hand.addUnit(new Obstacle(loadedObstacle));
        }

        // done loading in the player's hand


    }
}
