/*
 * @(#)PowerUpGenerator.java
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

class PowerUpGenerator {
	
	private LinkedList<PowerUpSprite> powerUps;
	private ImageStore imageStore;
	
	public PowerUpGenerator (LinkedList<PowerUpSprite> powerUps, 
			ImageStore imageStore) {
		
		this.powerUps = powerUps;
		this.imageStore = imageStore;
	}
	
	public PowerUpSprite makePowerUpWeapon(int x, int y, int value) {
		
		PowerUpSprite powerUp = new WeaponPowerUpSprite(value,32,8);
		
		powerUp.addFrame(this.imageStore.getImage(ImageStore.POWERUP_WEAPON));
		powerUp.setPosition(x, y);
		powerUp.setSpeed(0, 1);
		
		this.powerUps.add(powerUp);
		
		return powerUp;
	}
	
	public PowerUpSprite makePowerUpArmor(int x, int y, int value) {
		
		PowerUpSprite powerUp = new ArmorPowerUpSprite(value,32,8);
		
		powerUp.addFrame(this.imageStore.getImage(ImageStore.POWERUP_ARMOR));
		powerUp.setPosition(x, y);
		powerUp.setSpeed(0, 1);
		
		this.powerUps.add(powerUp);
		
		return powerUp;
	}
	
	public PowerUpSprite makePowerUpLife(int x, int y, int value) {
		
		PowerUpSprite powerUp = new LifePowerUpSprite(value,32,8);
		
		powerUp.addFrame(this.imageStore.getImage(ImageStore.POWERUP_LIFE));
		powerUp.setPosition(x, y);
		powerUp.setSpeed(0, 1);
		
		this.powerUps.add(powerUp);
		
		return powerUp;
	}
	
}