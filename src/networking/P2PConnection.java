package networking;

import domain.Player;

import java.net.Socket;

public class P2PConnection {

    // this class represents a peer-to-peer connection between two players (two computers)
    // it represents the connection both ways
    // we will have to update this later, but for now we only need the two

    private Player player1;
    private Player player2;

    private Socket player1ToPlayer2;
    private Socket player2ToPlayer1;

    public P2PConnection (Player player1, Player player2)
    {
        // TODO: provide arguments to the Socket constructor
        player1ToPlayer2 = new Socket();
        player2ToPlayer1 = new Socket();

        player1.addConnection(this);
        player2.addConnection(this);

    }

    public Socket getPlayer1ToPlayer2() {
        return player1ToPlayer2;
    }

    public Socket getPlayer2ToPlayer1() {
        return player2ToPlayer1;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
