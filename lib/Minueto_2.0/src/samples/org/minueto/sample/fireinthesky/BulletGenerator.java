/*
 * @(#)BulletGenerator.java
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

class BulletGenerator {
	
	private LinkedList<BulletSprite> bulletList;
	private LinkedList<BulletSprite> enemyBulletList;
	
	private ImageStore imageStore;
	private MusicStore musicStore;
	
	public BulletGenerator(ImageStore imageStore, MusicStore musicStore, 
			LinkedList<BulletSprite> lilBulletList, 
			LinkedList<BulletSprite> lilEnemyBulletList) {
		
		this.imageStore = imageStore;
		this.musicStore = musicStore;
		
		this.bulletList = lilBulletList;
		this.enemyBulletList = lilEnemyBulletList;
		
	}
	
	public void makePlayerSmallBullet(int x, int y) {
	
		BulletSprite bullet = new BulletSprite(50, 32, 6);
		bullet.addFrame(this.imageStore.getImage(ImageStore.PLAYER_BULLET));
		bullet.setPosition(x, y);
		bullet.setSpeed(0, -8);
					
		this.bulletList.add(bullet);
		this.musicStore.playWave("gun");
	}
	
	public void makePlayerLargeBullet(int x, int y) {
	
		BulletSprite bullet = new BulletSprite(100, 32, 8);
		bullet.addFrame(this.imageStore.getImage(ImageStore.PLAYER_LARGE_BULLET));
		bullet.setPosition(x, y);
		bullet.setSpeed(0, -10);
					
		this.bulletList.add(bullet);
		this.musicStore.playWave("gun");

	}
	
	public void makeSmallBullet(int x, int y) {
		
		BulletSprite bullet = new BulletSprite(10, 32, 4);
		bullet.addFrame(this.imageStore.getImage(ImageStore.SMALL_BULLET));
		bullet.setPosition(x, y);
		bullet.setSpeed(0, 3);
					
		this.enemyBulletList.add(bullet);
		this.musicStore.playWave("gun2");
	}
	
	public void makeSmartSmallBullet(int x, int y, int targetX, int targetY) {
		
		double speed = 3;
		
		double distanceX = targetX - x;
		double distanceY = targetY - y;
		
		double factor = Math.sqrt( (distanceX*distanceX) + (distanceY*distanceY) );
		
		double directionX = (distanceX/factor*speed);
		double directionY = (distanceY/factor*speed) - 2;
				
		BulletSprite bullet = new BulletSprite(10, 32, 4);		
		bullet.addFrame(this.imageStore.getImage(ImageStore.SMALL_BULLET));
		bullet.setPosition(x, y);
		bullet.setSpeed( directionX, directionY );
							
		this.enemyBulletList.add(bullet);
		this.musicStore.playWave("gun2");
	}
}
	
	