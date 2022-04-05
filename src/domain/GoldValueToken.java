package domain;

import javax.swing.*;
import java.awt.*;

public class GoldValueToken {

    private int value;
    private int width;
    private int height;
    private JLabel image;

    public GoldValueToken(int pValue, int pWidth, int pHeight) {
        assert pValue > 0;
        this.value = pValue;
        this.width = pWidth;
        this.height = pHeight;

        ImageIcon icon = new ImageIcon("./assets/sprites/" + this.value + ".png");
        Image resized = icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        this.image = new JLabel(new ImageIcon(resized));
    }

    public JLabel getImage() {
        return image;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getValue() {
        return value;
    }
}
