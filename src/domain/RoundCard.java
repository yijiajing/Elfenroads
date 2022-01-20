package domain;

public class RoundCard extends CardUnit {

    private int roundNum;

    public RoundCard(int roundNum, int resizeWidth, int resizeHeight) {
        super(resizeWidth, resizeHeight, "R" + roundNum);
    }

}
