package domain;

import savegames.SerializableGoldCard;
import windows.MainFrame;

public class GoldCard extends CardUnit {

    public GoldCard(int resizeWidth, int resizeHeight) {
        super(resizeWidth, resizeHeight, "Gold");
    }

    public GoldCard (SerializableGoldCard loaded)
    {
        super(MainFrame.getInstance().getWidth() * 130 / 1440, MainFrame.getInstance().getHeight() * 2 / 10 , "Gold");
    }

}