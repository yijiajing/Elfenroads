package domain;

import enums.CounterUnitType;
import enums.GoldPieceType;
import windows.MainFrame;

public class GoldPiece extends CounterUnit {
	public GoldPiece(GoldPieceType pType, int resizeWidth, int resizeHeight) {
		super(pType, resizeWidth, resizeHeight, pType.toString());//Magic spell pictures are renamed as M0+MagicSpellType
		super.initializeMouseListener();
	}


}
