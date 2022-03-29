package domain;

import enums.MagicSpellType;
import enums.CounterUnitType;
import windows.MainFrame;

public class MagicSpell extends CounterUnit{
	
	
	public MagicSpell(MagicSpellType pType, int resizeWidth, int resizeHeight) {
		super(pType, resizeWidth, resizeHeight, pType.toString());//Magic spell pictures are renamed as M0+MagicSpellType.
		super.initializeMouseListener();
	}

	
}
