package commands;

import domain.Player;
import enums.Colour;
import gamemanager.GameManager;
import networking.GameSession;
import networking.User;
import windows.ChooseBootWindow;
import windows.HostWaitWindow;
import windows.MainFrame;
import windows.PlayerWaitWindow;

import javax.swing.*;

// sent by the host after he receives a boot validation command
public class BootValidationResponseCommand implements GameCommand
{
    private boolean validated;
    private Colour colorChosen;

    public BootValidationResponseCommand(boolean pValidated, Colour pChosenColor)
    {
        validated = pValidated;
        colorChosen = pChosenColor;
    }

    @Override
    public void execute() {
        // TODO: implement
        // will be executed on the receiving machine
        // when the user receives his boot information, he can now set his boot.

        if (isValidated()) // if the boot choice was approved, the user can set his boot and continue
        {
            // this will happen on the choose boot window.
            // if the boot choice is validated, set the player color to that and continue
            String localPlayerName = User.getInstance().getUsername();
            GameManager.getInstance().setThisPlayer(new Player(colorChosen, localPlayerName));

            // the player is not the host of the session, so he should go to the playerWaitingRoom
            // if we previously had a PlayerWaitWindow (meaning we were in a game before and then left,) we need to completely reinitialize it
            if (MainFrame.getPlayerWait() != null)
            {
                PlayerWaitWindow prev = MainFrame.getPlayerWait();
                MainFrame.mainPanel.remove(prev);
            }
            // initialize a new PlayerWaitWindow
            PlayerWaitWindow updated = new PlayerWaitWindow(GameManager.getInstance().getSessionID());
            MainFrame.setPlayerWaitWindow(updated);
            MainFrame.mainPanel.add(updated, "playerWaitingRoom");
            MainFrame.cardLayout.show(MainFrame.mainPanel, "playerWaitingRoom");
        }

        else // if the boot choice was rejected, the user will get a pop-up and have to start over.
        {
            // we know now that this boot color is not available, so remove it before we re-display the choose boot window
            GameManager.getInstance().removeAvailableColour(colorChosen);
            // now, show a pop-up message letting the user know that his choice was rejected
            JOptionPane.showMessageDialog(null, "Too slow! That boot color is taken. Please try again.");
            // next, re-display the choose boot window with the updated choices
            ChooseBootWindow window = new ChooseBootWindow(GameManager.getInstance().getSessionID(), GameManager.getInstance().getAvailableColours());
            MainFrame.mainPanel.add(window, "choose-boot");
            MainFrame.cardLayout.show(MainFrame.mainPanel, "choose-boot");


        }
    }

    public boolean isValidated() {
        return validated;
    }
}
