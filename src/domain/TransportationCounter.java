package domain;

import enums.CounterType;
import enums.CounterUnitType;
import enums.RegionType;
import windows.MainFrame;
import savegames.SerializableTransportationCounter;

import windows.MainFrame;
import java.util.Objects;

public class TransportationCounter extends CounterUnit {

    private CounterType aType;
    
    public TransportationCounter(CounterType pType, int resizeWidth, int resizeHeight) {
        super(pType, resizeWidth, resizeHeight, Integer.toString(pType.ordinal() + 1)); // since the images start from M01, not M00
        this.aType = pType;

        super.initializeMouseListener();
    }

    public TransportationCounter(SerializableTransportationCounter loaded)
    {
        super (loaded.getType(), MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900, Integer.toString(loaded.getType().ordinal() + 1));
        this.aType = loaded.getType();

        super.initializeMouseListener();
    }

    @Override
    public CounterType getType() {
        return this.aType;
    }

    public int getRequiredNumOfUnitsOn(Road r) {
        RegionType region = r.getRegionType();
        switch (aType) {
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
    public String toString() {
        return "TransportationCounter{" +
                "owned=" + owned +
                ", isSecret=" + isSecret +
                ", type=" + aType +
                '}';
    }
}
