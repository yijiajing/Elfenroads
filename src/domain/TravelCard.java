package domain;

import enums.TravelCardType;

public class TravelCard extends CardUnit {

    private TravelCardType type;

    public TravelCard(TravelCardType type, int resizeWidth, int resizeHeight) {
        super(resizeWidth, resizeHeight, "T0" + (type.ordinal() + 1));
        this.type = type;
    }

    public TravelCardType getType() {
        return type;
    }
}
