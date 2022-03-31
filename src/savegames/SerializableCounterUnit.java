package savegames;

import domain.CounterUnit;
import domain.Drawable;
import domain.TransportationCounter;
import enums.CounterType;
import enums.CounterUnitType;

// a serializable translation of CounterUnit that we can save to a file
public abstract class SerializableCounterUnit extends SerializableDrawable {

    private CounterUnitType type;
    private boolean isSecret;

    public SerializableCounterUnit(CounterUnit original, String imageNumber) // need this to be a String for magic spells
    {
        super ("./assets/sprites/M0" + imageNumber + ".png");
        type = original.getType();
        isSecret = original.isSecret();
    }




}
