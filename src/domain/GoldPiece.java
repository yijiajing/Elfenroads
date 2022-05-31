package domain;

import enums.GoldPieceType;
import savegames.SerializableGoldPiece;
import windows.MainFrame;

public class GoldPiece extends CounterUnit {
    public GoldPiece(GoldPieceType pType, int resizeWidth, int resizeHeight) {
        super(pType, resizeWidth, resizeHeight, pType.toString());//Magic spell pictures are renamed as M0+MagicSpellType
        super.initializeMouseListener();
    }

    public GoldPiece(SerializableGoldPiece loaded) {
        super(loaded.getType(), MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900, loaded.getType().toString());
    }

}