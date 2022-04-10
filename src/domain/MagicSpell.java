package domain;

import enums.MagicSpellType;
import enums.CounterUnitType;
import windows.MainFrame;
import savegames.SerializableMagicSpell;
import windows.MainFrame;

public class MagicSpell extends CounterUnit{
	
	
	public MagicSpell(MagicSpellType pType, int resizeWidth, int resizeHeight) {
		super(pType, resizeWidth, resizeHeight, pType.toString());//Magic spell pictures are renamed as M0+MagicSpellType.
		super.initializeMouseListener();
	}

  public static CounterUnit getNew(CounterUnitType pType) {
        assert pType instanceof MagicSpellType;
    	return new MagicSpell((MagicSpellType)pType, MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900);
    }

	public MagicSpell(SerializableMagicSpell loaded)
	{
		super (loaded.getType(), MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900, loaded.getType().toString());
		super.initializeMouseListener();
	}

	@Override
	public String toString() {
		return "MagicSpell{" +
				"type=" + getType() +
				"isSecret=" + isSecret +
				'}';
	}
}
