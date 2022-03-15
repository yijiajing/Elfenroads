package utils;

import domain.*;
import enums.GameVariant;
import enums.RegionType;
import enums.RoundPhaseType;
import enums.TravelCardType;
import networking.GameState;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public final class GameRuleUtils {

    private static final Logger LOGGER = Logger.getLogger("Game Rule Utils");

    private GameRuleUtils() {
    }

    /**
     * @param gameMap  the game map
     * @param srcTown  the town where the player is current in
     * @param destTown the town where the player intends to move to
     * @param cards    the travel cards the player intends to use
     * @return whether the player can move from srcTown to destTown using the cards provided
     * Note: returns false when there are extra cards provided. In other words, the move is only valid
     * with an exact number of exact types of travel cards
     */
    public static boolean validateMove(GameMap gameMap, Town srcTown, Town destTown, List<TravelCard> cards) {
        if (cards == null || cards.isEmpty()) {
            return false;
        } else if (!(GameState.instance().getCurrentPhase() == RoundPhaseType.MOVE
                && !cards.isEmpty()
                && GameManager.getInstance().isLocalPlayerTurn())) {
            return false;
        }
        Set<Road> roads = gameMap.getRoadsBetween(srcTown, destTown);

        //TODO: implement elven witch and special transportation counters for Elfengold

        for (Road road : roads) {
            // cannot move on a non-river and non-lake road without transportation counter
            if (road.getRegionType() != RegionType.LAKE && road.getRegionType() != RegionType.RIVER
                    && road.getTransportationCounter() == null) {
                continue;
            }
            if (road.getRegionType() == RegionType.LAKE) {
                // lake - two raft cards
                if (cards.size() == 2 && cards.stream().allMatch(card -> card.getType() == TravelCardType.RAFT)) {
                    LOGGER.info("[Lake] Can travel with two raft cards");
                    return true;
                }
            } else if (road.getRegionType() == RegionType.RIVER) {
                if (gameMap.getRoadSource(road).equals(destTown)) {
                    // upriver - two raft cards
                    if (cards.size() == 2 && cards.stream().allMatch(card -> card.getType() == TravelCardType.RAFT)) {
                        LOGGER.info("[Upriver] Can travel with two raft cards");
                        return true;
                    }
                } else {
                    // downriver - one raft card
                    if (cards.size() == 1 && cards.get(0).getType() == TravelCardType.RAFT) {
                        LOGGER.info("[Downriver] Can travel with one raft card");
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
        for (Road road : roads) {
            LOGGER.info("No matching cards, checking for Caravan...");
            if (road.getRegionType() == RegionType.LAKE || road.getRegionType() == RegionType.RIVER
                    || road.getTransportationCounter() == null) {
                // caravan cannot move on a lake/river or a road without transportation counter
                continue;
            }
            int requiredNumOfCards = road.hasObstacle() ? 4 : 3;
            if (cards.size() == requiredNumOfCards) {
                LOGGER.info("Caravan triggered with " + requiredNumOfCards + " cards");
                return true;
            }
        }
        return false;
    }

    public static boolean isDrawCountersPhase() {
        return List.of(RoundPhaseType.DRAW_COUNTER_ONE,
                RoundPhaseType.DRAW_COUNTER_TWO, RoundPhaseType.DRAW_COUNTER_THREE)
                .contains(GameState.instance().getCurrentPhase());
    }

    public static boolean isPlanRoutesPhase() {
        return List.of(RoundPhaseType.PLAN_ROUTES_ONE,
                RoundPhaseType.PLAN_ROUTES_TWO, RoundPhaseType.PLAN_ROUTES_THREE,
                RoundPhaseType.PLAN_ROUTES_FOUR, RoundPhaseType.PLAN_ROUTES_FIVE,
                RoundPhaseType.PLAN_ROUTES_SIX)
                .contains(GameState.instance().getCurrentPhase());
    }

    public static boolean isElfengoldVariant() {
        GameVariant gameVariant = GameState.instance().getGameVariant();

        return ((gameVariant == GameVariant.ELFENGOLD_CLASSIC) || (gameVariant == GameVariant.ELFENGOLD_WITCH) ||
                (gameVariant == GameVariant.ELFENGOLD_RANDOM_GOLD) || (gameVariant == GameVariant.ELFENGOLD_TRAVEL_CARDS));
    }

}
