/*
 * @(#)EnemyGenerator.java
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

class EnemyGenerator {
	
	private Sprite player;
	private ImageStore imageStore;
	private MusicStore musicStore;
	private BulletGenerator bulletGenerator;
	private LinkedList<EnemySprite> enemyList;	
	
	public EnemyGenerator(Sprite player, ImageStore imageStore, 
			MusicStore musicStore, 
			LinkedList<EnemySprite> lilEnemyList, 
			BulletGenerator bulletGenerator) {
		
		this.player = player;
		this.imageStore = imageStore;
		this.musicStore = musicStore;
		this.bulletGenerator = bulletGenerator;
		this.enemyList = lilEnemyList;
	}
	
	public EnemySprite makePassiveEnemy(int x, int y) {
		
		EnemySprite sprTempSprite = makeSmallEnemy(25);
		sprTempSprite.setBehavior(new EnemyBehaviorPassive(sprTempSprite));
		sprTempSprite.setPosition(x, y);		
		this.enemyList.add(sprTempSprite);
		
		for (int i = 0; i < 16; i++) {
			sprTempSprite.addFrame(this.imageStore.getImage(ImageStore.ENEMY_PLANE_SMALL1+i));
		}
		
		return sprTempSprite;
		
	}
	
	public EnemySprite makeKamikazeEnemy(int x, int y) {
		
		EnemySprite sprTempSprite = makeSmallEnemy(150);
		sprTempSprite.setBehavior(new EnemyBehaviorKamikaze(sprTempSprite, this.player));
		sprTempSprite.setPosition(x, y);		
		
		for (int i = 0; i < 16; i++) {
			sprTempSprite.addFrame(this.imageStore.getImage(ImageStore.ENEMY_PLANE_SMALL2+i));
		}		
		
		this.enemyList.add(sprTempSprite);
		
		return sprTempSprite;
		
	}
	
	public EnemySprite makeShootingEnemy(int x, int y) {
		
		EnemySprite sprTempSprite = makeSmallEnemy(100);
		sprTempSprite.setBehavior(new EnemyBehaviorShooter(sprTempSprite, this.player, this.bulletGenerator));
		sprTempSprite.setPosition(x, y);		
		
		for (int i = 0; i < 16; i++) {
			sprTempSprite.addFrame(this.imageStore.getImage(ImageStore.ENEMY_PLANE_SMALL1+i));
		}
		
		this.enemyList.add(sprTempSprite);
		
		return sprTempSprite;
	}
	
	public EnemySprite makeSmartShootingEnemy(int x, int y) {
		
		EnemySprite sprTempSprite = makeSmallEnemy(100);
		sprTempSprite.setBehavior(new EnemyBehaviorSmartShooter(sprTempSprite, this.player, this.bulletGenerator));
		sprTempSprite.setPosition(x, y);
		
		for (int i = 0; i < 16; i++) {
			sprTempSprite.addFrame(this.imageStore.getImage(ImageStore.ENEMY_PLANE_SMALL3+i));
		}
		
		this.enemyList.add(sprTempSprite);
		
		return sprTempSprite;
	}
	
	public EnemySprite makeBossEnemy(int x, int y) {
		
		EnemySprite sprTempSprite = new EnemySprite(2000, 1500, 98, 32, 12, musicStore);
		sprTempSprite.setPosition(x, y);
		
		for (int i = 0; i < 16; i++) {
			sprTempSprite.addFrame(this.imageStore.getImage(ImageStore.ENEMY_PLANE_BOSS+i));
		}
				
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG1));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG2));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG3));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG4));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG5));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_BIG6));
		
		//sprTempSprite.setSound(this.musicStore.getHit(), this.musicStore.getExplosion());	
				
		sprTempSprite.setBehavior(new EnemyBehaviorBoss(sprTempSprite, this.player, this.bulletGenerator));
				
		this.enemyList.add(sprTempSprite);
		
		return sprTempSprite;	
	}
	
	private EnemySprite makeSmallEnemy(int pointValue) {
		
		EnemySprite sprTempSprite = new EnemySprite(pointValue, 10,32,15, 6, musicStore);
				
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_SMALL1));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_SMALL2));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_SMALL3));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_SMALL4));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_SMALL5));
		sprTempSprite.addExplosionFrame(this.imageStore.getImage(ImageStore.EXPLOSION_SMALL6));
		
		//sprTempSprite.setSound(this.musicStore.getHit(), this.musicStore.getExplosion());	
		
		return sprTempSprite;
		
	}
	
	
}