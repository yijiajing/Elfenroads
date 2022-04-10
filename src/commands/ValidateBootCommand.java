package commands;

// used during boot selection
// will be sent by a player to the host to check if a boot selection is valid
import enums.Colour;
import gamemanager.GameManager;
import networking.CommunicationsManager;
import windows.ChooseBootWindow;
import windows.MainFrame;

import java.io.IOException;
import java.util.logging.Logger;

public class ValidateBootCommand implements GameCommand {

    String senderName;
    Colour bootColorChoice;

    public ValidateBootCommand(String pSenderName, Colour pBootColorChoice)
    {
        senderName = pSenderName;
        bootColorChoice = pBootColorChoice;
    }

    @Override
    /**
     * will be executed on the host computer
     * will check if the boot is already taken
     * @pre the local user is the game creator/host
     */
    public void execute()
    {
        // the host will check to see if that color is available
        CommunicationsManager coms = CommunicationsManager.getINSTANCE();
        boolean validated = coms.checkIfColorAvailable(bootColorChoice);
        // if the boot was validated, consider it selected
        if (validated)
        {
            CommunicationsManager.recordColorChoice(bootColorChoice);
            GameManager.getInstance().removeAvailableColour(bootColorChoice);
            // remove the colour from this host's screen and reinitialize the window
            // next, re-display the choose boot window with the updated choices
            ChooseBootWindow window = new ChooseBootWindow(GameManager.getInstance().getSessionID(), GameManager.getInstance().getAvailableColours());
            MainFrame.mainPanel.add(window, "choose-boot");
            MainFrame.cardLayout.show(MainFrame.mainPanel, "choose-boot");

        }
        BootValidationResponseCommand cmd = new BootValidationResponseCommand(validated, bootColorChoice);
        try {coms.sendCommandToIndividual(cmd, senderName);}
        catch (IOException e)
        {
            Logger.getGlobal().info("Ran into an error responding to " + senderName + " 's boot validation request.");
        }
    }
}
