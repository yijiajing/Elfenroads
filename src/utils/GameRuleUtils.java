package utils;

import domain.*;
import enums.*;
import gamemanager.GameManager;
import networking.GameState;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.Optional;

public final class GameRuleUtils {

    private static final Logger LOGGER = Logger.getLogger("Game Rule Utils");

    private GameRuleUtils() {
    }

    /**
     * @param gameMap  the game map
     * @param srcTown  the town where the player is current in
     * @param destTown the town where the player intends to move to
     * @param cards    the travel cards the player intends to use
     * @return on which road the player can move from srcTown to destTown using the cards provided
     * Note: returns null when there are extra cards provided. In other words, the move is only valid
     * with an exact number of exact types of travel cards
     */
    public static Road validateMove(GameMap gameMap, Town srcTown, Town destTown, List<TravelCard> cards) {
        if (cards == null || cards.isEmpty()) {
            return null;
        } else if (!((GameState.instance().getCurrentPhase() == EGRoundPhaseType.MOVE ||
                GameState.instance().getCurrentPhase() == ELRoundPhaseType.MOVE)
                && !cards.isEmpty()
                && GameManager.getInstance().isLocalPlayerTurn())) {
            return null;
        }
        Set<Road> roads = gameMap.getRoadsBetween(srcTown, destTown);

        //TODO: implement elven witch and special transportation counters for Elfengold (seamonster done, magic spell done)
        
        boolean hasWitch = false;
        TravelCard witch = null;
        //check if the player has choose a witch card and has at lease one gold coin.
        for(TravelCard card: cards) {
        	if(card.getType() == TravelCardType.WITCH && GameState.instance().getCurrentPlayer().getGoldCoins() >= 1) {
        		hasWitch = true;
        		witch = card;
        		cards.remove(card);
        	}
        }
        
        for (Road road : roads) {
            // cannot move on a non-river and non-lake road without transportation counter
            if (road.getRegionType() != RegionType.LAKE && road.getRegionType() != RegionType.RIVER
                    && road.numOfTransportationCounter() == 0) {
                continue;
            }
            if (road.getRegionType() == RegionType.LAKE) {
                // lake - two raft cards (three with seamonster)
                int requiredCards = 2;
                if (road.hasObstacle() && !hasWitch) {
                    requiredCards++;
                }

                if (cards.size() == requiredCards && cards.stream().allMatch(card -> card.getType() == TravelCardType.RAFT)) {
                    LOGGER.info("[Lake] Can travel with " + Integer.toString(requiredCards) + " raft cards");
                    if (hasWitch) {
                    	LOGGER.info("Current Player use a witch card to go over obstacle.");
                    	GameState.instance().getCurrentPlayer().removeGoldCoins(1);
                    }
                    return road;
                }
            } else if (road.getRegionType() == RegionType.RIVER) {
                int extraCard = 0;
                if (road.hasObstacle() && !hasWitch) {
                    extraCard = 1;
                }

                if (gameMap.getRoadSource(road).equals(destTown)) {
                    // upriver - two raft cards (three with seamonster)
                    if (cards.size() == 2 + extraCard && cards.stream().allMatch(card -> card.getType() == TravelCardType.RAFT)) {
                        LOGGER.info("[Upriver] Can travel with " + Integer.toString(2 + extraCard) + " raft cards");
                        if (hasWitch) {
                        	LOGGER.info("Current Player use a witch card to go over obstacle.");
                        	GameState.instance().getCurrentPlayer().removeGoldCoins(1);
                        }
                        return road;
                    }
                } else {
                    // downriver - one raft card (two with seamonster)
                    if (cards.size() == 1 + extraCard && cards.get(0).getType() == TravelCardType.RAFT) {
                        LOGGER.info("[Downriver] Can travel with " + Integer.toString(1 + extraCard) + " raft card");
                        if (hasWitch) {
                        	LOGGER.info("Current Player use a witch card to go over obstacle.");
                        	GameState.instance().getCurrentPlayer().removeGoldCoins(1);
                        }
                        return road;
                    }
                }
            } else {
                // If there is a Double Magic Spell, there are two transportation counters. The way it is checked is
                // the same as one, so we just loop through all transportation counters.
                // We don't need to do anything extra for Exchange Magic Spell.
                for (TransportationCounter t : road.getAllTransportationCounters()) {
                    // convert transportation counter type to corresponding card type
                    TravelCardType requiredType = TravelCardType.valueOf(t.getType().name());
                    int requiredNumOfCards = t.getRequiredNumOfUnitsOn(road);
                    if (road.hasObstacle() && !hasWitch) {
                        requiredNumOfCards++;
                    }
                    if (cards.size() == requiredNumOfCards && cards.stream().allMatch(card -> card.getType() == requiredType)) {
                        if (hasWitch) {
                        	LOGGER.info("Current Player use a witch card to go over obstacle.");
                        	GameState.instance().getCurrentPlayer().removeGoldCoins(1);
                        }
                    	return road;
                    }
                }
            }
        }

        // no matching cards, check for Caravan (any three travel cards, or four if there is an obstacle)
        for (Road road : roads) {
            LOGGER.info("No matching cards, checking for Caravan...");
            if (road.getRegionType() == RegionType.LAKE || road.getRegionType() == RegionType.RIVER
                    || road.numOfTransportationCounter() == 0) {
                // caravan cannot move on a lake/river or a road without transportation counter
                continue;
            }
            int requiredNumOfCards = road.hasObstacle()&&hasWitch ? 4 : 3;
            if (cards.size() == requiredNumOfCards) {
                LOGGER.info("Caravan triggered with " + requiredNumOfCards + " cards");
                if (hasWitch) {
                	LOGGER.info("Current Player use a witch card to go over obstacle.");
                	GameState.instance().getCurrentPlayer().removeGoldCoins(1);
                }
                return road;
            }
        }
        if(witch != null && hasWitch) {
        	cards.add(witch);
        }
        return null;
    }

