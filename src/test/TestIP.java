package test;

import utils.NetworkUtils;

import java.net.UnknownHostException;

public class TestIP {


    public static void main (String [] args) throws Exception
    {

        System.out.println(NetworkUtils.getLocalIP());

    }

}
