package networking;
// copied from http://github.com/DoctorLai and edited slightly

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DNSLookup {
    // https://helloacm.com/the-dns-lookup-tool-in-java-inetaddress/
    public static String getAdd(String dns) {
        try {
            InetAddress host;

            // byte[] byteArray = dns.getBytes();

            host = InetAddress.getByName(dns);

            InetAddress[] addr = InetAddress.getAllByName(dns);
            System.out.println(addr[0]);

            System.out.println("The host is: " + host);


            displayHost(host);

            return host.getHostAddress();

        } catch (UnknownHostException e) {
            System.out.println("The DNS lookup failed.");
            e.printStackTrace();
        }

        return null;
    }

    private static void displayHost(InetAddress host) {
        System.out.println("Host:'" + host.getHostName()
                + "' has address: " + host.getHostAddress());
    }


}
