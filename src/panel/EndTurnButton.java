package panel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import commands.PassTurnCommand;
import enums.EGRoundPhaseType;
import enums.ELRoundPhaseType;
import gamemanager.GameManager;
import gamescreen.GameScreen;
import windows.MP3Player;
import networking.GameState;
import utils.GameRuleUtils;

public class EndTurnButton extends JButton {
    MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");

    public EndTurnButton() {
        setText("EndTurn");
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!GameManager.getInstance().isLocalPlayerTurn()) { // do nothing if it's not the local player's turn
                    GameScreen.displayMessage("You cannot end someone else's turn!");
                } else if (GameRuleUtils.isDrawCountersPhase()) {
                    GameScreen.displayMessage("You must click on a counter to draw.");
                } else if (GameState.instance().getCurrentPhase() == ELRoundPhaseType.RETURN_COUNTERS ||
                        GameState.instance().getCurrentPhase() == EGRoundPhaseType.RETURN_COUNTERS) {
                    GameScreen.displayMessage("You must click on a transportation counter to return.");
                } else if (GameState.instance().getCurrentPhase() == null) {
                    GameScreen.displayMessage("The game has ended. Please exit the game.");
                } else if (GameState.instance().getCurrentPhase() == ELRoundPhaseType.PLAN_ROUTES ||
                        GameState.instance().getCurrentPhase() == EGRoundPhaseType.PLAN_ROUTES) {
                    PassTurnCommand command = new PassTurnCommand();
                    command.execute();
                    try {
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(command);
                    } catch (IOException e) {
                        System.out.println("There was a problem sending the PassTurnCommand to all players.");
                        e.printStackTrace();
                    }
                    GameManager.getInstance().endTurn();
                    track1.play();
                } else {
                    GameManager.getInstance().endTurn();
                    track1.play();
                }
            }
        });
    }
}
