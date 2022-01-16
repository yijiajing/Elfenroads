package test;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.stream.Collectors;

import networking.PlayerClient;
import networking.PlayerServer;

public class testP2P 
{
    private static PrintWriter out;
    private static BufferedReader in;
    
    public static void main(String[] args)
    {
        try 
        {
            Socket test = new Socket("127.0.0.1", 6666);
            out = new PrintWriter(test.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(test.getInputStream()));
            
            String input = in.lines().collect(Collectors.joining("\n"));
            
            System.out.println(input);
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }  
    }
}
