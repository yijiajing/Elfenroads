/*
 * @(#)GameHud.java
 *
 * Fire in the Sky - A Minueto Demo
 * Copyright (c) 2004 McGill University
 * 3480 University Street, Montreal, Quebec H3A 2A7
 
 * Fire in the sky is a demo of the Minueto graphic API. More information on
 * Minueto can be found at http://minueto.cs.mcgill.ca .
 
 * This game is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This game is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.minueto.sample.fireinthesky;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoStopWatch;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoWindow;

class GameHud {
	
	private ProgressBar playerToughness;
	private ProgressBar bossToughness;
	
	private PlayerSprite player;
	private EnemySprite boss;
	
	private int playerToughnessCache;
	private int bossToughnessCache;
	
	private int score;	
	
	private MinuetoStopWatch frameTimer;
	
	private int frameCount;
	
	private MinuetoFont font;
	
	private MinuetoImage imageTextPlayer;
	private MinuetoImage imageTextBoss;
	private MinuetoImage imageTextLife;
	private MinuetoImage imageTextScore;
	private MinuetoImage imageTextFPS;
	
	private MinuetoImage imageFPS;
	private MinuetoImage imageScore;
	
	private ImageStore imageStore;
	
	public GameHud(ImageStore imageStore, PlayerSprite player) {
	
		this.player = player;
		this.imageStore = imageStore;
	
		this.font = new MinuetoFont("Arial", 12, true, false);
		
		this.imageTextPlayer = new MinuetoText("Player", this.font, MinuetoColor.WHITE);
		this.imageTextBoss = new MinuetoText("Boss", this.font, MinuetoColor.WHITE);
		this.imageTextLife = new MinuetoText("Life", this.font, MinuetoColor.WHITE);
		this.imageTextScore = new MinuetoText("Score", this.font, MinuetoColor.WHITE);
		this.imageTextFPS = new MinuetoText("FPS", this.font, MinuetoColor.WHITE);
	
		this.playerToughnessCache = player.getToughness();
	
		this.playerToughness = new ProgressBar(player.getMaxToughness(), this.playerToughnessCache, 300, this.imageTextPlayer.getHeight(), new MinuetoColor(255,255,0));
		this.bossToughness = new ProgressBar(100, 0, 300, this.imageTextBoss.getHeight(), MinuetoColor.RED);
		
		this.frameCount = 0;
		this.frameTimer = new MinuetoStopWatch();
		this.frameTimer.start();
		this.imageFPS = new MinuetoText( "0",this.font, MinuetoColor.WHITE);
		
		this.score = 0;
		this.updateScore(0);
	}
	
	public void updateScore(int value) {
	
		this.score = this.score + value;
		this.imageScore = new MinuetoText( this.score + "",this.font, MinuetoColor.WHITE);
	}
	
	
	public void setBoss(EnemySprite boss) {
		
		this.boss = boss;
		
		if (boss != null) {			
			this.bossToughnessCache = boss.getToughness();
			this.bossToughness = new ProgressBar(boss.getMaxToughness(), this.bossToughnessCache, 300, this.imageTextBoss.getHeight(), new MinuetoColor(255,0,0));
		} else {
			this.bossToughness = new ProgressBar(100, 0, 300, this.imageTextBoss.getHeight(), MinuetoColor.RED);
		}
		
	}
	
	public void draw(MinuetoWindow window, int x, int y) {
	
		double frameRate;
		int max;
		
		frameCount++;
	
		if (frameCount == 20) {
			this.frameTimer.stop();
				
			frameRate =  (double)20000/(double)this.frameTimer.getTime();
				
			this.imageFPS = new MinuetoText( frameRate +"" ,this.font, MinuetoColor.WHITE);
			this.frameCount = 0;
			this.frameTimer.reset();
			this.frameTimer.start();

		}
	
		if (this.playerToughnessCache != player.getToughness()) {
			this.playerToughnessCache = player.getToughness();
			this.playerToughness.update(this.playerToughnessCache);
		}
		
		if (boss != null) {
		
			if (this.bossToughnessCache != boss.getToughness()) {
				this.bossToughnessCache = boss.getToughness();
				this.bossToughness.update(this.bossToughnessCache);
			}
		}
		
		window.draw(this.imageTextPlayer, x+5, y+5);
		this.playerToughness.draw(window, x+75, y+5);
		
		window.draw(this.imageTextBoss, x+5, y+28);
		this.bossToughness.draw(window, x+75, y+28);
		
		window.draw(this.imageTextFPS, x+400, y+5);
		window.draw(this.imageFPS, x+430, y+5);
		window.draw(this.imageTextScore, x+600, y+5);
		window.draw(this.imageScore, x+650, y+5);
		
		window.draw(this.imageTextLife, x+600, y+28);
		
		max = this.player.getLifeLeft();
		for (int i = 0; i < max; i++) {
			window.draw(this.imageStore.getImage(ImageStore.PLAYER_LIFE), x+630+(i*30), y+20);
			
		}
		
	
	}
	
}

class ProgressBar {
	
	private int maxValue;
	private int height;
	private int width;
	
	MinuetoImage imageBox;
	MinuetoImage imageBar;	
	MinuetoImage imageResult;
	
	
	public ProgressBar(int maxValue, int value, int width, int height, MinuetoColor fillColor) {
	
		this.maxValue = maxValue;
		this.height = height;
		this.width = width;
		
		this.imageBox = new MinuetoRectangle(this.width, this.height, MinuetoColor.BLACK, true);
		this.imageBox.draw(new MinuetoRectangle(this.width, this.height, MinuetoColor.WHITE, false),0,0);
		this.imageBar = new MinuetoRectangle(this.width-3, this.height-3, fillColor, true);
		this.imageResult = new MinuetoImage(this.width, this.height);
				
		this.update(value);
	}
	
	public void update(int value) {
		
		int sizeBar;
				
		this.imageResult.draw(this.imageBox, 0, 0);
				
		if (value != 0) {
			sizeBar = (value*this.imageBar.getWidth())/this.maxValue;
			this.imageResult.draw(this.imageBar.crop(0,0,sizeBar, this.imageBar.getHeight()), 2,2);		
			
		}
	}
	
	public void draw(MinuetoWindow window, int x, int y) {
		
		window.draw(this.imageResult, x, y);
	}
}