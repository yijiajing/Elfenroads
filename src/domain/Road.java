package domain;

import enums.GameVariant;
import enums.ObstacleType;
import enums.RegionType;
import panel.CounterPanel;
import gamescreen.GameScreen;

import static utils.GameRuleUtils.isElfengoldVariant;

public class Road {

    private RegionType regionType;
    private CounterPanel counterPanel;
    private TransportationCounter transportationCounter;
    private Obstacle obstacle;
    private GameVariant variant;

    public Road(RegionType regionType, int x, int y, GameScreen pScreen, GameVariant variant) {
        this.regionType = regionType;
        this.variant = variant;
        if (canPlaceCounter() || canPlaceSeaMonster()) {
            counterPanel = new CounterPanel(x, y, this, pScreen);
        }
    }

    public void roadSelected() {
    		
    }

    public boolean canPlaceCounter() {
        return !(regionType == RegionType.LAKE || regionType == RegionType.RIVER);
    }

    public boolean canPlaceSeaMonster() {
        return (isElfengoldVariant(variant) && (regionType == RegionType.LAKE || regionType == RegionType.RIVER));
    }

    public boolean canPlaceTreeObstacle() {
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
    
	public void setMagicSpell(MagicSpell counter) {
		// TODO 
		
	}
	
	public void placeGoldPiece(GoldPiece counter) {
		//TODO
	}
    

    public TransportationCounter getTransportationCounter() {
        return transportationCounter;
    }

    public boolean placeObstacle(Obstacle obstacle) {
        if (this.obstacle != null) {
            return false; // obstacle already exists on this road
        }

        if (obstacle.getType() == ObstacleType.TREE) {
            if (transportationCounter == null) {
                return false; // Tree obstacles can only be placed on roads that have a counter already
            }
            if (canPlaceTreeObstacle()) {
                this.obstacle = obstacle;
                obstacle.setPlacedOn(this);
                counterPanel.placeObstacle(obstacle); // update map
                return true;
            } else {
                return false;
            }
        }

        else if (obstacle.getType() == ObstacleType.SEAMONSTER) {
            if (canPlaceSeaMonster()) {
                this.obstacle = obstacle;
                obstacle.setPlacedOn(this);
                counterPanel.placeObstacle(obstacle); // update map
                return true;
            } else {
                return false;
            }
        }

        else {
            return false;
        }
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
