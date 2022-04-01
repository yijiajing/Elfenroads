package domain;

import javax.swing.*;
import java.awt.*;

public abstract class CardUnit extends Drawable {

    boolean selected;
    JLabel miniDisplay;

    public CardUnit(int resizeWidth, int resizeHeight, String filename) {

        super ("./assets/sprites/" + filename + ".png");

        Image toResize = icon.getImage();
        Image resized = toResize.getScaledInstance(resizeWidth, resizeHeight,  java.awt.Image.SCALE_SMOOTH);
        Image resized_mini = toResize.getScaledInstance(resizeWidth-40, resizeHeight-50,  java.awt.Image.SCALE_SMOOTH);
        display = new JLabel(new ImageIcon(resized));
        miniDisplay = new JLabel(new ImageIcon(resized_mini));
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

    public JLabel getMiniDisplay() {
        return this.miniDisplay;
    }
}
