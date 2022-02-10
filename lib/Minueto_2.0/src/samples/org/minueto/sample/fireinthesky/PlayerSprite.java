/*
 * @(#)PlayerSprite.java
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

import org.minueto.window.MinuetoWindow;

class PlayerSprite extends PlaneSprite {
	
	
	private int life;	
	private int invincibleCooldown;
	
	private int superBulletCooldown;
	
	private BulletGenerator bulletGenerator;
	
	
	
	public PlayerSprite(int toughness, int tileSize, int radius, 
			BulletGenerator bulletGenerator, MusicStore musicStore) {
		
		super(toughness, tileSize, radius, musicStore);
		this.bulletGenerator = bulletGenerator;
		
		this.life = 3;
				
		this.invincibleCooldown = 0;
		this.superBulletCooldown = 0;
	}
		
	public void revive() {
		
		if (this.life > 0) {
			this.life--;
			this.invincibleCooldown = 100;
			super.revive();
		}
		
	}
	
	public void hit(int value) {
		
		if (this.invincibleCooldown > 0) return;
		
		super.hit(value);		
		
		if (this.isExploding() == false) {
			this.invincibleCooldown = 50;
		}
	}
	
	public void addLife(int value) {
		this.life = this.life + value;
		if (this.life > 5) {
			this.life = 5;
		}
	}
	
	public void upgradeWeapon(int value) {
		
		this.superBulletCooldown = value;
	}
	
	public void shoot() {
		
		if (this.superBulletCooldown == 0) {
			
			this.bulletGenerator.makePlayerSmallBullet(this.getShootX(), this.getShootY());
		} else {
			
			this.bulletGenerator.makePlayerLargeBullet(this.getShootX(), this.getShootY());
		}
	}
	
	public boolean isInScreen(MinuetoWindow window, int x, int y) {

        int sizeScreenX = window.getWidth();
        int sizeScreenY = window.getHeight();

		int positionOnScreenX = this.getPositionX() - x + (int)this.getSpeedX() + 1;
		int positionOnScreenY = this.getPositionY() - y + (int)this.getSpeedY() + 1;
		
		int iHalfTile = this.getTileSize() / 2;
		
		if ((positionOnScreenX < iHalfTile ) || (positionOnScreenX > (sizeScreenX-iHalfTile ))) {
			return false;
		}

		if ((positionOnScreenY < iHalfTile ) || (positionOnScreenY > (sizeScreenY-iHalfTile ))) {
			return false;
		}
 
		return true;
	}
	
	public void tick() {

		//int iTempPositionX = this.getPositionX() + ;
		//int iTempPositionY = this.getPositionY() + this.getSpeedY();
		
		super.tick();
		
	}
	
	public int getLifeLeft() {
		
		return this.life;
	}
	
	public int getShootX() {
	
		return this.getPositionX() + 2;
	}	
	
	public int getShootY() {
		
		return this.getPositionY() - (this.getTileSize()/2) - 5;
	}
	
	public boolean draw(MinuetoWindow window, int x, int y) {

		if ( this.invincibleCooldown > 0 ) { this.invincibleCooldown--; }
		if ( this.superBulletCooldown > 0 ) { this.superBulletCooldown--; }
		if ( ((this.invincibleCooldown/6)%3) == 1 ) { return true; }		

		return super.draw(window, x, y);
	}
}
