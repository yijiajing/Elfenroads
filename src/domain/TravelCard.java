package domain;

import enums.TravelCardType;
import loginwindow.MP3Player;
import networking.ActionManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class TravelCard extends CardUnit {

    private TravelCardType type;
    MP3Player track1 = new MP3Player("./assets/Music/0000171.mp3");

    public TravelCard(TravelCardType type, int resizeWidth, int resizeHeight) {
        super(resizeWidth, resizeHeight, "T0" + (type.ordinal() + 1));
        this.type = type;
        this.getDisplay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                track1.play();
                ActionManager.getInstance().addSelectedCard(TravelCard.this);
            }
        });
    }

    public TravelCardType getType() {
        return type;
    }
}
