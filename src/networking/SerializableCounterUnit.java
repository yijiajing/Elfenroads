package networking;

import domain.CounterUnit;
import enums.CounterType;
import enums.CounterUnitType;
import org.w3c.dom.css.Counter;

// a serializable translation of CounterUnit that we can save to a file
public class SerializableCounterUnit {

    private CounterUnitType type;
    private boolean isSecret;

    public SerializableCounterUnit(CounterUnit original)
    {
        type = (CounterUnitType) original.getType(); // this should be a safe upcast
        isSecret = original.isSecret();
    }




}
