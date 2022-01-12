package domain;

import networking.*;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Player {

    // this represents a player in a game
    // there may be some overlap between this and the networking.User class, but I thought it would be a good idea to keep them separated for now

    // basic info
    private String name;
    private String bootColor; // TODO: write enum for colors instead
    private int score; // we might want to do this somewhere else instead, but for now I will store score in here


    // networking stuff
    private User associated;
    private String IP;

    /**
     * constructs a player from a User object (designed to be called after login)
     * @param loggedIn the LS account the player is currently signed into
     */
    public Player (User loggedIn)
    {
        associated = loggedIn;
        name = loggedIn.getUsername();
    }

    public String getName() {return name;}

    public String getBootColor() {return bootColor;}

    public int getScore() {return score;}

    public User getAssociated() {return associated;}

    public String getIP() {return IP;}
}
