package Test;

import utils.NetworkUtils;

import java.net.InetAddress;
import java.util.logging.Logger;

public class TestIP {


    public static void main (String [] args) throws Exception
    {

        InetAddress local = InetAddress.getLocalHost();
        String localHostname = local.getHostName();
        InetAddress [] allAddresses = InetAddress.getAllByName(localHostname);

        for (InetAddress address : allAddresses)
        {
            Logger.getGlobal().info(address.getHostAddress());
        }



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
