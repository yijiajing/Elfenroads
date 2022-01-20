package domain;

import javax.swing.*;
import java.awt.*;

public abstract class CardUnit {

    String imageFilepath;
    ImageIcon image;
    JLabel display;
    String filepathToRepo = ".";

    public CardUnit(int resizeWidth, int resizeHeight, String filename) {
        this.imageFilepath = (filepathToRepo + "/assets/sprites/" + filename + ".png");
        this.image = new ImageIcon(this.imageFilepath);
        Image toResize = this.image.getImage();
        Image resized = toResize.getScaledInstance(resizeWidth, resizeHeight,  java.awt.Image.SCALE_SMOOTH);
        this.image = new ImageIcon(resized);

        // add JLabel
        this.display = new JLabel(this.image);
    }

}
