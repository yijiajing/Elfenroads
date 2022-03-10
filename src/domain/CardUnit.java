package domain;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public abstract class CardUnit extends Drawable {

    boolean selected;

    public CardUnit(int resizeWidth, int resizeHeight, String filename) {

        super ("./assets/sprites/" + filename + ".png");
        Image toResize = icon.getImage();
        Image resized = toResize.getScaledInstance(resizeWidth, resizeHeight,  java.awt.Image.SCALE_SMOOTH);
        display = new JLabel(new ImageIcon(resized));
        selected = false;

    }



    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean pSelected) {
        if (pSelected && !this.selected) {
            // this card is selected
            display.setBorder(BorderFactory.createLineBorder(Color.yellow,5));

        } else if (!pSelected && this.selected) {
            // this card is deselected
            display.setBorder(BorderFactory.createEmptyBorder());

        }
        this.selected = pSelected;
    }


}
