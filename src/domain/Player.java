package domain;

import networking.*;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {

    // this represents a player in a game
    // there may be some overlap between this and the networking.User class, but I thought it would be a good idea to keep them separated for now

    private InetAddress location; // will be used to connect a player
    private String locationAsString; // will represent the player's IP
    private ArrayList<Player> connectedPlayers;
    private ArrayList<Socket> in; // connections in
    private ArrayList<Socket> out; // connections out

    // game-specific info
    private String currentTownName;
    private Set<String> townsVisited = new HashSet<>();
    
    private int aScore;//The score of a player 

    // TODO: implement the constructor
    public Player(String IP)
    {
        this.currentTownName = "Elvenhold";
    }


    // TODO: implement this once I finish up with P2Pconnection
    public void addConnection(P2PConnection conWithPlayer)
    {

    }

    public String getCurrentTownName() {
        return currentTownName;
    }

    public void setCurrentTown(String currentTownName) {
        this.currentTownName = currentTownName;
        if (!townsVisited.contains(currentTownName)) {
            townsVisited.add(currentTownName);
            aScore++;
        }
        //TODO: move elfboot, update UI
    }

    public int getScore() {
    	return aScore;
    }

}
