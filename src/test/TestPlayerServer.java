package test;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import networking.PlayerServer;

public class TestPlayerServer 
{
    public static void main(String[] args) throws IOException
    {
        //String command = "./ngrok tcp 6666";
        //Process proc = Runtime.getRuntime().exec(command);

        System.out.println(PlayerServer.validateNgrok());
        System.out.println("No errors in the new method validateNgrok!");

        System.out.println(PlayerServer.getServerInfo() + " is the ngrok address.");
        String [] tokenizedAddr = PlayerServer.tokenizeNgrokAddr();
        for (String entry : tokenizedAddr)
        {
            System.out.println(entry);
        }






        // PlayerServer server = new PlayerServer(1);
        // server.setMessage("It worked from the Server!! " + "You are now connected");
        // server.start(6666);
        // System.out.println(server.getServerInfo());
    }
}
