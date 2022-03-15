package domain;

import enums.MagicSpellType;
import commands.DrawCounterCommand;
import enums.CounterType;
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
	
	private MagicSpellType aType;
	private MP3Player track1 = new MP3Player("./assets/Music/0000171.mp3");
	
	public MagicSpell(MagicSpellType pType, int resizeWidth, int resizeHeight) {
		super(resizeWidth, resizeHeight, pType.ordinal() + 10);//Magic spell pictures are renamed as M10 and M11
		aType = pType;
		
		super.initializeMouseListener();
	}


	
}