    public static boolean isDrawCountersPhase() {
        return List.of(ELRoundPhaseType.DRAW_COUNTER_ONE,
                ELRoundPhaseType.DRAW_COUNTER_TWO, ELRoundPhaseType.DRAW_COUNTER_THREE)
                .contains(GameState.instance().getCurrentPhase());
    }

    public static boolean isDrawCardsPhase() {
        return List.of(EGRoundPhaseType.DRAW_CARD_ONE,
                EGRoundPhaseType.DRAW_CARD_TWO, EGRoundPhaseType.DRAW_CARD_THREE)
                .contains(GameState.instance().getCurrentPhase());
    }

    public static boolean isElfengoldVariant(GameVariant gameVariant) {
        return ((gameVariant == GameVariant.ELFENGOLD_CLASSIC) || (gameVariant == GameVariant.ELFENGOLD_WITCH) ||
                (gameVariant == GameVariant.ELFENGOLD_RANDOM_GOLD) || (gameVariant == GameVariant.ELFENGOLD_TRAVEL_CARDS));
    }

    /**
     * Be careful! Only call this method after game state is initialized.
     */
    public static boolean isElfengoldVariant() {
        GameVariant gameVariant = GameState.instance().getGameVariant();

        return ((gameVariant == GameVariant.ELFENGOLD_CLASSIC) || (gameVariant == GameVariant.ELFENGOLD_WITCH) ||
                (gameVariant == GameVariant.ELFENGOLD_RANDOM_GOLD) || (gameVariant == GameVariant.ELFENGOLD_TRAVEL_CARDS));
    }

    public static boolean isElfengoldDrawCardsPhase() {
        return List.of(EGRoundPhaseType.DRAW_CARD_ONE,
                EGRoundPhaseType.DRAW_CARD_TWO, EGRoundPhaseType.DRAW_CARD_THREE)
                .contains(GameState.instance().getCurrentPhase());
    }
}
