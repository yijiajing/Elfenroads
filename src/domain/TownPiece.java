package domain;

import enums.Colour;
import panel.GameScreen;

import javax.swing.*;
import java.awt.*;

public class TownPiece {

    private Colour colour;
    private JLabel image;
    private int width;
    private int height;
    private Town town;
    private GameScreen gameScreen;

    public TownPiece(Colour pColour, Town pTown, GameScreen pScreen) {
        this.colour = pColour;
        this.town = pTown;
        this.gameScreen = pScreen;
        this.width = gameScreen.getWidth() * 1/144;
        this.height = gameScreen.getHeight() * 1/90;

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
