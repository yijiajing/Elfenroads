/*
 * @(#)PowerUpSprite.java
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

abstract class PowerUpSprite extends Sprite {
	
	public PowerUpSprite(int tileSize, int radius) {
		
		super(tileSize, radius);
	}
	
	public abstract void execute(PlayerSprite player);
}

class WeaponPowerUpSprite extends PowerUpSprite {
	
	private int value;
	
	public WeaponPowerUpSprite(int value, int tileSize, int radius) {
	
		super(tileSize, radius);
		this.value = value;		
	}	
	
	public void execute(PlayerSprite player) {
		
		player.upgradeWeapon(this.value);
	}
}

class ArmorPowerUpSprite extends PowerUpSprite {
	
	private int value;
	
	public ArmorPowerUpSprite(int value, int tileSize, int radius) {
	
		super(tileSize, radius);
		this.value = value;		
	}	
	
	public void execute(PlayerSprite player) {
		
		player.heal(this.value);
	}
}

class LifePowerUpSprite extends PowerUpSprite {
	
	private int value;
	
	public LifePowerUpSprite(int value, int tileSize, int radius) {
	
		super(tileSize, radius);
		this.value = value;		
	}	
	
	public void execute(PlayerSprite player) {
		
		player.addLife(this.value);
	}
}
