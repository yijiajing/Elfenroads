package panel;

import domain.GoldValueToken;
import domain.Town;

import javax.swing.*;

import java.awt.*;

import static utils.GameRuleUtils.isElfengoldVariant;

public class GoldValueTokenPanel extends JPanel {

    private Town town;
    private GoldValueToken token;

    public GoldValueTokenPanel(Town pTown, GoldValueToken pToken, int x, int y) {
        this.town = pTown;
        this.token = pToken;

        this.setBounds(x, y, this.token.getWidth()+20, this.token.getHeight()+20);
        this.setOpaque(false);
        this.setVisible(true);
    }

    public void drawGoldValueToken() {
        add(token.getImage());
        repaint();
        revalidate();
    }
}
