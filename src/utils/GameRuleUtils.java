package utils;

import domain.*;
import enums.RegionType;
import enums.TravelCardType;

import java.util.List;
import java.util.Set;

public final class GameRuleUtils {

    private GameRuleUtils() {}

    /**
     *
     * @param gameMap the game map
     * @param srcTown the town where the player is current in
     * @param destTown the town where the player intends to move to
     * @param cards the travel cards the player intends to use
     * @return whether the player can move from srcTown to destTown using the cards provided
     *         Note: returns false when there are extra cards provided. In other words, the move is only valid
     *         with an exact number of exact types of travel cards
     */
    public static boolean validateMove(GameMap gameMap, Town srcTown, Town destTown, List<TravelCard> cards) {
        if (cards == null || cards.isEmpty()) {
            return false;
        }
        Set<Road> roads = gameMap.getRoadsBetween(srcTown, destTown);

        //TODO: implement elven witch and special transportation counters for Elfengold

        for (Road road: roads) {
            // cannot move on a non-river and -lake road without transportation counter
            if (road.getRegionType() != RegionType.LAKE && road.getRegionType() != RegionType.RIVER
                    && road.getTransportationCounter() == null) {
                continue;
            }
            if (road.getRegionType() == RegionType.LAKE) {
                // lake - two raft cards
                if (cards.size() == 2 && cards.stream().allMatch(card -> card.getType() == TravelCardType.RAFT)) {
                    return true;
                }
            } else if (road.getRegionType() == RegionType.RIVER) {
                if (gameMap.getRoadSource(road).equals(destTown)) {
                    // upriver - two raft cards
                    if (cards.size() == 2 && cards.stream().allMatch(card -> card.getType() == TravelCardType.RAFT)) {
                        return true;
                    }
                } else {
                    // downriver - one raft card
                    if (cards.size() == 1 && cards.get(0).getType() == TravelCardType.RAFT) {
                        return true;
                    }
                }
            } else {
                // convert transportation counter type to corresponding card type
                TravelCardType requiredType = TravelCardType.valueOf(road.getTransportationCounter().getType().name());
                int requiredNumOfCards = road.getTransportationCounter().getRequiredNumOfUnitsOn(road);
                if (road.hasObstacle()) {
                    requiredNumOfCards++;
                }
                if (cards.size() == requiredNumOfCards && cards.stream().allMatch(card -> card.getType() == requiredType)) {
                    return true;
                }
            }
        }

        // no matching cards, check for Caravan (any three travel cards, or four if there is an obstacle)
        for (Road road: roads) {
            if (!(road.getRegionType() == RegionType.LAKE || road.getRegionType() == RegionType.RIVER)) {
                int requiredNumOfCards = road.hasObstacle()? 3: 4;
                if (cards.size() == requiredNumOfCards) {
                    return true;
                }
            }
        }
        return false;
    }


}
