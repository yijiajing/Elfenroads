package domain;

import enums.RegionType;
import panel.CounterPanel;
import gamescreen.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class Road {

    private RegionType regionType;
    private CounterPanel counterPanel;
    private List<CounterUnit> counters = new ArrayList<>();

    public Road(RegionType regionType, int x, int y, GameScreen pScreen) {
        this.regionType = regionType;
        if (canPlaceCounter()) {
            counterPanel = new CounterPanel(x, y, this, pScreen);
        }
    }

    public void roadSelected() {
    		
    }

    public boolean canPlaceCounter() {
        return !(regionType == RegionType.LAKE || regionType == RegionType.RIVER);
    }

    public RegionType getRegionType() {
        return regionType;
    }

    public boolean setTransportationCounter(TransportationCounter transportationCounter) {
        if (regionType == RegionType.LAKE || regionType == RegionType.RIVER || numOfTransportationCounter() > 0) {
            return false;
        }

        if (transportationCounter.getRequiredNumOfUnitsOn(this) >= 1){
            transportationCounter.setPlacedOn(this);
            transportationCounter.setOwned(false);
            counters.add(transportationCounter);
            counterPanel.addCounterUnit(transportationCounter); // update map
            return true;
        } else {
            return false;
        }
    }
    
	public void setMagicSpell(MagicSpell counter) {
		// TODO 
		
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
        if (numOfTransportationCounter() == 0 || hasObstacle()) {
            return false;
        }
        counters.add(obstacle);
        obstacle.setPlacedOn(this);
        obstacle.setOwned(false);
        counterPanel.addCounterUnit(obstacle); // update map
        return true;
    }

    public void clear() {
        for (CounterUnit c: counters) {
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
        for (CounterUnit c: counters) {
            if (c instanceof TransportationCounter) {
                ct++;
            }
        }
        return ct;
    }

    public boolean hasObstacle() {
        for (CounterUnit c: counters) {
            if (c instanceof Obstacle) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMagicSpell() {
        for (CounterUnit c: counters) {
            if (c instanceof MagicSpell) {
                return true;
            }
        }
        return false;
    }

    public boolean hasGoldPiece() {
        for (CounterUnit c: counters) {
            if (c instanceof GoldPiece) {
                return true;
            }
        }
        return false;
    }
}
