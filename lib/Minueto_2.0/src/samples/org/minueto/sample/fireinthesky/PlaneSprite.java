/*
 * @(#)PlaneSprite.java
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

import org.minueto.image.MinuetoImage;
import org.minueto.window.MinuetoWindow;

class PlaneSprite extends Sprite {
	
	private MinuetoImage[] imageExplosionFrame;
	private int numberExplosionFrame;
	private int currentExplosionFrame;
		
	private boolean exploding;
	private boolean dead;
	
	private int toughness;
	private int maxToughness;
	
	private MusicStore musicStore;
	
	public PlaneSprite(int toughness, int tileSize, int radius, MusicStore musicStore) {
		
		super(tileSize, radius);
			
		this.imageExplosionFrame = new MinuetoImage[10];		
		this.currentExplosionFrame = 0;
		this.numberExplosionFrame = 0;
		
		this.exploding = false;
		this.dead = false;	
		
		this.maxToughness = toughness;
		this.toughness = toughness;
		
		this.musicStore = musicStore;
	}
	
	public void addExplosionFrame(MinuetoImage imageNewFrame) {

		if(numberExplosionFrame > 10) return;

		this.imageExplosionFrame[this.numberExplosionFrame] = imageNewFrame;
		this.numberExplosionFrame++;
	}
		
	public void hit(int value) {
		
		this.toughness = this.toughness - value;		
		
		if (this.toughness <= 0) {
			this.toughness = 0; 
			this.explode();
			this.musicStore.playWave("hit");
		} else {
			this.musicStore.playWave("ding");
		}
	}
	
	public void heal(int value) {
	
		this.toughness = this.toughness + value;
		
		if (this.toughness > this.maxToughness) {
			this.toughness = this.maxToughness;
		}
	}
	
	public void explode() {
		
		this.exploding = true;
	}
	
	public void kill() {
		
		this.dead = true;
	}
	
	public void revive() {
		
		this.dead = false;
		this.currentExplosionFrame = 0;
		this.toughness = this.maxToughness;
	}
	
	public int getToughness() {
		
		return this.toughness;
	}
	
	public int getMaxToughness() {
		
		return this.maxToughness;
	}
	
	public boolean isDead() {
		
		return this.dead;
	}
	
	public boolean isExploding() {
		
		return this.exploding;
	}
	
	public boolean isCollision(Sprite sprOtherSprite) {
	
		if (this.dead == true) return false;
				
		return super.isCollision(sprOtherSprite);	
	}
	
	public boolean isInScreen(MinuetoWindow window, int x, int y) {

		if (this.dead == true) return false;

		return super.isInScreen(window, x, y);
	}
	
	public boolean draw(MinuetoWindow window, int x, int y) {

		if (this.dead == true) return false;

		if (this.exploding == true) {
				
			int positionOnScreenX = ((int)this.getPositionX() - x) - (this.getTileSize() /2);
			int positionOnScreenY = ((int)this.getPositionY() - y) - (this.getTileSize() /2);
			
			window.draw(this.imageExplosionFrame[this.currentExplosionFrame/6], positionOnScreenX, positionOnScreenY);
			this.currentExplosionFrame++;
			
			if ((this.currentExplosionFrame/6) == this.numberExplosionFrame) {
				this.exploding = false;
				this.dead = true;
			}
			
		} else {
			
			return super.draw(window, x, y);
		}
				
		return true;
	}
}