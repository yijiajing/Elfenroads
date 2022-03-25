package enums;

public enum EGRoundPhaseType implements RoundPhaseType {
    DRAW_CARD_ONE,
    DRAW_CARD_TWO,
    DRAW_CARD_THREE,
    // distribute gold coins and draw tokens and counters are automatically done before this phase
    CHOOSE_FACE_UP,
    AUCTION,
    PLAN_ROUTES,
    MOVE,
    RETURN_COUNTERS;
}
