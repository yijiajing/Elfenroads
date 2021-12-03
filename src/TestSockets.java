import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TestSockets


// main computer version

{
    public static void main (String [] args) throws IOException

    {
        String otherComputerIP = "10.0.0.244";
        int port = 4444;
        ServerSocket server = new ServerSocket(port);
        Socket accepted = server.accept();


        BufferedReader inputReader = new BufferedReader(new InputStreamReader(accepted.getInputStream()));
        while ( inputReader.readLine() != null)
        {
            System.out.println("Current line of the input says...");
        }
    }

    }



    private static ServerSocket serverSide(int port) throws IOException

    {

        ServerSocket listener = new ServerSocket(port);
        Socket accepted = listener.accept();





    }