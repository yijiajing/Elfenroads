import java.net.Socket;
import java.util.ArrayList;

public class Player {

    // this represents a player in a game
    // there may be some overlap between this and the User class, but I thought it would be a good idea to keep them separated for now

    private String location; // will represent the player's IP
    private ArrayList<Player> connectedPlayers;
    private ArrayList<Socket> in; // connections in
    private ArrayList<Socket> out; // connections out

    // TODO: implement the constructor
    public Player()
    {

    }

    // TODO: implement this once I finish up with P2Pconnection
    public void addConnection(P2PConnection conWithPlayer)
    {

    }




}
