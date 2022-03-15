package panel;

import domain.GoldValueToken;
import domain.Town;
import loginwindow.MainFrame;

import javax.swing.*;

import static utils.GameRuleUtils.isElfengoldVariant;

public class GoldValueTokenPanel extends JPanel {

    private Town town;
    private GoldValueToken token;

    public GoldValueTokenPanel(Town pTown, GoldValueToken pToken, int x, int y) {
        this.town = pTown;
        this.token = pToken;

        this.setBounds(x, y, this.token.getWidth(), this.token.getHeight());
        this.setOpaque(false);
    }

    public void drawGoldValueToken() {
        if (isElfengoldVariant()) {
            add(token.getImage());
            repaint();
            revalidate();
        }
    }
}
