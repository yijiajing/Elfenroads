package domain;

import commands.DrawCounterCommand;
import enums.CounterType;
import enums.RegionType;
import enums.RoundPhaseType;
import gamemanager.GameManager;
import loginwindow.MP3Player;
import loginwindow.MainFrame;
import networking.ActionManager;
import networking.GameState;
import panel.GameScreen;
import utils.GameRuleUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class TransportationCounter extends CounterUnit implements Comparable<TransportationCounter> {

    private CounterType type;
    private MP3Player track1 = new MP3Player("./assets/Music/0000171.mp3");

    public TransportationCounter(CounterType pType, int resizeWidth, int resizeHeight) {
        super(resizeWidth, resizeHeight, pType.ordinal() + 1); // since the images start from M01, not M00
        this.type = pType;

        initializeMouseListener();
    }

    private void initializeMouseListener() {
        this.getDisplay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!GameManager.getInstance().isLocalPlayerTurn()) {
                    return;
                }

                // DRAW COUNTERS PHASE, counter is face-up and available to be chosen
                if (!isOwned() && GameRuleUtils.isDrawCountersPhase()) {
                    // adding the counter to my hand
                    track1.play();
                    GameState.instance().getFaceUpCounters().remove(TransportationCounter.this); // remove the counter from the face-up pile
                    GameManager.getInstance().getThisPlayer().getHand().addUnit(TransportationCounter.this);
                    GameState.instance().addFaceUpCounterFromPile(); // replenish the face-up counters with one from the pile
                    GameScreen.getInstance().updateAll(); // update GUI
                    TransportationCounter.this.owned = true;
                    TransportationCounter.this.setSecret(false);
                    Logger.getGlobal().info("Just added " + TransportationCounter.this.getType() +
                            ", current counters in hand: " +
                            GameManager.getInstance().getThisPlayer().getHand().getCounters().toString());

                    // tell the other peers to remove the counter
                    try {
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(
                                new DrawCounterCommand(TransportationCounter.this, true));
                    } catch (IOException err) {
                        System.out.println("Error: there was a problem sending the DrawCounterCommand to the other peers.");
                    }

                    GameManager.getInstance().endTurn();

                }
                // RETURN COUNTERS PHASE
                else if (GameState.instance().getCurrentPhase() == RoundPhaseType.RETURN_COUNTERS) {
                    GameManager.getInstance().returnCounter(TransportationCounter.this);
                    track1.play();
                }

                // PLAN TRAVEL ROUTES PHASE
                else if (GameState.instance().getCurrentPhase() == RoundPhaseType.PLAN_ROUTES) {
                    if (getPlacedOn() == null) {
                        ActionManager.getInstance().setSelectedCounter(TransportationCounter.this);
                    } else {
                        // If the counter is placed on a road, then the user's intention is to click on the road
                        ActionManager.getInstance().setSelectedRoad(getPlacedOn());
                    }
                    track1.play();
                }
            }
        });
    }

    public CounterType getType() {
        return this.type;
    }

    public int getRequiredNumOfUnitsOn(Road r) {
        RegionType region = r.getRegionType();
        switch (type) {
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
        return new TransportationCounter(counterType, MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900);
    }

    @Override
    public String toString() {
        return "TransportationCounter{" +
                "owned=" + owned +
                ", isSecret=" + isSecret +
                ", type=" + type +
                '}';
    }
}
