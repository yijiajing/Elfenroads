package domain;

import javax.swing.*;
import java.awt.*;

public abstract class CounterUnit extends Drawable{

    private JLabel image;
    private Road placedOn;

    CounterUnit(int resizeWidth, int resizeHeight, int imageNumber) {

        // find the picture of the card based on what type it is
        // since the images are named similarly and ordered the same way as they are in the enum declaration,
        // we can get the filepath just by using the type
        String filepath = ("./assets/sprites/M0" + imageNumber + ".png");
        ImageIcon imageIcon = new ImageIcon(filepath);
        Image toResize = imageIcon.getImage();
        Image resized = toResize.getScaledInstance(resizeWidth, resizeHeight,  java.awt.Image.SCALE_SMOOTH);
        this.image = new JLabel(new ImageIcon(resized));
    }

    public Road getPlacedOn() {
        return placedOn;
    }

    public void setPlacedOn(Road placedOn) {
        this.placedOn = placedOn;
    }

    public JLabel getImage() {return this.image;}
}
