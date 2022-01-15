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
            Socket test = new Socket("127.0.0.1", 6666);
            Socket test2 = new Socket("127.0.0.1", 6666);
            Socket test3 = new Socket("127.0.0.1", 6666);
            Socket test4 = new Socket("127.0.0.1", 6666);
            Socket test5 = new Socket("127.0.0.1", 6666);
            Socket test6 = new Socket("127.0.0.1", 6666);
            Socket test7 = new Socket("127.0.0.1", 6666);
            Socket test8 = new Socket("127.0.0.1", 6666);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
    }
}
