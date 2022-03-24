package domain;

import enums.MagicSpellType;
import enums.CounterUnitType;
import loginwindow.MainFrame;

public class MagicSpell extends CounterUnit{
	
	
	public MagicSpell(MagicSpellType pType, int resizeWidth, int resizeHeight) {
		super(pType, resizeWidth, resizeHeight, pType.toString());//Magic spell pictures are renamed as M0+MagicSpellType.
		super.initializeMouseListener();
	}

    public static CounterUnit getNew(CounterUnitType pType) {
        assert pType instanceof MagicSpellType;
    	return new MagicSpell((MagicSpellType)pType, MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900);
    }
	
}
