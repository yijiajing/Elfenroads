package savegames;

import domain.TravelCard;
import enums.TravelCardType;

public class SerializableTravelCard extends SerializableCardUnit {

    private TravelCardType type;
    private boolean owned;

    public SerializableTravelCard (TravelCard original)
    {
        super("T0" + original.getType().ordinal() + 1);
        type = original.getType();
        owned = original.isOwned();
    }

    public TravelCardType getType() {
        return type;
    }

    public boolean isOwned() {
        return owned;
    }
}
