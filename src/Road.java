public class Road {

    private RegionType regionType;
    private CounterPanel counterPanel;

    public Road(RegionType regionType, int x, int y, GameScreen pScreen) {
        this.regionType = regionType;
        if (canPlaceCounter()) {
            counterPanel = new CounterPanel(x, y, pScreen);
        }
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
}
