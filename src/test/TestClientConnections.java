package test;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.stream.Collectors;

import networking.PlayerClient;
import networking.PlayerClientHandler;
import networking.PlayerServer;

public class TestClientConnections 
{  
    public static void main(String[] args)
    {
        //PlayerClient client = new PlayerClient();
        //client.startConnection("127.0.0.1", 6666);
        //System.out.println(client.getMessage());
        
        
        PlayerClient client2 = new PlayerClient();
        client2.startConnection("0.tcp.ngrok.io", 19868);
        System.out.println(client2.getMessage());
    }
}
