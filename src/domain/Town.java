package domain;

import enums.Colour;
import loginwindow.MainFrame;
import networking.GameState;
import panel.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static utils.GameRuleUtils.isElfengoldVariant;

public class Town implements Comparable<Town> {

    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private TownPanel panel;
    private ArrayList<TownPiece> townPieces;
    private GameScreen gameScreen;

    // only for elfengold variant
    private GoldValueToken goldValueToken = null;
    private GoldValueTokenPanel tokenPanel = null;

    public Town(String name, int x, int y, int pWidth, int pHeight, GameScreen pScreen, int tokenValue) {
        this.name = name;
        this.gameScreen = pScreen;
        this.x = MainFrame.getInstance().getWidth() * x / 1440;
        this.y = MainFrame.getInstance().getHeight() * y / 900;
        this.width = MainFrame.getInstance().getWidth() * pWidth / 1440;
        this.height = MainFrame.getInstance().getHeight() * pHeight / 900;
        this.townPieces = new ArrayList<>();
        this.panel = new TownPanel(name, this.x, this.y, width, height, pScreen, this);

        if (isElfengoldVariant() && tokenValue > 0) {
            this.goldValueToken = new GoldValueToken(tokenValue, MainFrame.getInstance().getWidth()/144, MainFrame.getInstance().getHeight()/70);
            this.tokenPanel = new GoldValueTokenPanel(this, this.goldValueToken, this.x-10, this.y);
        }
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

    // not truly a getter method since this is not a field, but functions similarly
    public ElfBootPanel getElfBootPanel()
    {
        return panel.getElfBootPanel();
    }

    public GoldValueToken getGoldValueToken() {
        return this.goldValueToken;
    }

    public GoldValueTokenPanel getTokenPanel() {
        return this.tokenPanel;
    }
}
