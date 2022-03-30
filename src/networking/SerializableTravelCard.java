package networking;

import domain.TravelCard;
import enums.TravelCardType;

public class SerializableTravelCard extends SerializableCardUnit {

    private TravelCardType type;
    private boolean owned;

    public SerializableTravelCard (TravelCard original)
    {
        super(original);
    }





}
