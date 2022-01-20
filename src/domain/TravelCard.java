package domain;

public class TravelCard extends CardUnit {

    private CounterType type;

    public TravelCard(CounterType type, int resizeWidth, int resizeHeight) {
        super(resizeWidth, resizeHeight, "T0" + type.ordinal() + 1);
        this.type = type;
    }

    public CounterType getType() {
        return type;
    }
}
