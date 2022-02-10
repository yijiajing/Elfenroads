/*
 * @(#)ImageStore.java
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

import java.net.URL;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoText;

class ImageStore {
	
	private MinuetoImage[] imageDatabase;
	
	private MinuetoImage[] mapDatabase;
	
	public static final int PLAYER_PLANE1 = 0;
	public static final int PLAYER_PLANE2 = 1;
	public static final int PLAYER_PLANE3 = 2;
	public static final int PLAYER_LIFE   = 22;
	
	public static final int PLAYER_BULLET = 3;
	public static final int PLAYER_LARGE_BULLET = 20;
	public static final int SMALL_BULLET = 21;
	
	public static final int MAP_TILE1 = 4;
	public static final int MAP_TILE2 = 5;
	public static final int MAP_TILE3 = 6;
	public static final int MAP_TILE4 = 7;
	
	public static final int EXPLOSION_SMALL1 = 8;
	public static final int EXPLOSION_SMALL2 = 9;
	public static final int EXPLOSION_SMALL3 = 10;
	public static final int EXPLOSION_SMALL4 = 11;
	public static final int EXPLOSION_SMALL5 = 12;
	public static final int EXPLOSION_SMALL6 = 13;
	
	public static final int EXPLOSION_BIG1 = 14;
	public static final int EXPLOSION_BIG2 = 15;
	public static final int EXPLOSION_BIG3 = 16;
	public static final int EXPLOSION_BIG4 = 17;
	public static final int EXPLOSION_BIG5 = 18;
	public static final int EXPLOSION_BIG6 = 19;
	
	public static final int ENEMY_PLANE_SMALL1 = 29; //30-44	
	public static final int ENEMY_PLANE_SMALL2 = 45; //46-30
	public static final int ENEMY_PLANE_SMALL3 = 61; //62-76
	public static final int ENEMY_PLANE_BOSS = 77; //78-92
	
	public static final int POWERUP_WEAPON = 93;
	public static final int POWERUP_ARMOR = 94;
	public static final int POWERUP_LIFE = 95;
	public static final int BOMB = 96;
	
	public static final int SUBMARINE = 97; //98-101
	public static final int DESTROYER = 102; //103
	
	public static final int GAMEOVER = 23;
	public static final int MISSION_SUCCESS = 104;
	public static final int TITLE = 24;
	public static final int TITLE_SHADOW = 25;
	public static final int TITLE_BG = 27;
	public static final int PRESS_START = 26;
	public static final int PRESS_START_SHADOW = 28;
	
	public ImageStore () {
		
		MinuetoImage tileImage;
		MinuetoImage tempImage;
		
        URL imageLocation;
        
		MinuetoFont arial64B = new MinuetoFont("Arial",64,true,false);
		MinuetoFont arial40B = new MinuetoFont("Arial",32,true,false);

		this.imageDatabase = new MinuetoImage[128];        

		try {
            imageLocation = this.getClass().getResource("1945.png");
            if (imageLocation != null) {
                tileImage = new MinuetoImageFile(imageLocation);
            } else {
                tileImage = new MinuetoImageFile("images/1945.png");
            }
		} catch (MinuetoFileException mfe) {
			System.err.println("Could not load image file.");
			System.exit(-1);
			return;
		}

		try {
            imageLocation = this.getClass().getResource("title.png");
            if (imageLocation != null) {
                imageDatabase[TITLE_BG] = new MinuetoImageFile(imageLocation);
            } else {
                imageDatabase[TITLE_BG] = new MinuetoImageFile("images/title.png");
            }
		} catch (MinuetoFileException mfe) {
			System.err.println("Could not load title image file.");
			System.exit(-1);
			return;
		}		

		this.imageDatabase[MAP_TILE2] = tileImage.crop(103,499,64,64);
		this.imageDatabase[MAP_TILE3] = tileImage.crop(168,499,64,64);
		this.imageDatabase[MAP_TILE4] = tileImage.crop(233,499,64,64);

		this.imageDatabase[MAP_TILE1] = new MinuetoImage(64,64);
		tempImage = tileImage.crop(268,235,32,32);
		this.imageDatabase[MAP_TILE1].draw(tempImage, 0,0);
		this.imageDatabase[MAP_TILE1].draw(tempImage, 0,32);
		this.imageDatabase[MAP_TILE1].draw(tempImage, 32,0);
		this.imageDatabase[MAP_TILE1].draw(tempImage, 32,32);

		this.imageDatabase[PLAYER_PLANE1] = tileImage.crop(301,104,64,64);
		this.imageDatabase[PLAYER_LIFE] = tileImage.crop(136,202,32,32);
		
		this.imageDatabase[ENEMY_PLANE_SMALL1] = tileImage.crop(136,70,32,32);
				
		for (int i = 1; i < 16; i++) {
			
			this.imageDatabase[ENEMY_PLANE_SMALL1+i] = 
				this.imageDatabase[ENEMY_PLANE_SMALL1].rotate((Math.PI * i) / 8.0);						
		}
		
		this.imageDatabase[ENEMY_PLANE_SMALL2] = tileImage.crop(136,4,32,32);
				
		for (int i = 1; i < 16; i++) {
			
			this.imageDatabase[ENEMY_PLANE_SMALL2+i] = 
				this.imageDatabase[ENEMY_PLANE_SMALL2].rotate((Math.PI * i) / 8.0);			
		}
		
		this.imageDatabase[ENEMY_PLANE_SMALL3] = tileImage.crop(136,103,32,32);
				
		for (int i = 1; i < 16; i++) {
			
			
			this.imageDatabase[ENEMY_PLANE_SMALL3+i] = 
				this.imageDatabase[ENEMY_PLANE_SMALL3].rotate((Math.PI * i) / 8.0);			
		}
		
		this.imageDatabase[ENEMY_PLANE_BOSS] = tileImage.crop(598,4,98,98);
				
		for (int i = 1; i < 16; i++) {
			
			this.imageDatabase[ENEMY_PLANE_BOSS+i] = 
				this.imageDatabase[ENEMY_PLANE_BOSS].rotate((Math.PI * i) / 8.0);					
		}
		
		this.imageDatabase[SUBMARINE] = new MinuetoImage(64,128);
		this.imageDatabase[SUBMARINE].draw(this.imageDatabase[MAP_TILE1], 0,0);
		this.imageDatabase[SUBMARINE].draw(this.imageDatabase[MAP_TILE1], 0,64);
		
		this.imageDatabase[SUBMARINE+1] = this.imageDatabase[SUBMARINE].crop(0,0,64,64);
		this.imageDatabase[SUBMARINE+2] = this.imageDatabase[SUBMARINE].crop(0,0,64,64);
		this.imageDatabase[SUBMARINE+3] = this.imageDatabase[SUBMARINE].crop(0,0,64,64);
		this.imageDatabase[SUBMARINE+4] = this.imageDatabase[SUBMARINE].crop(0,0,64,64);
		this.imageDatabase[SUBMARINE+5] = this.imageDatabase[SUBMARINE].crop(0,0,64,64);
		
		tempImage = tileImage.crop(367,103,31,97);
		this.imageDatabase[SUBMARINE].draw(tempImage, 15,15);
		
		tempImage = tileImage.crop(400,103,31,97);
		this.imageDatabase[SUBMARINE+1].draw(tempImage, 15,15);
		
		tempImage = tileImage.crop(433,103,31,97);
		this.imageDatabase[SUBMARINE+2].draw(tempImage, 15,15);
		
		tempImage = tileImage.crop(466,103,31,97);
		this.imageDatabase[SUBMARINE+3].draw(tempImage, 15,15);
		
		tempImage = tileImage.crop(499,103,31,97);
		this.imageDatabase[SUBMARINE+4].draw(tempImage, 15,15);
		
		tempImage = tileImage.crop(532,103,31,97);
		this.imageDatabase[SUBMARINE+5].draw(tempImage, 15,15);
		
		this.imageDatabase[DESTROYER] = new MinuetoImage(64,256);
		this.imageDatabase[DESTROYER].draw(this.imageDatabase[MAP_TILE1], 0,0);
		this.imageDatabase[DESTROYER].draw(this.imageDatabase[MAP_TILE1], 0,64);
		this.imageDatabase[DESTROYER].draw(this.imageDatabase[MAP_TILE1], 0,128);
		this.imageDatabase[DESTROYER].draw(this.imageDatabase[MAP_TILE1], 0,192);

		this.imageDatabase[DESTROYER+1] = this.imageDatabase[DESTROYER].crop(0,0,64,64);
		
		tempImage = tileImage.crop(466,301,40,196);
		this.imageDatabase[DESTROYER].draw(tempImage, 12, 30);
		
		tempImage = tileImage.crop(508,301,40,196);
		this.imageDatabase[DESTROYER+1].draw(tempImage, 12, 30);
		
		this.imageDatabase[PLAYER_BULLET] = tileImage.crop(37,169,32,32);
		this.imageDatabase[PLAYER_LARGE_BULLET] = tileImage.crop(4,169,32,32);
		this.imageDatabase[SMALL_BULLET] = tileImage.crop(70,202,32,32);
		
		this.imageDatabase[POWERUP_WEAPON] = tileImage.crop(103,268,32,32);
		this.imageDatabase[POWERUP_ARMOR] = tileImage.crop(136,268,32,32);
		this.imageDatabase[POWERUP_LIFE] = tileImage.crop(202,268,32,32);
		
		this.imageDatabase[BOMB] = tileImage.crop(268,268,32,32);
				
		this.imageDatabase[EXPLOSION_SMALL1] = tileImage.crop(70,169,32,32);
		this.imageDatabase[EXPLOSION_SMALL2] = tileImage.crop(103,169,32,32);
		this.imageDatabase[EXPLOSION_SMALL3] = tileImage.crop(136,169,32,32);
		this.imageDatabase[EXPLOSION_SMALL4] = tileImage.crop(169,169,32,32);
		this.imageDatabase[EXPLOSION_SMALL5] = tileImage.crop(202,169,32,32);
		this.imageDatabase[EXPLOSION_SMALL6] = tileImage.crop(235,169,32,32);
		
		this.imageDatabase[EXPLOSION_BIG1] = tileImage.crop(4,301,64,64);
		this.imageDatabase[EXPLOSION_BIG2] = tileImage.crop(70,301,64,64);
		this.imageDatabase[EXPLOSION_BIG3] = tileImage.crop(136,301,64,64);
		this.imageDatabase[EXPLOSION_BIG4] = tileImage.crop(202,301,64,64);
		this.imageDatabase[EXPLOSION_BIG5] = tileImage.crop(268,301,64,64);
		this.imageDatabase[EXPLOSION_BIG6] = tileImage.crop(334,301,64,64);
			
		this.imageDatabase[GAMEOVER] 
			= new MinuetoText("Game Over", arial64B, MinuetoColor.WHITE);
		this.imageDatabase[TITLE] 
			= new MinuetoText("Fire In the Sky", arial64B, MinuetoColor.WHITE);
		this.imageDatabase[TITLE_SHADOW] 
			= new MinuetoText("Fire In the Sky", arial64B, new MinuetoColor(10,10,10));
		this.imageDatabase[PRESS_START] 
			= new MinuetoText("Press Space to Play", arial40B, MinuetoColor.WHITE);																
		this.imageDatabase[PRESS_START_SHADOW] 
			= new MinuetoText("Press Space to Play", arial40B, new MinuetoColor(10,10,10));
		this.imageDatabase[MISSION_SUCCESS] 
			= new MinuetoText("Mission Success!", arial64B, MinuetoColor.WHITE);
																																
		this.mapDatabase = new MinuetoImage[4];
		this.mapDatabase[0] = this.imageDatabase[MAP_TILE1];
		this.mapDatabase[1] = this.imageDatabase[MAP_TILE2];
		this.mapDatabase[2] = this.imageDatabase[MAP_TILE3];
		this.mapDatabase[3] = this.imageDatabase[MAP_TILE4];
	}
	
	public MinuetoImage getImage(int value) {
		
		return this.imageDatabase[value];	
	}
	
	public MinuetoImage getImage(int value, int index) {
		
		return this.imageDatabase[value+index];	
	}
	
	public MinuetoImage[] getMapImage() {
		
		return this.mapDatabase;
	}
	
}