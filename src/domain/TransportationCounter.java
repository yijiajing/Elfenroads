package domain;

import enums.CounterType;
import enums.RegionType;
import enums.RoundPhaseType;
import loginwindow.MainFrame;
import networking.ActionManager;
import networking.GameState;
import panel.GameScreen;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class TransportationCounter extends CounterUnit implements Comparable<TransportationCounter> {

    private CounterType type;

    public TransportationCounter(CounterType pType, int resizeWidth, int resizeHeight)
    {
        super(resizeWidth, resizeHeight, pType.ordinal() + 1); // since the images start from M01, not M00
        this.type = pType;

        this.getDisplay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isOwned()) { // counter is face-up and available to be chosen
                    if (GameState.instance().getCurrentPhase().equals(RoundPhaseType.DRAW_COUNTERS)) {
                        GameState.instance().getFaceUpCounters().remove(TransportationCounter.this); // remove the counter from the face-up pile
                        GameManager.getInstance().getThisPlayer().getHand().addUnit(TransportationCounter.this); // add to player's hand
                        GameState.instance().addFaceUpCounterFromPile(); // replenish the face-up counters with one from the pile
                        GameScreen.getInstance().updateAll(); // update GUI
                        TransportationCounter.this.owned = true;
                        GameManager.getInstance().endTurn();
                    }
                } else if (GameState.instance().getCurrentPhase().equals(RoundPhaseType.RETURN_COUNTERS)) {
                    while (true) { // get the user to try again if there is an error
                        try {
                            GameManager.getInstance().returnAllCountersExceptOne(TransportationCounter.this);
                            return;
                        } catch (IllegalArgumentException err) { // the user has selected a counter that is not in their hand
                            GameScreen.displayMessage("You must select a transportation counter from your hand. Try again.", false, false);
                        }
                    }

                } else if (getPlacedOn() == null) {
                    ActionManager.getInstance().setSelectedCounter(TransportationCounter.this);
                } else {
                    // If the counter is placed on a road, then the user's intention is to click on the road
                    ActionManager.getInstance().setSelectedRoad(getPlacedOn());
                }
            }
        });
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


    public static TransportationCounter getNew(CounterType counterType) {
        return new TransportationCounter(counterType, MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900);
    }
}
