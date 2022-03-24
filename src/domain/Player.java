package domain;

import enums.Colour;
import networking.*;

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

    public Player(Colour pColour, String pName)
    {
        this.curTown = GameMap.getInstance().getTownByName("Elvenhold");
        townsVisited.add(GameMap.getInstance().getTownByName("Elvenhold"));
        this.colour = pColour;
        this.hand = new Hand();
        this.name = pName;
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


    
    public String getName() {
        return name;
    }

    public Town getDestinationTown() {
        return destinationTown;
    }

    public void setDestinationTown(Town destinationTown) {
        this.destinationTown = destinationTown;
    }

    // need to be able to sort a list of players so that everyone has the same list
    @Override
    public int compareTo(Player o) {
        return name.compareTo(o.getName());
    }

    public String toString() {
        return name + " " + colour + " " + curTown.getName();
    }
}
