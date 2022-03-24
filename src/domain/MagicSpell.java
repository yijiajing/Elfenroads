package domain;

import enums.MagicSpellType;
import commands.DrawCounterCommand;
import enums.CounterType;
import enums.CounterUnitType;
import enums.RegionType;
import enums.RoundPhaseType;
import loginwindow.MP3Player;
import loginwindow.MainFrame;
import networking.ActionManager;
import networking.GameState;
import panel.GameScreen;
import utils.GameRuleUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

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
