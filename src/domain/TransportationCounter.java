package domain;

import enums.CounterType;
import enums.RegionType;
import enums.RoundPhaseType;
import networking.ActionManager;
import networking.GameState;
import panel.GameScreen;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class TransportationCounter extends CounterUnit implements Comparable<TransportationCounter> {

    private CounterType type;
    private boolean owned; // indicates if the counter is owned by any player

    public TransportationCounter(CounterType pType, int resizeWidth, int resizeHeight)
    {
        super(resizeWidth, resizeHeight, pType.ordinal() + 1); // since the images start from M01, not M00
        this.type = pType;
        this.owned = false;

        this.getImage().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isOwned()) { // counter is face-up and available to be chosen
                    if (GameState.instance().getCurrentPhase().equals(RoundPhaseType.DRAWCOUNTERS)) {
                        GameState.instance().getFaceUpCounters().remove(TransportationCounter.this); // remove the counter from the face-up pile
                        GameManager.getInstance().getThisPlayer().getHand().addUnit(TransportationCounter.this); // add to player's hand
                        GameState.instance().addFaceUpCounterFromPile(); // replenish the face-up counters with one from the pile
                        GameScreen.getInstance().updateAll(); // update GUI
                        GameManager.getInstance().endTurn();
                        TransportationCounter.this.owned = true;

                        // this code should never execute but is used for testing with a single player
                        if (GameState.instance().getCurrentPlayer().equals(GameManager.getInstance().getThisPlayer())) {
                            GameManager.getInstance().planTravelRoutes(); // PHASE 4
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

    public boolean isOwned() {
        return this.owned;
    }

    public void setOwned(boolean b) {
        this.owned = b;
    }
}
