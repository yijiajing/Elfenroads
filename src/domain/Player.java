package domain;

import enums.Colour;
import networking.*;
import panel.GameScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player implements Comparable<Player> {

    // this represents a player in a game
    // there may be some overlap between this and the networking.User class, but I thought it would be a good idea to keep them separated for now

    // game-specific info
    private Town curTown;
    private Set<Town> townsVisited = new HashSet<>();

    private Colour colour;
    private int score;
    private String name;
    
    private Hand hand; //The Hand of this Player, including hand of CardUnit and hand of CounterUnit

    public Player(Colour pColour, String pName)
    {
        this.curTown = GameMap.getInstance().getTownByName("Elvenhold");
        this.colour = pColour;
        this.hand = new Hand();
        this.name = pName;
    }

    public String getCurrentTownName() {
        return curTown.getName();
    }

    // called by ElfBoot when it moves to a new town
    public void setCurrentTown(Town curTown) {
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

    public Colour getColour() {
        return this.colour;
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

    // need to be able to sort a list of players so that everyone has the same list
    @Override
    public int compareTo(Player o) {
        return name.compareTo(o.getName());
    }
}
