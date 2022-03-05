package domain;

import javax.swing.*;
import java.awt.*;

public abstract class CardUnit extends Drawable{

    private boolean selected;

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

    public void setSelected(boolean selected) {
        if (selected && !this.selected) {
            // this card is selected
            //TODO: add a highlight border when selected

        } else if (!selected && this.selected) {
            // this card is deselected
            //TODO: remove the highlight border when deselected

        }
        this.selected = selected;
    }


}
