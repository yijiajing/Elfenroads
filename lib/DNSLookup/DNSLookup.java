
// TAKEN FROM http://github.com/DoctorLai/DNSLookup

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DNSLookup {
  // https://helloacm.com/the-dns-lookup-tool-in-java-inetaddress/
  public static void main(String args[]) {
    try {
      InetAddress host;
      if (args.length == 0) {
        host = InetAddress.getLocalHost();
        displayHost(host);
      } else {
        for (int i = 0; i < args.length; ++ i) {
          host = InetAddress.getByName(args[i]);
          displayHost(host);
        }
      }      
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  private static void displayHost(InetAddress host) {
    System.out.println("Host:'" + host.getHostName()
          + "' has address: " + host.getHostAddress());    
  }
}  