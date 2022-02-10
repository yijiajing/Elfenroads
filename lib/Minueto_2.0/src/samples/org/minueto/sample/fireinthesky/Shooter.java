/*
 * @(#)Shooter.java
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

import java.util.LinkedList;

import org.minueto.MinuetoEventQueue;
import org.minueto.MinuetoStopWatch;
import org.minueto.window.MinuetoWindow;

public class Shooter {

	private MinuetoWindow window;	
	private MinuetoEventQueue eventQueue;
	
	private ShooterKeyboard shooterKeyboard;
	
	private int mapPositionX;
	private int mapPositionY;

	private GameMap gameMap;

	private PlayerSprite player;

	private int coolDownCounter;
	
	private LinkedList<EnemySprite> enemyList;
	private LinkedList<BulletSprite> bulletList;
	private LinkedList<BulletSprite> enemyBulletList;	
	private LinkedList<PowerUpSprite> powerUp;
	
	private ImageStore imageStore;
	private MusicStore musicStore;
	
	private EnemyGenerator enemyGenerator;
	private BulletGenerator bulletGenerator;
	private PowerUpGenerator powerUpGenerator;
	
	private LevelMap levelMap;
	private GameHud hud;
	
	@SuppressWarnings("unused")
	private final static int SCREEN_WIDTH = 800;
	private final static int SCREEN_HEIGHT = 600;
	private final static int MAP_HEIGHT = 400*64;
	private final static int MAP_WIDTH = 13*64;
		
	private MinuetoStopWatch frameLock;
	
	private boolean gameOver;
	private boolean missionComplete;
	private int gameOverCountDown;
	private boolean exit;
	
	public Shooter(MinuetoWindow window, ImageStore imageStore, MusicStore musicStore) {

		this.window = window;
		this.imageStore = imageStore;
		this.musicStore = musicStore;

		this.gameMap = new GameMap(MAP_WIDTH/64, MAP_HEIGHT/64, 64);
		this.mapPositionX = 0;
		this.mapPositionY = MAP_HEIGHT - SCREEN_HEIGHT;
		
		this.enemyList = new LinkedList<EnemySprite>();
		this.bulletList = new LinkedList<BulletSprite>();
		this.enemyBulletList = new LinkedList<BulletSprite>();
		this.powerUp = new LinkedList<PowerUpSprite>();

		this.bulletGenerator = new BulletGenerator(this.imageStore, this.musicStore, this.bulletList, this.enemyBulletList);

		this.player = new PlayerSprite(100,64,30, this.bulletGenerator, musicStore);
		this.player.addFrame(this.imageStore.getImage(ImageStore.PLAYER_PLANE1));
		this.player.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG1));
		this.player.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG2));
		this.player.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG3));
		this.player.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG4));
		this.player.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG5));
		this.player.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG6));
		//this.player.setSound(this.musicStore.getHit(), this.musicStore.getExplosion());		
		this.player.setPosition(320,MAP_HEIGHT - 50);
		this.player.setSpeed(0,-2);			
		
		this.enemyGenerator = new EnemyGenerator(this.player, this.imageStore, this.musicStore, this.enemyList, this.bulletGenerator);
		this.powerUpGenerator = new PowerUpGenerator(this.powerUp, this.imageStore);
		
		this.hud = new GameHud(this.imageStore, this.player);
		
		this.levelMap = new LevelMap(this.enemyGenerator, this.powerUpGenerator, this.hud);
				
		this.eventQueue = new MinuetoEventQueue();
		this.shooterKeyboard = new ShooterKeyboard();
		this.window.registerKeyboardHandler(this.shooterKeyboard, this.eventQueue);
		
		this.coolDownCounter = 0;		
		
		this.frameLock = new MinuetoStopWatch();
		
		this.gameOver = false;
		this.missionComplete = false;
		this.gameOverCountDown = 0;
		this.exit = false;
	}

	public void gameLoop() {
		
		this.window.setVisible(true);
		this.window.setCursorVisible(false);
		this.frameLock.start();
		
		this.musicStore.stopMidi("intro");
		this.musicStore.playMidi("attack");  

		while(this.exit == false) {
			
			// Locks the framerate to 50 FPS. This helps use calibrate the speed
			// the rover. We can achieve the framelock by forcing the system
			// to wait 20 millisec between each update.
			while (this.frameLock.getTime() < 20) {
			
				/*try {
					Thread.yield();
				} catch (Exception e) { };
				*/
			} 			
			
			this.frameLock.reset();
			this.frameLock.start();

			this.handleInput();
			this.updateState();
			this.drawGraphics();
		
			this.window.render();
			
			Thread.yield();

		}
		
		this.musicStore.stopMidi("intro");
		this.musicStore.stopMidi("attack");
	}
	
	public void drawGraphics() {
	
		Sprite sprTempSprite;		
	
		this.gameMap.draw(this.window, this.imageStore.getMapImage(), this.mapPositionX, this.mapPositionY);
		this.player.draw(this.window, this.mapPositionX, this.mapPositionY);
		
		for(int i = 0; i < this.powerUp.size(); i++) {
				
			sprTempSprite = (Sprite)this.powerUp.get(i);
			sprTempSprite.draw(this.window, this.mapPositionX, this.mapPositionY);			
		}
		
		for(int i = 0; i < this.enemyList.size(); i++) {
				
			sprTempSprite = (Sprite)this.enemyList.get(i);
			sprTempSprite.draw(this.window, this.mapPositionX, this.mapPositionY);			
		}
		
		for(int i = 0; i < this.bulletList.size(); i++) {
				
			sprTempSprite = (Sprite)this.bulletList.get(i);
			sprTempSprite.draw(this.window, this.mapPositionX, this.mapPositionY);			
		}
		
		for(int i = 0; i < this.enemyBulletList.size(); i++) {
				
			sprTempSprite = (Sprite)this.enemyBulletList.get(i);
			sprTempSprite.draw(this.window, this.mapPositionX, this.mapPositionY);			
		}
		
		this.hud.draw(this.window, 0, 0);
		
		if (this.gameOver == true) {
						
			this.window.draw(this.imageStore.getImage(ImageStore.GAMEOVER), 215, 254);
		}
		
		if (this.missionComplete == true) {
		
			this.window.draw(this.imageStore.getImage(ImageStore.MISSION_SUCCESS), 165, 254);
		}
		
		/*int lastPositionX = this.player.getPositionX() - this.mapPositionX;
		int lastPositionY = this.player.getPositionY() - this.mapPositionY;
		
		window.drawLine(MinuetoColor.BLUE, lastPositionX-10, 
				lastPositionY, lastPositionX+10, lastPositionY);
		window.drawLine(MinuetoColor.BLUE, lastPositionX, 
				lastPositionY-10, lastPositionX, lastPositionY+10);*/
	}
	
	public void updateState() {
	
		PowerUpSprite sprTempPowerUp;
		EnemySprite sprTempSprite;
		BulletSprite sprTempBullet;
		boolean bRet;
	
		this.mapPositionY--;
		this.mapPositionY--;
		
		if (this.mapPositionY == 0) {
			this.mapPositionY = MAP_HEIGHT - SCREEN_HEIGHT;
			this.player.setPosition(320,MAP_HEIGHT - 50);
		}
		
		this.levelMap.tick(mapPositionY);

		if (this.player.isDead()) { this.player.revive(); }

		bRet = this.player.isInScreen(this.window, this.mapPositionX, this.mapPositionY);
		if (bRet == false) {
				this.player.setSpeed(0,-2);
		}

		this.player.tick();
		
		for(int i = 0; i < this.powerUp.size(); i++) {
			sprTempPowerUp = (PowerUpSprite)this.powerUp.get(i);
			
			bRet = sprTempPowerUp.isInScreen(this.window, this.mapPositionX, this.mapPositionY);
			
			if (this.player.isCollision(sprTempPowerUp)) {
				sprTempPowerUp.execute(this.player);
				this.powerUp.remove(sprTempPowerUp);										
			} else if (bRet == false) {
				this.powerUp.remove(sprTempPowerUp);
				System.out.println("Tick!");		
			}
			else {			
				sprTempPowerUp.tick();
			}
		}
		
		for(int i = 0; i < this.enemyList.size(); i++) {
			sprTempSprite = (EnemySprite)this.enemyList.get(i);
			
			bRet = sprTempSprite.isInScreen(this.window, this.mapPositionX, this.mapPositionY);
			
			if (this.player.isCollision(sprTempSprite)) {
					if (!sprTempSprite.isExploding()) {
						this.player.hit(50);
						sprTempSprite.hit(50);	
					}
										
			}
			
			if ( sprTempSprite.isDead() ) {
				this.enemyList.remove(sprTempSprite);
				this.hud.updateScore(sprTempSprite.getPointValue());
			} else if (bRet == false) {
				sprTempSprite.kill(); 
				this.enemyList.remove(sprTempSprite);
			}
			else {			
				sprTempSprite.tick();
			}
		}

		for(int i = 0; i < this.bulletList.size(); i++) {
			sprTempBullet = (BulletSprite)this.bulletList.get(i);
			sprTempBullet.tick();
			
			bRet = sprTempBullet.isInScreen(this.window, this.mapPositionX, this.mapPositionY);
			
			if (bRet == false) { 
				this.bulletList.remove(sprTempBullet); 
				i--;
			} else {
				for(int j = 0; j < this.enemyList.size(); j++) {
					
					sprTempSprite = (EnemySprite)this.enemyList.get(j);
					
					if (sprTempSprite.isCollision(sprTempBullet)) {
						
						sprTempSprite.hit(sprTempBullet.getDamage());
						this.bulletList.remove(sprTempBullet);						
					}
				}	
			}						
			
		}
		
		for(int i = 0; i < this.enemyBulletList.size(); i++) {
			sprTempBullet = (BulletSprite)this.enemyBulletList.get(i);
			sprTempBullet.tick();
			
			bRet = sprTempBullet.isInScreen(this.window, this.mapPositionX, this.mapPositionY);
			
			if (bRet == false) { 
				this.enemyBulletList.remove(sprTempBullet); 
				i--;
			} else {
					
				if (this.player.isCollision(sprTempBullet)) {
						
					this.player.hit(sprTempBullet.getDamage());
					this.enemyBulletList.remove(sprTempBullet); 
					i--;
				}
					
			}						
			
		}				
		
		if (this.gameOverCountDown > 0) {
			this.gameOverCountDown--;			
		}
		
		if ((this.gameOverCountDown == 0) && 
			((this.gameOver == true) || (this.missionComplete == true)) ) {
			this.exit = true;
		}
		
		if ((this.player.isDead()) && (this.player.getLifeLeft() ==0)) {
			
			if (this.gameOver == false) {
				this.gameOverCountDown = 250;
			}
			
			this.gameOver = true;
		}
		
		if (this.levelMap.isMissionSuccess() == true) {
		
			if (this.missionComplete == false) {
				this.gameOverCountDown = 250;
			}
		
			this.missionComplete = true;
		}
	}
	
	public void handleInput() {
	
		while(this.eventQueue.hasNext()) {
			this.eventQueue.handle();
		}
		
		this.coolDownCounter--;
		
		if ( !this.player.isExploding() && !this.player.isDead()) {
		
			if (this.shooterKeyboard.isKeyLeft()) {
				this.player.setSpeedX(-4);
			} else if (this.shooterKeyboard.isKeyRight()) {
				this.player.setSpeedX(4);
			} else {
				this.player.setSpeedX(0);
			}
			
			if (this.shooterKeyboard.isKeyUp()) {
				this.player.setSpeedY(-6);
			} else if (this.shooterKeyboard.isKeyDown()) {
				this.player.setSpeedY(2);
			} else {
				this.player.setSpeedY(-2);
			}	
			
			//System.out.println(this.player.getSpeedX() + " " + this.player.getSpeedY());
					
			if (this.shooterKeyboard.isKeySpace()) {
				
				if (this.coolDownCounter < 0) {
					
					this.player.shoot();					
					this.coolDownCounter = 16;
				}
			} else {
			
				this.coolDownCounter = -1;
			}
		} else {
			this.player.setSpeed(0,-2);
		}
	}
	



}
