package commands;

import gamemanager.GameManager;
import enums.Colour;

/**
 * A command to send our boot colour to another player
 */

public class SendBootColourCommand implements GameCommand {

    private Colour bootColour;
    private String senderIP;

    public SendBootColourCommand(Colour pBootColour, String senderIP) {
        this.bootColour = pBootColour;
        this.senderIP = senderIP;
    }

    /**
     * We have received the boot colour of someone else
     */
    @Override
    public void execute() {
        GameManager.getInstance().removeAvailableColour(bootColour, senderIP);
    }


}
