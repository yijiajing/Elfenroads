package networking;

import loginwindow.*;
import org.minueto.MinuetoTool;
import panel.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkDemoPlayer1 extends JFrame {

    public static CardLayout cardLayout;
    public static JPanel mainPanel;
    StartWindow start;
    LoginWindow login;
    LobbyWindow lobby;
    LobbyWindow lobbyAfterBack;
    VersionToPlayWindow version;
    LoadGameWindow load;
    GameScreen gameScreen;


    NetworkDemoPlayer1() throws IOException, ClassNotFoundException, InterruptedException {

        setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new StartWindow(), "start");

        // TODO: update this when testing on the other computer
        boolean isOurTurn = true;

        // while (true)
         // {
            GameScreen ourScreen = GameScreen.init(this);

            mainPanel.add(ourScreen, "gameScreen");

            add(mainPanel);
            setVisible(true);

            cardLayout.show(mainPanel, "start");

            // TimeUnit.SECONDS.sleep(20);

            // this code here is gonna be dumb

            // here, let's listen for a response from the ServerSocket

            while (!isOurTurn)
            {
                // while it's not our turn, listen for info about the other player's turn

                ServerSocket listening = new ServerSocket(4444);
                Socket inbound = listening.accept();
                ObjectInputStream in = new ObjectInputStream(inbound.getInputStream());
                JPanel newSpot = (JPanel) in.readObject();

                // it is now our turn, so we need to change the value of that field

                // close the sockets
                listening.close();
                inbound.close();


                // we now have the new screen info
                // let's set our game screen to this and then update it
                // we need to use it to reinitialize the game screen

                ourScreen.moveBlackBoot(newSpot);
                // mainPanel.add(newScreen, "gameScreen");

                for (Component toUpdate : mainPanel.getComponents())
                {
                    toUpdate.repaint();
                    toUpdate.revalidate();
                }

                for (Component toUpdate : ourScreen.getComponents())
                {
                    toUpdate.repaint();
                    toUpdate.revalidate();
                }

                mainPanel.repaint();
                mainPanel.revalidate();

                isOurTurn = true;
            }




        // }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
        NetworkDemoPlayer1 mainFrame = new NetworkDemoPlayer1();


    }
}
