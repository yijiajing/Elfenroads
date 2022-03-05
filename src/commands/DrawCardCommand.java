package commands;

import domain.GameManager;

public class DrawCardCommand implements GameCommand {

    private int numCards;

    public DrawCardCommand(int numCards) {
        this.numCards = numCards;
    }

    @Override
    public void execute(GameManager manager) {

    }
}
