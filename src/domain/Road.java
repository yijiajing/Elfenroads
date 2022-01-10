package domain;

import panel.CounterPanel;
import panel.GameScreen;

public class Road {

    private RegionType regionType;
    private CounterPanel counterPanel;

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

    public CounterPanel getCounterPanel() {
        return counterPanel;
    }

    public void clear() {
        counterPanel.clear();
    }
}
