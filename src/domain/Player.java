package domain;

import networking.*;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Player {

    // this represents a player in a game
    // there may be some overlap between this and the networking.User class, but I thought it would be a good idea to keep them separated for now

    private InetAddress location; // will be used to connect a player
    private String locationAsString; // will represent the player's IP
    private ArrayList<Player> connectedPlayers;
    private ArrayList<Socket> in; // connections in
    private ArrayList<Socket> out; // connections out

    // TODO: implement the constructor
    public Player(String IP)
    {

    }


    // TODO: implement this once I finish up with P2Pconnection
    public void addConnection(P2PConnection conWithPlayer)
    {

    }




}
