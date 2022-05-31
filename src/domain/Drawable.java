package domain;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.*;

public abstract class Drawable {
    String imageFilepath;
    ImageIcon icon;
    JLabel display; // the GUI element that actually shows the Drawable

    protected Drawable(String pImageFilepath) {
        imageFilepath = pImageFilepath;
        icon = new ImageIcon(imageFilepath);
    }

    public JLabel getDisplay() {
        return display;
    }

    public String getImageFilePath() {
        return this.imageFilepath;
    }
}
