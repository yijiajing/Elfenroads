package commands;

import domain.GameManager;
import domain.Player;
import enums.Colour;
import networking.CommunicationsManager;
import utils.NetworkUtils;

public class SendPlayerInfoCommand implements GameCommand {

    // this type of command will be used to get information about players
    // each computer will send this command to every other player once during GameManager.launch()

    String playerName; // the player we want information about, identified by his LS username
    String senderName; // the person sending this command (so that the receiver knows who to send his response to)

    public SendPlayerInfoCommand (String playerNameWeWant)
    {
        playerName = playerNameWeWant;
        try {senderName = NetworkUtils.getLocalIP();}
        catch (Exception e) {e.printStackTrace();}
    }


    public void execute()
    {
        // the receiving computer can just send his player object back to the computer who requested it
        Player thisPlayer = GameManager.getInstance().getThisPlayer();
        // send a new storePlayerCommand back to the original requester
        String playerName = thisPlayer.getName();
        Colour playerColor = thisPlayer.getColour();
        AddPlayerCommand cmd = new AddPlayerCommand(playerName, playerColor);

        // send the command back to the original requester of the Player info
        try{GameManager.getInstance().getComs().sendCommandToIndividual(cmd, playerName);}
        catch (Exception e2){e2.printStackTrace();}

    }


}
