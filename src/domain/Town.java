package domain;

import enums.Colour;
import loginwindow.MainFrame;
import networking.GameState;
import panel.GameScreen;
import panel.TownPanel;

import java.util.ArrayList;
import java.util.List;
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
        this.x = MainFrame.getInstance().getWidth() * x / 1440;
        this.y = MainFrame.getInstance().getHeight() * y / 900;
        this.width = MainFrame.getInstance().getWidth() * pWidth / 1440;
        this.height = MainFrame.getInstance().getHeight() * pHeight / 900;
        this.townPieces = new ArrayList<>();
        this.panel = new TownPanel(name, this.x, this.y, width, height, pScreen, this);
    }

    public String getName() {
        return name;
    }

    public TownPanel getPanel() {
        return panel;
    }

    /**
     * Create town pieces for the colours of players in the game
     */
    public void initializeTownPieces() {
        List<Player> players = GameState.instance().getPlayers();

        for (Player p : players) {
            townPieces.add(new TownPiece(p.getColour(), this));
        }
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
