package domain;

import enums.CounterUnitType;
import enums.GoldPieceType;
import loginwindow.MainFrame;

public class GoldPiece extends CounterUnit {
	public GoldPiece(GoldPieceType pType, int resizeWidth, int resizeHeight) {
		super(resizeWidth, resizeHeight, pType.toString());//Magic spell pictures are renamed as M0+MagicSpellType
		super.setType(pType);
		super.initializeMouseListener();
	}

    public static GoldPiece getNew(CounterUnitType pType) {
        assert pType instanceof GoldPieceType;
    	return new GoldPiece((GoldPieceType)pType, MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900);
    }
}
