/*
 * @(#)EnemySprite.java
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

public class EnemySprite extends PlaneSprite {
	
	EnemyBehavior enemyBehavior;
	int pointValue;
		
	int targetSpeedX;
	int targetSpeedY;
	
	int turnCoolDown;
	int turnSpeed;
	
	public EnemySprite(int pointValue, int toughness, int tileSize, int radius,
			int turnSpeed, MusicStore musicStore) {
		
		super(toughness, tileSize, radius, musicStore);
		
		this.pointValue = pointValue;
		
		this.targetSpeedX = 0;
		this.targetSpeedY = 0;						
	
		this.turnSpeed = turnSpeed;	
		this.turnCoolDown = 0;
	}
	
	public void setSpeed(double valueX, double valueY) {

		this.targetSpeedX = (int)(valueX - this.getSpeedX());
		this.targetSpeedY = (int)(valueY - this.getSpeedY());						
	}
	
	public void setBehavior(EnemyBehavior enemyBehavior) {
		
		this.enemyBehavior = enemyBehavior;
	}
	
	public void tick() {
	
		double dTemp = 0;
		int iTemp = 0;
		
		if (this.turnCoolDown != 0) { this.turnCoolDown--; }
	
		if ((( (this.targetSpeedX != 0) || 
				(this.targetSpeedY != 0) )) && 
				(this.turnCoolDown == 0)) {
		
			if (this.targetSpeedX !=0) {
				
				if (this.targetSpeedX > 0) {
					
					this.updateSpeed(1,0);
					this.targetSpeedX--;
					
				} else {
										
					this.updateSpeed(-1,0);
					this.targetSpeedX++;
					
				}
				
			}
			
			if (this.targetSpeedY !=0) {
				
				if (this.targetSpeedY > 0) {
					
					this.updateSpeed(-1,1);
					this.targetSpeedX++;
					this.targetSpeedY--;
					
				} else {
										
					this.updateSpeed(1,-1);
					this.targetSpeedX--;
					this.targetSpeedY++;
					
				}
				
			}
		
			dTemp = Math.atan2(-this.getSpeedX(), this.getSpeedY()) / Math.PI;
			iTemp = (int)((dTemp*8)+8);
			if (iTemp == 16) iTemp = 0;
			this.setCurrentFrame( iTemp );
			
			this.turnCoolDown = this.turnSpeed ;
		}
		
		this.enemyBehavior.tick();
		super.tick();
		
	}
	
	public int getShootX() {
	
		return (int)this.getPositionX();
	}	
	
	public int getShootY() {
		
		return (int)this.getPositionY() + (this.getTileSize()/2);
	}
	
	public int getPointValue() {
		
		return this.pointValue;
	}
	
}