package domain;

import enums.GameVariant;
import enums.MagicSpellType;
import enums.ObstacleType;
import enums.RegionType;
import panel.CounterPanel;
import gamescreen.GameScreen;

import java.util.ArrayList;
import java.util.List;
import static utils.GameRuleUtils.isElfengoldVariant;

public class Road {

    private RegionType regionType;
    private CounterPanel counterPanel;
    private List<CounterUnit> counters = new ArrayList<>();

    public Road(RegionType regionType, int x, int y, GameScreen pScreen, GameVariant variant) {
        this.regionType = regionType;
        if (canPlaceCounter() || canPlaceSeaMonster(variant)) {
            counterPanel = new CounterPanel(x, y, this, pScreen);
        }
    }

    public void roadSelected() {

    }

    public boolean canPlaceCounter() {
        return !(regionType == RegionType.LAKE || regionType == RegionType.RIVER);
    }

    public boolean canPlaceSeaMonster() {
        return (isElfengoldVariant() && (regionType == RegionType.LAKE || regionType == RegionType.RIVER));
    }

    public boolean canPlaceSeaMonster(GameVariant variant) {
        return (isElfengoldVariant(variant) && (regionType == RegionType.LAKE || regionType == RegionType.RIVER));
    }

    public boolean canPlaceTreeObstacle() {
        return !(regionType == RegionType.LAKE || regionType == RegionType.RIVER);
    }

    public RegionType getRegionType() {
        return regionType;
    }

    public boolean setTransportationCounter(TransportationCounter transportationCounter) {
        if (regionType == RegionType.LAKE || regionType == RegionType.RIVER ||
                (numOfTransportationCounter() > 0 && !hasDouble())) {
            return false;
        } else if (hasDouble() && numOfTransportationCounter() != 1) {
            return false;
        }

        if (transportationCounter.getRequiredNumOfUnitsOn(this) > 0) {
            transportationCounter.setPlacedOn(this);
            transportationCounter.setOwned(false);
            counters.add(transportationCounter);
            counterPanel.addCounterUnit(transportationCounter); // update map
            return true;
        } else {
            return false;
        }
    }

    public boolean exchangeWith(Road anotherRoad) {
        assert numOfTransportationCounter() == 1 && anotherRoad.numOfTransportationCounter() == 1;
        TransportationCounter t1 = getAllTransportationCounters().get(0);
        TransportationCounter t2 = anotherRoad.getAllTransportationCounters().get(0);
        assert counters.contains(t1) && anotherRoad.counters.contains(t2);
        if (t2.getRequiredNumOfUnitsOn(this) > 0 && t1.getRequiredNumOfUnitsOn(anotherRoad) > 0) {
            counters.remove(t1);
            anotherRoad.counters.remove(t2);
            updateCounterPanel();
            setTransportationCounter(t2);
            anotherRoad.setTransportationCounter(t1);
            return true;
        } else {
            return false;
        }
    }

    private void updateCounterPanel() {
        counterPanel.clear();
        for (CounterUnit c : counters) {
            counterPanel.addCounterUnit(c);
        }
    }

    public boolean placeGoldPiece(GoldPiece goldPiece) {
        if (regionType == RegionType.LAKE || regionType == RegionType.RIVER || numOfTransportationCounter() == 0
                || hasObstacle() || hasGoldPiece()) {
            return false;
        }
        counters.add(goldPiece);
        goldPiece.setPlacedOn(this);
        goldPiece.setOwned(false);
        counterPanel.addCounterUnit(goldPiece);
        return true;
    }


    public List<CounterUnit> getCounters() {
        return counters;
    }

    public boolean placeObstacle(Obstacle obstacle) {
        if (hasObstacle()) {
            return false; // obstacle already exists on this road
        }

        if (obstacle.getType() == ObstacleType.TREE || obstacle.getType() == ObstacleType.EGTREE) {
            if (numOfTransportationCounter() == 0) {
                return false; // Tree obstacles can only be placed on roads that have a counter already
            }
            if (canPlaceTreeObstacle()) {
                counters.add(obstacle);
                obstacle.setPlacedOn(this);
                obstacle.setOwned(false);
                counterPanel.addCounterUnit(obstacle); // update map
                return true;
            } else {
                return false;
            }
        } else if (obstacle.getType() == ObstacleType.SEAMONSTER) {
            if (canPlaceSeaMonster()) {
                counters.add(obstacle);
                obstacle.setPlacedOn(this);
                obstacle.setOwned(false);
                counterPanel.addCounterUnit(obstacle); // update map
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean placeDouble(MagicSpell magicSpell) {
        assert magicSpell.getType() == MagicSpellType.DOUBLE;
        if (regionType == RegionType.RIVER || regionType == RegionType.LAKE
                || hasDouble() || numOfTransportationCounter() == 0) {
            return false;
        }
        counters.add(magicSpell);
        magicSpell.setPlacedOn(this);
        magicSpell.setOwned(false);
        counterPanel.addCounterUnit(magicSpell);
        return true;
    }

    public void clear() {
        for (CounterUnit c : counters) {
            c.setPlacedOn(null);
        }
        counters.clear();
        if (counterPanel != null) {
            counterPanel.clear();
        }
    }

    public CounterPanel getCounterPanel() {
        return counterPanel;
    }

    public int numOfTransportationCounter() {
        int ct = 0;
        for (CounterUnit c : counters) {
            if (c instanceof TransportationCounter) {
                ct++;
            }
        }
        return ct;
    }

    public List<TransportationCounter> getAllTransportationCounters() {
        List<TransportationCounter> tcs = new ArrayList<>();
        for (CounterUnit c : counters) {
            if (c instanceof TransportationCounter) {
                tcs.add((TransportationCounter) c);
            }
        }
        return tcs;
    }

    public boolean hasObstacle() {
        for (CounterUnit c : counters) {
            if (c instanceof Obstacle) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMagicSpell() {
        for (CounterUnit c : counters) {
            if (c instanceof MagicSpell) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDouble() {
        for (CounterUnit c : counters) {
            if (c instanceof MagicSpell && c.getType() == MagicSpellType.DOUBLE) {
                return true;
            }
        }
        return false;
    }

    public boolean hasGoldPiece() {
        for (CounterUnit c : counters) {
            if (c instanceof GoldPiece) {
                return true;
            }
        }
        return false;
    }
}
