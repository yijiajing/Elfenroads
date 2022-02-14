package domain;

import java.util.*;

public class Hand {
	private List<CardUnit> cardList;
	private List<TransportationCounter> counterList;
	private Optional<Obstacle> obstacle;
	
	public Hand() {
		cardList = new ArrayList<>();
		counterList = new ArrayList<>();
		obstacle = Optional.empty();
	}
	
	public void addUnit(Drawable pUnit) {
		if (pUnit instanceof CardUnit) {
			cardList.add((CardUnit)pUnit);
		} else if (pUnit instanceof Obstacle) {
			obstacle = Optional.of((Obstacle) pUnit);
		} else {
			counterList.add((TransportationCounter) pUnit);
		}
	}
	
	public void removeUnit(Drawable pUnit) {
		if (pUnit instanceof CardUnit) {
			cardList.remove((CardUnit)pUnit);
		} else if (pUnit instanceof Obstacle) {
			obstacle = Optional.empty();
		} else {
			counterList.remove((TransportationCounter) pUnit);
		}
	}

	public List<CardUnit> getCards() {
		return this.cardList;
	}

	public Obstacle getObstacle() {
		if (obstacle.isPresent()) {
			return obstacle.get();
		} else {
			return null;
		}
	}
}
