package commands;

import gamemanager.GameManager;
import enums.Colour;
import networking.CommunicationsManager;
import utils.NetworkUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * A command to request the boot colours of the other players
 */

public class GetBootColourCommand implements GameCommand {

    private String senderIP;

    public GetBootColourCommand(String senderIP) {
        this.senderIP = senderIP;
    }

    /***
     * The sender has requested our boot colour, so we send it back to them
     */
    @Override
    public void execute() {
        CommunicationsManager coms = GameManager.getInstance().getComs();
        Logger.getGlobal().info("Someone requested our boot color.");

        try {
            String localAddress = CommunicationsManager.getLocalAddress();

            // if we have not picked our color yet, we can tell the asker that
            if (GameManager.getInstance().getThisPlayer() == null)
            {
                // we haven't chosen a boot yet
                SendBootColourCommand response = new SendBootColourCommand(null);
                coms.sendGameCommandToPlayer(response, senderIP);
                Logger.getGlobal().info("Telling " + senderIP + " that we have not selected a boot color yet.");
                return;
            }

            Colour myColour = GameManager.getInstance().getThisPlayer().getColour();
            coms.sendGameCommandToPlayer(new SendBootColourCommand(myColour), senderIP);
            Logger.getGlobal().info("Telling " + senderIP + " that we have not selected a boot color yet.");
        } catch (IOException e) {
            System.out.println("There was a problem sending the SendBootColourCommand.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("There was a problem retrieving the local IP.");
            e.printStackTrace();
        }
    }
}
