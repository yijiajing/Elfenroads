package savegames;

import domain.CardUnit;
import domain.Drawable;
import domain.TravelCard;
import enums.TravelCardType;

public abstract class SerializableCardUnit extends SerializableDrawable {

    public SerializableCardUnit (String filename)
    {
        super("./assets/sprites/" + filename + ".png");
    }


}
