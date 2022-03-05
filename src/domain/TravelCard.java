package domain;

import enums.TravelCardType;
import networking.ActionManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TravelCard extends CardUnit {

    private TravelCardType type;

    public TravelCard(TravelCardType type, int resizeWidth, int resizeHeight) {
        super(resizeWidth, resizeHeight, "T0" + (type.ordinal() + 1));
        this.type = type;
        this.getDisplay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionManager.getInstance().addSelectedCard(TravelCard.this);
            }
        });
    }

    public TravelCardType getType() {
        return type;
    }
}
