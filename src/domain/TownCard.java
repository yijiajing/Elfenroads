package domain;

public class TownCard extends Drawable {

    private final Town town;

    protected TownCard(String pImageFilepath, Town town) {
        super(pImageFilepath);
        this.town = town;
    }

    public Town getTown() {
        return town;
    }
}
