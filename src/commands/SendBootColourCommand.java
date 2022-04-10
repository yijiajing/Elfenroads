package commands;

import gamemanager.GameManager;
import enums.Colour;
import networking.GameSession;
import windows.ChooseBootWindow;
import windows.MainFrame;

import java.io.IOException;
import java.util.logging.Logger;

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

        GameManager.getInstance().receivedBootNotif(); // increments the counter
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
        // if we have gotten all of the responses we need, we can show the boot window.

        try
        {
            if (GameManager.getInstance().getBootNotifsReceived() == GameSession.getNumPlayers(GameManager.getInstance().getSessionID()) - 1)
            {
                ChooseBootWindow window = new ChooseBootWindow(GameManager.getInstance().getSessionID(), GameManager.getInstance().getAvailableColours());
                MainFrame.mainPanel.add(window, "choose-boot");
                MainFrame.cardLayout.show(MainFrame.mainPanel, "choose-boot");
            }
        }
        catch (IOException e)
        {
            Logger.getGlobal().severe("There was a problem getting session information for session " + GameManager.getInstance().getSessionID() + " from the API.");
            e.printStackTrace();
        }

    }


}
