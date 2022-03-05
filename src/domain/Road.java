package domain;

import enums.RegionType;
import panel.CounterPanel;
import panel.GameScreen;

public class Road {

    private RegionType regionType;
    private CounterPanel counterPanel;
    private TransportationCounter transportationCounter;
    private boolean hasObstacle;

    public Road(RegionType regionType, int x, int y, GameScreen pScreen) {
        this.regionType = regionType;
        if (canPlaceCounter()) {
            counterPanel = new CounterPanel(x, y, this, pScreen);
        }
        this.hasObstacle = false;
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
        transportationCounter.setPlacedOn(this);
        this.transportationCounter = transportationCounter;
        counterPanel.setTransportationCounter(transportationCounter); // update UI
        return true;
    }

    public TransportationCounter getTransportationCounter() {
        return transportationCounter;
    }

    public boolean placeObstacle() {
        if (this.transportationCounter == null) {
            return false;
        }
        hasObstacle = true;
        counterPanel.placeObstacle(); // update UI
        return true;
    }

    public boolean hasObstacle() {
        return hasObstacle;
    }

    public void clear() {
        this.transportationCounter.setPlacedOn(null);
        this.transportationCounter = null;
        this.hasObstacle = false;
        counterPanel.clear();
    }
}
