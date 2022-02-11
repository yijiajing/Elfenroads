/*
 * @(#)LevelMap.java
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

import java.util.Random;

class LevelMap {
	
	private EnemyGenerator enemyGenerator;
	private PowerUpGenerator powerUpGenerator;
	private GameHud hud;
	
	private EnemySprite boss;
		
	private Random randomGenerator;
	
	private boolean bossBattle;
	private boolean missionSuccess;
	
	public LevelMap(EnemyGenerator enemyGenerator, PowerUpGenerator powerUpGenerator, GameHud hud) {
		
		this.randomGenerator = new Random();
	
		this.hud = hud;
		
		this.enemyGenerator = enemyGenerator;
		this.powerUpGenerator = powerUpGenerator;
		
		this.boss = null;
		
		this.bossBattle = false;
		this.missionSuccess = false;
	}
	
	public void tick(int y) {

		if ( y > 25000 ) {
		
		} else if ( y > 17000 ) {
		
			if ((y%100) == 0) {
				this.generateWing(y);
			}
			
			if ((y%1499) == 0) {
				this.generatePowerUp(y);
			}

		} else {
		
			if (this.bossBattle == false) {
			
				this.bossBattle = true;
				this.boss = this.enemyGenerator.makeBossEnemy(this.randomGenerator.nextInt(500)+100,y+50);
				this.hud.setBoss(this.boss);		
			}
		}

		if ((this.bossBattle == true) && (this.boss.isDead())) {							
				this.missionSuccess = true;	
		}
	}
	
	public void generateWing(int y) {

		int iEvent = this.randomGenerator.nextInt(6);
			
		switch (iEvent) {
				
			case 0:
				this.generateKamikazeWingLeft(y);
				break;
			case 1:
				this.generatePassiveWing(y);
				break;				
			case 2:
				break;				
			case 3:
				this.generateShooterWing(y);
				break;				
			case 4:
				this.generateSmartWing(y);
				break;
			case 5:
				this.generateKamikazeWingRight(y);
				break;
		}
	}
	
	public void generateKamikazeWingLeft(int y) {
	
		this.enemyGenerator.makeKamikazeEnemy(84,y+5);
		this.enemyGenerator.makeKamikazeEnemy(52,y+37);
		this.enemyGenerator.makeKamikazeEnemy(20,y+69);
	}
	
	public void generateKamikazeWingRight(int y) {
	
		this.enemyGenerator.makeKamikazeEnemy(716,y+5);
		this.enemyGenerator.makeKamikazeEnemy(748,y+37);
		this.enemyGenerator.makeKamikazeEnemy(780,y+69);
	}
	
	public void generatePassiveWing(int y) {
	
		this.enemyGenerator.makePassiveEnemy(this.randomGenerator.nextInt(760),y+5);
		this.enemyGenerator.makePassiveEnemy(this.randomGenerator.nextInt(760),y+5);
		this.enemyGenerator.makePassiveEnemy(this.randomGenerator.nextInt(760),y+5);
	}
	
	public void generateShooterWing(int y) {
	
		this.enemyGenerator.makeShootingEnemy(this.randomGenerator.nextInt(760),y+5);
		this.enemyGenerator.makeShootingEnemy(this.randomGenerator.nextInt(760),y+5);
	}
	
	public void generateSmartWing(int y) {
	
		this.enemyGenerator.makeSmartShootingEnemy(this.randomGenerator.nextInt(760),y+5);
		this.enemyGenerator.makeSmartShootingEnemy(this.randomGenerator.nextInt(760),y+5);
	}
	
	public void generatePowerUp(int y) {
	
		int iEvent = this.randomGenerator.nextInt(11);
		
		if (iEvent < 6) {
			this.powerUpGenerator.makePowerUpWeapon(this.randomGenerator.nextInt(760),y+5,2000);
		} else if (iEvent < 10) {
			this.powerUpGenerator.makePowerUpArmor(this.randomGenerator.nextInt(760),y+5,50);
		} else {
			this.powerUpGenerator.makePowerUpLife(this.randomGenerator.nextInt(760),y+5,1);
		}
	}
	
	public boolean isMissionSuccess() {
	
		return this.missionSuccess;
	}	
	
}