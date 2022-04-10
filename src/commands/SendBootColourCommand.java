package commands;

import gamemanager.GameManager;
import enums.Colour;

/**
 * A command to send our boot colour to another player
 */

public class SendBootColourCommand implements GameCommand {

    private Colour bootColour;
    public SendBootColourCommand(Colour pBootColour) {
        this.bootColour = pBootColour;
    }

    /**
     * We have received the boot colour of someone else
     */
    @Override
    public void execute() {

        // if the response bootColour is null, then we can do nothing
        if (bootColour == null)
        {
            // do nothing
            return;
        }
        // else remove the color
        else
        {
            GameManager.getInstance().removeAvailableColour(bootColour);
        }
    }


}
