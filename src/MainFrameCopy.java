import org.minueto.MinuetoTool;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class MainFrameCopy extends JFrame {

    static CardLayout cardLayout;
    static JPanel mainPanel;
    StartWindow start;
    LoginWindow login;
    LobbyWindow lobby;
    LobbyWindow lobbyAfterBack;
    VersionToPlayWindow version;
    LoadGameWindow load;
    GameScreen gameScreen;


    MainFrameCopy() throws IOException, ClassNotFoundException, InterruptedException {

        setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new StartWindow(), "start");

        // TODO: update this when testing on the other computer
        boolean isOurTurn = true;

        // while (true)
         // {
            GameScreen ourScreen = new GameScreen(this, isOurTurn);

            mainPanel.add(ourScreen, "gameScreen");

            add(mainPanel);
            setVisible(true);

            cardLayout.show(mainPanel, "start");

            // TimeUnit.SECONDS.sleep(20);

            // this code here is gonna be dumb

            // here, let's listen for a response from the socketserver

            while (!isOurTurn)
            {
                // while it's not our turn, listen for info about the other player's turn

                ServerSocket listening = new ServerSocket(4444);
                Socket inbound = listening.accept();
                ObjectInputStream in = new ObjectInputStream(inbound.getInputStream());
                GameScreen newScreen = (GameScreen) in.readObject();

                // it is now our turn, so we need to change the value of that field
                newScreen.reverseTurn();

                // close the sockets
                listening.close();
                inbound.close();


                // we now have the new screen info
                // let's set our game screen to this and then update it
                // we need to use it to reinitialize the game screen

                mainPanel.remove(ourScreen);
                mainPanel.add(newScreen, "gameScreen");

                for (Component toUpdate : mainPanel.getComponents())
                {
                    toUpdate.repaint();
                    toUpdate.revalidate();
                }

                for (Component toUpdate : newScreen.getComponents())
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
        MainFrameCopy mainFrame = new MainFrameCopy();
    }
}
