package test;

import java.io.*;
import java.net.*;
import java.util.Date;

import networking.PlayerClient;
import networking.PlayerServer;

public class testP2P 
{
    public static void main(String[] args)
    {
        try 
        {
            PlayerClient client = new PlayerClient();
            client.startConnection("127.0.0.1", 6666);
            client.sendMessage("allo\n");

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
    }
}
