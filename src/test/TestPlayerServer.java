package test;

import networking.PlayerServer;

public class TestPlayerServer 
{
    public static void main(String[] args)
    {
        PlayerServer server = new PlayerServer(2);
        server.setMessage("It worked from the Server!!\n" + "You are now connected to the server");
        server.start(6666);
    }
}
