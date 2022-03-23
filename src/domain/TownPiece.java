package domain;

import enums.Colour;
import loginwindow.MainFrame;

import javax.swing.*;
import java.awt.*;

public class TownPiece {

    private Colour colour;
    private JLabel image;
    private int width;
    private int height;
    private Town town;

    public TownPiece(Colour pColour, Town pTown) {
        this.colour = pColour;
        this.town = pTown;
        this.width = MainFrame.getInstance().getWidth()/144;
        this.height = MainFrame.getInstance().getHeight()/90;

        ImageIcon pieceIcon = new ImageIcon("./assets/boppels-and-boots/boppel-" + this.colour + ".png");
        Image pieceImage = pieceIcon.getImage();
        Image pieceResized = pieceImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        this.image = new JLabel(new ImageIcon(pieceResized));
    }

    public JLabel getImage() {
        return image;
    }

    public Colour getColour() {
        return this.colour;
    }
}
