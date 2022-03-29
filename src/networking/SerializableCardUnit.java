package networking;

import domain.CardUnit;
import domain.TravelCard;
import enums.TravelCardType;

public class SerializableCardUnit {

    private TravelCardType type;

    public SerializableCardUnit (TravelCard original)
    {
        type = original.getType();
    }


}
