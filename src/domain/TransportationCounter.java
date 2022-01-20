package domain;

import javax.swing.*;
import java.util.Objects;

public class TransportationCounter extends CounterUnit implements Comparable<TransportationCounter> {

    // another rudimentary class to go along with domain.Deck

    // will represent, for now, a Transportation Counter. It will contain information about how to display said counter.

    private CounterType type;

    public TransportationCounter (CounterType pType, int resizeWidth, int resizeHeight)
    {
        super(resizeWidth, resizeHeight, pType.ordinal() + 1); // since the images start from M01, not M00
        this.type = pType;
    }


    public CounterType getType() {return this.type;}

    public int getRequiredNumOfUnitsOn(Road r) {
        RegionType region = r.getRegionType();
        switch(type) {
            case GIANTPIG:
                if (region == RegionType.PLAIN || region == RegionType.WOODS) {
                    return 1;
                }
                break;
            case ELFCYCLE:
                if (region == RegionType.PLAIN || region == RegionType.WOODS) {
                    return 1;
                } else if (region == RegionType.MOUNTAIN) {
                    return 2;
                }
                break;
            case MAGICCLOUD:
                if (region == RegionType.PLAIN || region == RegionType.WOODS) {
                    return 2;
                } else if (region == RegionType.MOUNTAIN) {
                    return 1;
                }
                break;
            case UNICORN:
                if (region == RegionType.MOUNTAIN || region == RegionType.WOODS) {
                    return 1;
                } else if (region == RegionType.DESERT) {
                    return 2;
                }
                break;
            case TROLLWAGON:
                if (region == RegionType.DESERT || region == RegionType.MOUNTAIN || region == RegionType.WOODS) {
                    return 2;
                } else if (region == RegionType.PLAIN) {
                    return 1;
                }
                break;
            case DRAGON:
                if (region == RegionType.PLAIN || region == RegionType.DESERT || region == RegionType.MOUNTAIN) {
                    return 1;
                } else if (region == RegionType.WOODS) {
                    return 2;
                }
                break;
        }
        return -1; // not applicable
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransportationCounter that = (TransportationCounter) o;
        return getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

    @Override
    public int compareTo(TransportationCounter o) {
        return type.compareTo(o.type);
    }
}
