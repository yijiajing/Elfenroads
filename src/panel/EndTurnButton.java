package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import domain.GameManager;
import enums.RoundPhaseType;
import loginwindow.MP3Player;
import networking.GameState;
import utils.GameRuleUtils;

public class EndTurnButton extends JButton {
    MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");

    public EndTurnButton() {
        setText("EndTurn");
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!GameManager.getInstance().isLocalPlayerTurn()) { // do nothing if it's not the local player's turn
                    GameScreen.displayMessage("You cannot end someone else's turn!");
                } else if (GameRuleUtils.isDrawCountersPhase()) {
                    GameScreen.displayMessage("You must click on a counter to draw.");
                } else if (GameState.instance().getCurrentPhase() == RoundPhaseType.RETURN_COUNTERS) {
                    GameScreen.displayMessage("You must click on a transportation counter to return.");
                } else if (GameState.instance().getCurrentPhase() == null) {
                    GameScreen.displayMessage("The game has ended. Please exit the game.");
                } else {
                    GameManager.getInstance().endTurn();
                    track1.play();
                }
            }
        });
    }
}
