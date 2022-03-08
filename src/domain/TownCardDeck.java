package domain;

import java.util.List;
import java.util.stream.Collectors;

public class TownCardDeck extends Deck<TownCard> {

    public TownCardDeck(String sessionID) {
        super(sessionID);
        List<Town> towns = GameMap.getInstance().getTownList();
        //TODO: we might display town cards in the future
        towns.forEach(t -> components.add(new TownCard("", t)));
        shuffle();
    }

    public List<String> getTownNames() {
        return components.stream().map(c -> c.getTown().getName()).collect(Collectors.toList());
    }
}
