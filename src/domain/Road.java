package domain;

import enums.RegionType;
import panel.CounterPanel;
import gamescreen.GameScreen;

public class Road {

    private RegionType regionType;
    private CounterPanel counterPanel;
    private TransportationCounter transportationCounter;
    private Obstacle obstacle;

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
        if (regionType == RegionType.LAKE || regionType == RegionType.RIVER || this.transportationCounter != null) {
            return false;
        }

        if (transportationCounter.getRequiredNumOfUnitsOn(this) >= 1){
            transportationCounter.setPlacedOn(this);
            this.transportationCounter = transportationCounter;
            counterPanel.setTransportationCounter(transportationCounter); // update map
            this.transportationCounter.setOwned(false);
            return true;
        } else {
            return false;
        }
    }

    public TransportationCounter getTransportationCounter() {
        return transportationCounter;
    }

    public boolean placeObstacle(Obstacle obstacle) {
        if (transportationCounter == null || this.obstacle != null) {
            return false;
        }
        this.obstacle = obstacle;
        obstacle.setPlacedOn(this);
        counterPanel.placeObstacle(obstacle); // update map
        return true;
    }

    public void clear() {
        if (this.transportationCounter != null) {
            this.transportationCounter.setPlacedOn(null);
            this.transportationCounter = null;
        }
        if (this.obstacle != null) {
            this.obstacle.setPlacedOn(null);
            this.obstacle = null;
        }
        if (counterPanel != null) {
            counterPanel.clear();
        }
    }

    public CounterPanel getCounterPanel() {
        return counterPanel;
    }

    public boolean hasObstacle() {
        return obstacle != null;

    }
}
