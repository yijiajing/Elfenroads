package test;

import utils.NetworkUtils;

import java.io.*;
import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public class TestNgrokAddr {


    public static boolean wasAProblem = false;

    public static void main (String [] args) throws Exception

    {
        String removeOldFile = "rm Users/nicktriantos/Desktop/ngrokLogs.txt";
        // Process proc2 = Runtime.getRuntime().exec(removeOldFile);
        System.out.println("Removing the old file...");

        // String createFile = "touch ~/Desktop/f2021-hexanome-12/out/ngrokLogs.txt";
        String createFile = "touch /Users/nicktriantos/Desktop/ngrokLogs.txt";
        Process proc = Runtime.getRuntime().exec(createFile);
        System.out.println("Creating a new file to write to...");

        // File file = new File ("~/Desktop/f2021-hexanome-12/out/ngrokLogs.txt");
        File file = new File ("/Users/nicktriantos/Desktop/ngrokLogs.txt");
        PrintStream print;

        try {
            FileOutputStream write = new FileOutputStream(file);
            print = new PrintStream(write);

        }

        catch (FileNotFoundException e)
        {
            FileOutputStream write = new FileOutputStream(file);
            print = new PrintStream(write);
        }

        System.setOut(print);




        String prevAddr = NetworkUtils.ngrokAddrToPassToLS();
        LocalTime now = LocalTime.now();
        LocalTime lastError = LocalTime.now();

        for (int i = 0; i < 10000; i++)
        {
            // System.out.println("Checking...");
            String thisAddr = NetworkUtils.ngrokAddrToPassToLS();
            // System.out.println("Address according to ngrokAddrToPassToLS(): " + thisAddr);
            // System.out.println("Address according to getServerInfo(): " + NetworkUtils.getServerInfo());

            if (!thisAddr.equals(prevAddr))
            {
                System.out.println("Uh oh! The address changed!");
                wasAProblem = true;
                now = LocalTime.now();
                long diff = lastError.until(now, SECONDS);
                System.out.println("We got a different address for the first time in " + diff + " seconds.");
                lastError = LocalTime.now();
            }
            prevAddr = thisAddr;
            Thread.sleep(1000);
        }

        System.out.println("Finished the loop.");

        if (wasAProblem)
        {
            System.out.println("The ngrok address changed at some point. Uh oh!");
        }
        else
        {
            System.out.println("All good.");
        }




    }

}
