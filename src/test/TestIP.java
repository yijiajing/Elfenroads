package test;

import utils.NetworkUtils;

import java.net.InetAddress;

public class TestIP {


    public static void main (String [] args) throws Exception
    {

        System.out.println(NetworkUtils.getLocalIP());


        /* InetAddress add = InetAddress.getLocalHost();
        InetAddress[] all = InetAddress.getAllByName(add.getHostName());




        for (InetAddress cur : all)
        {
            if (NetworkUtils.isValidIP(cur.getHostAddress()) && !cur.isLoopbackAddress())
            {
                System.out.println(cur.getHostAddress());
                System.out.println(cur.getHostAddress().startsWith("10"));
            }
        }
        
         */


    }

}
