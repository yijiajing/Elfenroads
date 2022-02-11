/*
 * @(#)Sprite.java
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

public class Sprite {

	private double positionX;
	private double positionY;

	private double speedX;
	private double speedY;

	private MinuetoImage[] imageFrame;
	private int numberFrame;
	private int currentFrame;

	private int tileSize;
	private int radius;
	

	public Sprite(int tileSize, int radius) {

		this.imageFrame = new MinuetoImage[20];		
		this.currentFrame = 0;
		this.numberFrame = 0;

		this.positionX = 0;
		this.positionY = 0;

		this.speedX = 0;
		this.speedY = 0;

		this.tileSize = tileSize;
		this.radius = radius;
		

	}

	public void addFrame(MinuetoImage imageNewFrame) {

		if(numberFrame > 20) return;

		this.imageFrame[this.numberFrame] = imageNewFrame;
		this.numberFrame++;

	}
	
	public void setCurrentFrame(int value) {

		this.currentFrame = value;
	}

	public void setSpeed(double valueX, double valueY) {

		this.speedX = valueX;
		this.speedY = valueY;
	}
	
	public void setSpeedX(double valueX) {

		this.speedX = valueX;
	}
	
	public void setSpeedY(double valueY) {
		
		this.speedY = valueY;
	}

	public void updateSpeed(double valueX, double valueY) {

		this.speedX = this.speedX + valueX;
		this.speedY = this.speedY + valueY;
				
	}

	public void setPosition(int valueX, int valueY) {

		this.positionX = valueX;
		this.positionY = valueY;
	}

	public void updatePosition(int valueX, int valueY) {

		this.positionX = this.positionX + valueX;
		this.positionY = this.positionY + valueY;
	}
	
	public int getPositionX() {
		
		return (int)this.positionX;
	}
	
	public int getPositionY() {
		
		return (int)this.positionY;
	}

	public double getSpeedX() {
		
		return this.speedX;
	}
	
	public double getSpeedY() {
		
		return this.speedY;
	}
	
	public int getTileSize() {
		
		return this.tileSize;
	}

	public void tick() {

		//System.out.println(x + " " + y + " " + speedX + " " + speedY);

		this.positionX = this.positionX + this.speedX;		
		this.positionY = this.positionY + this.speedY;
		
		//System.out.println(x + " " + y);
	}
	
	public boolean isInScreen(MinuetoWindow window, int x, int y) {

                int sizeScreenX = window.getWidth();
                int sizeScreenY = window.getHeight();

		int positionOnScreenX = (int)this.positionX - x;
		int positionOnScreenY = (int)this.positionY - y;
				
		int iFullTile = this.getTileSize();

		if ((positionOnScreenX < 0 ) || (positionOnScreenX > (sizeScreenX+iFullTile ))) {
			return false;
		}

		if ((positionOnScreenY < 0 ) || (positionOnScreenY > (sizeScreenY+iFullTile ))) {
			return false;
		}
 
		return true;
		
	}
	
	public boolean isCollision(Sprite otherSprite) {		
				
		int distanceX = (int)this.positionX - (int)otherSprite.positionX;
		int distanceY = (int)this.positionY - (int)otherSprite.positionY;
		
		int distance = (int)Math.sqrt((distanceX*distanceX) + (distanceY*distanceY));
		
		int conflict = distance - this.radius - otherSprite.radius;
		
		if (conflict < 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean draw(MinuetoWindow window, int x, int y) {

		if (this.numberFrame == 0) return false;

		int positionOnScreenX = ((int)this.positionX - x) - (this.imageFrame[this.currentFrame].getWidth() /2);
		int positionOnScreenY = ((int)this.positionY - y) - (this.imageFrame[this.currentFrame].getHeight() /2);

		window.draw(this.imageFrame[this.currentFrame], positionOnScreenX, positionOnScreenY);
			
		return true;
	}
}
