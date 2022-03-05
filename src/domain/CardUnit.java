package domain;

import javax.swing.*;
import java.awt.*;

public abstract class CardUnit extends Drawable{

    private JLabel image;
    private boolean selected;

    public CardUnit(int resizeWidth, int resizeHeight, String filename) {

        ImageIcon imageIcon = new ImageIcon("./assets/sprites/" + filename + ".png");
        Image toResize = imageIcon.getImage();
        Image resized = toResize.getScaledInstance(resizeWidth, resizeHeight,  java.awt.Image.SCALE_SMOOTH);
        this.image = new JLabel(new ImageIcon(resized));

    }

    public JLabel getImage() {
        return this.image;
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
