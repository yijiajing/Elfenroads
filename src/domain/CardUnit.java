package domain;

import javax.swing.*;
import java.awt.*;

public abstract class CardUnit extends Drawable{

    public CardUnit(int resizeWidth, int resizeHeight, String filename) {

        super ("./assets/sprites/" + filename + ".png");
        Image toResize = icon.getImage();
        Image resized = toResize.getScaledInstance(resizeWidth, resizeHeight,  java.awt.Image.SCALE_SMOOTH);
        display = new JLabel(new ImageIcon(resized));

    }


}
