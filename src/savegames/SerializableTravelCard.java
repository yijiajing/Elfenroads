package savegames;

import domain.TravelCard;
import enums.TravelCardType;

public class SerializableTravelCard extends SerializableCardUnit {

    private TravelCardType type;
    private boolean owned;

    public SerializableTravelCard (TravelCard original)
    {
        super(original);
        type = original.getType();
        owned = original.isOwned();
    }





}
