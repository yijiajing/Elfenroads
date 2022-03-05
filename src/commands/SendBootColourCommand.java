package commands;

import domain.GameManager;
import enums.Colour;

public class SendBootColourCommand implements GameCommand {

    private Colour bootColour;
    private String senderIP;

    public SendBootColourCommand(Colour pBootColour, String senderIP) {
        this.bootColour = pBootColour;
        this.senderIP = senderIP;
    }

    @Override
    public void execute(GameManager manager) {

    }
}
