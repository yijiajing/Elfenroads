package domain;

import javax.swing.*;
import java.awt.*;

public abstract class CounterUnit extends Drawable{

    String filepathToRepo = ".";
    String imageFilepath;
    ImageIcon image;
    JLabel display;

    CounterUnit(int resizeWidth, int resizeHeight, int imageNumber) {
        // find the picture of the card based on what type it is
        // since the images are named similarly and ordered the same way as they are in the enum declaration,
        // we can get the filepath just by using the type.
        this.imageFilepath = ("./assets/sprites/M0" + imageNumber + ".png");
        this.image = new ImageIcon (this.imageFilepath);
        Image toResize = this.image.getImage();
        Image resized = toResize.getScaledInstance(resizeWidth, resizeHeight,  java.awt.Image.SCALE_SMOOTH);
        this.image = new ImageIcon(resized);

        // add JLabel
        this.display = new JLabel(this.image);
    }

    public ImageIcon getImage() {return this.image;}

    public String getImageFilepath() {return this.imageFilepath;}

    public JLabel getDisplay() {return this.display;}
}
