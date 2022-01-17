package test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import networking.PlayerServer;

public class TestPlayerServer 
{
    public static void main(String[] args) throws UnknownHostException
    {
        
        PlayerServer server = new PlayerServer(1);
        server.setMessage("It worked from the Server!! " + "You are now connected to the server");
        server.start(6666);
    }
}
