package commands;

import domain.Player;
import enums.Colour;
import gamemanager.GameManager;
import networking.GameSession;
import networking.User;
import windows.HostWaitWindow;
import windows.MainFrame;
import windows.PlayerWaitWindow;

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

            // go to the next screen
            // take the player to either the host or player waiting window, depending on whether they are the host of the session
            if (GameSession.isCreator(User.getInstance(), GameManager.getInstance().getSessionID()))) // the player is the host of the session
            {
                MainFrame.mainPanel.add(new HostWaitWindow(GameManager.getInstance().getSessionID()), "hostWaitingRoom");
                MainFrame.cardLayout.show(MainFrame.mainPanel, "hostWaitingRoom");
            }
            // the player is not the host of the session, so he should go to the playerWaitingRoom
            else
            {
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
        }
        else // if the boot choice was rejected, the user will get a pop-up and have to start over.
        {
            // we know now that this boot color is not available, so remove it before we re-display the choose boot window
            GameManager.getInstance().removeAvailableColour(colorChosen);
            // now, show a pop-up message letting the user know that his choice was rejected
            // next, re-display the choose boot window with the updated choices

        }
    }

    public boolean isValidated() {
        return validated;
    }
}
