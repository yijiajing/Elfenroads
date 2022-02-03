package domain;

import javax.swing.*;
import java.awt.*;

public abstract class CardUnit extends Drawable{

    String filepathToRepo = ".";
    private JLabel image;

    public CardUnit(int resizeWidth, int resizeHeight, String filename) {

        ImageIcon imageIcon = new ImageIcon("./assets/sprites/" + filename + ".png");
        Image toResize = imageIcon.getImage();
        Image resized = toResize.getScaledInstance(resizeWidth, resizeHeight,  java.awt.Image.SCALE_SMOOTH);
        this.image = new JLabel(new ImageIcon(resized));

    }

    public JLabel getImage() {
        return this.image;
    }

}
