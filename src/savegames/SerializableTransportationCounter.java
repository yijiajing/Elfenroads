package savegames;

import domain.TransportationCounter;
import enums.CounterType;

public class SerializableTransportationCounter extends SerializableCounterUnit {

    private CounterType type;

    public SerializableTransportationCounter(TransportationCounter original)
    {
        super(original, Integer.toString(original.getType().ordinal() - 1));
        type = original.getType();
    }

    public CounterType getType() {
        return type;
    }
}
