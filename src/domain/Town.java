package domain;

import enums.Colour;
import panel.GameScreen;
import panel.TownPanel;

import java.util.ArrayList;
import java.util.Objects;

public class Town implements Comparable<Town> {

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

        townPieces.add(new TownPiece(Colour.BLACK, this, gameScreen));
        townPieces.add(new TownPiece(Colour.BLUE, this, gameScreen));
        townPieces.add(new TownPiece(Colour.GREEN, this, gameScreen));
        townPieces.add(new TownPiece(Colour.PURPLE, this, gameScreen));
        townPieces.add(new TownPiece(Colour.RED, this, gameScreen));
        townPieces.add(new TownPiece(Colour.YELLOW, this, gameScreen));
    }

    public ArrayList<TownPiece> getTownPieces() {
        return townPieces;
    }

    public void removeTownPieceByColour(Colour colour) {
        for ( TownPiece piece : townPieces ) {
            if (piece.getColour() == colour) {
                townPieces.remove(piece);
                return;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Town town = (Town) o;
        return Objects.equals(getName(), town.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public int compareTo(Town o) {
        return name.compareTo(o.name);
    }
}
