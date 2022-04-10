package domain;

import enums.ObstacleType;

import java.util.*;

public class Hand {
	private List<CardUnit> cardList;
	private List<CounterUnit> counterList;
	private Optional<Obstacle> obstacle;
	private int numTravelCards;
	
	public Hand() {
		cardList = new ArrayList<>();
		counterList = new ArrayList<>();
		obstacle = Optional.empty();
	}
	
	public void addUnit(Drawable pUnit) {
		if (pUnit instanceof CardUnit) {
			cardList.add((CardUnit)pUnit);
		} else if (pUnit instanceof Obstacle && ((Obstacle) pUnit).getType() == ObstacleType.TREE) {
			obstacle = Optional.of((Obstacle) pUnit);
		} else if (pUnit instanceof CounterUnit) {
			counterList.add((CounterUnit) pUnit);
		}
	}

	public void removeUnit(Drawable pUnit) {
		if (pUnit instanceof CardUnit) {
			cardList.remove(pUnit);
		} else if (pUnit instanceof Obstacle && ((Obstacle) pUnit).getType() == ObstacleType.TREE) {
			obstacle = Optional.empty();
		} else if (pUnit instanceof CounterUnit) {
			counterList.remove(pUnit);
		}
	}

	public void removeUnits(List<? extends Drawable> units) {
		units.forEach(this::removeUnit);
	}

	public List<CardUnit> getCards() {
		return this.cardList;
	}

	public void setNumTravelCards(int numTravelCards) {
		this.numTravelCards = numTravelCards;
	}

	public int getNumTravelCards() {
		return numTravelCards;
	}

	public int getCardListSize() {
		return cardList.size();
	}

	public void updateNumTravelCards() {
		numTravelCards = cardList.size();
	}
	
	public void clearCounters() {
		this.counterList.clear();
	}
	
	public List<CounterUnit> getCounters() {
		return this.counterList;
	}

	public Obstacle getObstacle() {
		return obstacle.orElse(null);
	}

	public boolean hasObstacle() { return obstacle.isPresent();}
}
