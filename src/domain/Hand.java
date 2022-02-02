package domain;

import java.util.*;

public class Hand {
	private List<CardUnit> cardList;
	private List<CounterUnit> counterList;
	
	public Hand() {
		cardList = new ArrayList<>();
		counterList = new ArrayList<>();
	}
	
	public void addUnit(Drawable pUnit) {
		if (pUnit instanceof CardUnit) cardList.add((CardUnit)pUnit); else counterList.add((CounterUnit)pUnit);
	}
	
	public void removeUnit(Drawable pUnit) {
		if (pUnit instanceof CardUnit) cardList.remove((CardUnit)pUnit); else counterList.remove((CounterUnit)pUnit);
	}
	
}
