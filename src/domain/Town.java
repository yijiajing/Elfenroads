package domain;

import panel.GameScreen;
import panel.TownPanel;

import java.util.ArrayList;

public class Town {

    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private TownPanel panel;
    private ArrayList<TownPiece> townPieces;
    private GameScreen gameScreen;

    public Town(String name, int x, int y, int pWidth, int pHeight, GameScreen pScreen) {
        this.name = name;
        this.gameScreen = pScreen;
        this.x = pScreen.getWidth() * x / 1440;
        this.y = pScreen.getHeight() * y / 900;
        this.width = pScreen.getWidth() * pWidth / 1440;
        this.height = pScreen.getHeight() * pHeight / 900;
        this.townPieces = new ArrayList<>();

        // put town pieces on every town except for Elvenhold
        if (!name.equalsIgnoreCase("Elvenhold")) {
            initializeTownPieces();
        }

        this.panel = new TownPanel(name, this.x, this.y, width, height, pScreen, this);

    }

    public String getName() {
        return name;
    }

    public TownPanel getPanel() {
        return panel;
    }

    public void initializeTownPieces() {
        // TODO : get current players and add town pieces for those colours only

        townPieces.add(new TownPiece("black", this, gameScreen));
        townPieces.add(new TownPiece("blue", this, gameScreen));
        townPieces.add(new TownPiece("green", this, gameScreen));
        townPieces.add(new TownPiece("purple", this, gameScreen));
        townPieces.add(new TownPiece("red", this, gameScreen));
        townPieces.add(new TownPiece("yellow", this, gameScreen));
    }

    public ArrayList<TownPiece> getTownPieces() {
        return townPieces;
    }

    public void removeTownPieceByColour(String colour) {
        for ( TownPiece piece : townPieces ) {
            if (piece.getColour().equalsIgnoreCase(colour)) {
                townPieces.remove(piece);
                return;
            }
        }
    }
}
