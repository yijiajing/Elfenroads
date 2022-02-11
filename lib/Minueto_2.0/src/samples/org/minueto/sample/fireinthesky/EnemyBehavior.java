/*
 * @(#)EnemyBehavior.java
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

import java.util.*;

abstract class EnemyBehavior {
	
	public abstract void tick();
	
}

class EnemyBehaviorPassive extends EnemyBehavior {
	
	private EnemySprite enemySprite;
	private int distance;
	private int distanceMax;
	
	private Random randomGenerator;
	
	public EnemyBehaviorPassive(EnemySprite enemySprite) {
	
		this.randomGenerator = new Random();		
	
		this.enemySprite = enemySprite;
		enemySprite.setSpeed(0,2);
		
		this.distanceMax = this.randomGenerator.nextInt(250) + 100;
		this.distance = 0;
		
	}
	
	public void tick() {
		
		if (this.distance > this.distanceMax) {
			
			enemySprite.setSpeed(0,-2);
			
		} else {
			
			this.distance = this.distance + 3;
		}
						
	}
}

class EnemyBehaviorKamikaze extends EnemyBehavior {
	
	private EnemySprite enemySprite;
	private Sprite player;
	
	private Random randomGenerator;
	
	public EnemyBehaviorKamikaze(EnemySprite enemySprite, Sprite player) {
	
		this.randomGenerator = new Random();		
	
		this.enemySprite = enemySprite;
		this.player = player;
		enemySprite.setSpeed(0,1);
		
	}
	
	public void tick() {
			
		int oddsTurning = 10;
		int resultTurning = this.randomGenerator.nextInt(100);
		
		if (resultTurning < oddsTurning) {
			
			int distanceX = (int)player.getPositionX() - (int)enemySprite.getPositionX();
			
			if (distanceX > 32) {
				enemySprite.setSpeed(3,2);
			} else if (distanceX < -32) {
				enemySprite.setSpeed(-3,2);						
			} else {
				enemySprite.setSpeed(0,2);			
			}
		}
	}
}

class EnemyBehaviorShooter extends EnemyBehavior {
	
	private EnemySprite enemySprite;
	private Sprite player;
	
	private BulletGenerator bulletGenerator;
	
	private Random randomGenerator;

	private int distance;
	private int distanceMax;
	
	public EnemyBehaviorShooter(EnemySprite enemySprite, Sprite player, BulletGenerator bulletGenerator) {
	
		this.randomGenerator = new Random();
	
		this.enemySprite = enemySprite;
		this.player = player;
		
		this.bulletGenerator = bulletGenerator;
		
		enemySprite.setSpeed(0,2);
		
		this.distanceMax = this.randomGenerator.nextInt(200) + 100;
		this.distance = 0;
		
	}
	
	public void tick() {
				
		int oddsShooting = 1;
		int resultShooting = this.randomGenerator.nextInt(500);
		
		int distanceX = (int)player.getPositionX() - (int)enemySprite.getPositionX();
			
		if (distanceX > 50) {
			oddsShooting = 5;
		} else if (distanceX < -50) {
			oddsShooting = 5;
		}
		
		if (resultShooting < oddsShooting) {
			this.bulletGenerator.makeSmallBullet(enemySprite.getShootX(), enemySprite.getShootY());
		}
		
		if (this.distance > this.distanceMax) {
			
			enemySprite.setSpeed(0,-2);
			
		} else {
			
			this.distance = this.distance + 3;
		}
		
								
	}
	
}

class EnemyBehaviorSmartShooter extends EnemyBehavior {
	
	private EnemySprite enemySprite;
	private Sprite player;
	
	private BulletGenerator bulletGenerator;
	
	private Random randomGenerator;

	private int distance;
	private int distanceMax;
	
	public EnemyBehaviorSmartShooter(EnemySprite enemySprite, Sprite player, BulletGenerator bulletGenerator) {
	
		this.randomGenerator = new Random();
	
		this.enemySprite = enemySprite;
		this.player = player;
		
		this.bulletGenerator = bulletGenerator;
		
		enemySprite.setSpeed(0,2);
		
		this.distanceMax = this.randomGenerator.nextInt(200) + 100;
		this.distance = 0;
		
	}
	
	public void tick() {
				
		int oddsShooting = 3;
		int resultShooting = this.randomGenerator.nextInt(500);
				
		if (resultShooting < oddsShooting) {
			this.bulletGenerator.makeSmartSmallBullet(
				this.enemySprite.getShootX(), this.enemySprite.getShootY(),
				this.player.getPositionX(), this.player.getPositionY());
		}
		
		if (this.distance > this.distanceMax) {
			
			enemySprite.setSpeed(0,-2);
			
		} else {
			
			this.distance = this.distance + 3;
		}
		
								
	}
	
}

class EnemyBehaviorBoss extends EnemyBehavior {
	
	private EnemySprite enemySprite;
	private Sprite player;
	
	private BulletGenerator bulletGenerator;
	
	private Random randomGenerator;

	private int time;
	private int maxTime;
		
	private int speedX;
	private int speedY;
	
	private int relativeY;
	
		
	public EnemyBehaviorBoss(EnemySprite enemySprite, Sprite player, BulletGenerator bulletGenerator) {
	
		this.randomGenerator = new Random();
	
		this.enemySprite = enemySprite;
		this.player = player;
		
		this.bulletGenerator = bulletGenerator;
				
		this.time = 0;
		
		this.relativeY = enemySprite.getPositionY() - 50;

		this.speedX = this.randomGenerator.nextInt(7) - 3;
		this.speedY = this.randomGenerator.nextInt(7) - 3;

		this.enemySprite.setSpeed(this.speedX, this.speedY-2);

		this.maxTime = this.randomGenerator.nextInt(10) * 25;
		this.time = 0;	
	}
	
	public void tick() {
		
		int oddsShooting = 3;
		int resultShooting = this.randomGenerator.nextInt(100);
				
		if (resultShooting < oddsShooting) {
			this.bulletGenerator.makeSmartSmallBullet(
				this.enemySprite.getShootX(), this.enemySprite.getShootY(),
				this.player.getPositionX(), this.player.getPositionY());
		}
		
		this.time++;
		this.relativeY = this.relativeY - 2;
		
		if ( (this.time > this.maxTime) ) {
			
			this.speedX = this.randomGenerator.nextInt(7) - 3;
			this.speedY = this.randomGenerator.nextInt(7) - 3;		
			this.enemySprite.setSpeed(this.speedX, this.speedY-2);						
			this.maxTime = this.randomGenerator.nextInt(10) * 25;
			this.time = 0;
			
			return;
		}
		
		if (this.time > 0) {
					
			if ((enemySprite.getPositionX()+this.speedX) < 100) {
				
				this.speedX = this.randomGenerator.nextInt(3) + 1;						
				this.enemySprite.setSpeed(this.speedX, -2);						
				this.maxTime = this.randomGenerator.nextInt(10) * 25;
				this.time = -30;
				
				//System.out.println("Hit the left");
				
				return;
				
			}
			
			if ((enemySprite.getPositionY()+this.speedY) < (this.relativeY+100)) {
							
				this.speedY = this.randomGenerator.nextInt(3) + 1;		
				this.enemySprite.setSpeed(0, this.speedY-2);						
				this.maxTime = this.randomGenerator.nextInt(10) * 25;
				this.time = -30;
				
				//System.out.println("Hit the top");
				
				return;
							
			}
			
			if ((enemySprite.getPositionX()+this.speedX) > 700) {
				
				this.speedX = this.randomGenerator.nextInt(3) - 3;				
				this.enemySprite.setSpeed(this.speedX, -2);						
				this.maxTime = this.randomGenerator.nextInt(10) * 25;
				this.time = -30;
				
				//System.out.println("Hit the right");
				
				return;
			}
			
			if ((enemySprite.getPositionY()+this.speedY) > (this.relativeY+400)) {
							
				this.speedY = this.randomGenerator.nextInt(3) - 3;		
				this.enemySprite.setSpeed(0, this.speedY-2);						
				this.maxTime = this.randomGenerator.nextInt(10) * 25;
				this.time = -30;
				
				//System.out.println("Hit the bottom");
				
				return;	
							
			}
		}
								
	}
	
}