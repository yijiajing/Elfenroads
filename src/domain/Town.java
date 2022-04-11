package domain;

import enums.Colour;
import enums.GameVariant;
import gamescreen.GameScreen;
import windows.MainFrame;
import networking.GameState;
import panel.*;

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
    private ElfBootPanel elfBootPanel;
    private ArrayList<TownPiece> townPieces;

    // only for elfengold variant
    private GoldValueToken goldValueToken = null;
    private GoldValueTokenPanel tokenPanel = null;

    public Town(GameVariant variant, String name, int x, int y, int pWidth, int pHeight, GameScreen pScreen, int tokenValue) {
        this.name = name;
        this.x = MainFrame.getInstance().getWidth() * x / 1440;
        this.y = MainFrame.getInstance().getHeight() * y / 900;
        this.width = MainFrame.getInstance().getWidth() * pWidth / 1440;
        this.height = MainFrame.getInstance().getHeight() * pHeight / 900;
        this.townPieces = new ArrayList<>();
        this.panel = new TownPanel(name, this.x, this.y, width, height, pScreen, this);
        this.elfBootPanel = new ElfBootPanel(this, this.x-10, this.y+height, MainFrame.getInstance().getWidth()*13/144, MainFrame.getInstance().getHeight()/35, pScreen);

        if (isElfengoldVariant(variant) && variant != GameVariant.ELFENGOLD_RANDOM_GOLD && tokenValue > 0) {
            setGoldValueToken(new GoldValueToken(tokenValue, MainFrame.getInstance().getWidth()/50, MainFrame.getInstance().getHeight()/40));
        }
    }

    public String getName() {
        return name;
    }

    public TownPanel getPanel() {
        return panel;
    }

    public void setGoldValueToken(GoldValueToken goldValueToken) {
        this.goldValueToken = goldValueToken;
        this.tokenPanel = new GoldValueTokenPanel(this, this.goldValueToken, this.x-20, this.y);
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

    public void initializeTownPiecesLoadedGame() {
        List<Player> players = GameState.instance().getPlayers();

        for (Player p : players) {
            if (!p.getTownsVisited().contains(this)) {
                townPieces.add(new TownPiece(p.getColour(), this));
            }
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

    public ElfBootPanel getElfBootPanel()
    {
        return this.elfBootPanel;
    }

    public GoldValueToken getGoldValueToken() {
        return this.goldValueToken;
    }

    public int getGoldValue() {
        return goldValueToken.getValue();
    }

    public GoldValueTokenPanel getTokenPanel() {
        return this.tokenPanel;
    }
}
