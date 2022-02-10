/*
 * @(#)GameMap.java
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

import java.util.Random;

import org.minueto.image.MinuetoImage;
import org.minueto.window.MinuetoWindow;

public class GameMap {

	private int[][] mapData;
	private int sizeX;
	private int sizeY;
	private int tileSize;

	public GameMap(int sizeX, int sizeY, int tileSize) {

		Random randomGenerator = new Random();

		this.mapData = new int[sizeX][sizeY];
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.tileSize = tileSize;

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				this.mapData[i][j] = randomGenerator.nextInt(40);
				if (this.mapData[i][j] > 3) { this.mapData[i][j] = 0; }
			}
		}
	}

	public void draw(MinuetoWindow window, MinuetoImage[] imageMapImages, int x, int y) {

		int sizeScreenX = window.getWidth();
		int sizeScreenY = window.getHeight();

		int tileIndexX = x / this.tileSize;
		int tileRemainderX = x % this.tileSize;
		int tileIndexY = y / this.tileSize;
		int tileRemainderY = y % this.tileSize;

		int drawingX = -tileRemainderX;
		int drawingY = -tileRemainderY;

		int indexX = tileIndexX;
		int indexY = tileIndexY;

		while (drawingX < sizeScreenX) {
			while (drawingY < sizeScreenY) {

				if ( (indexX < this.sizeX) && (indexY < this.sizeY) ) {
					window.draw(imageMapImages[this.mapData[indexX][indexY]], drawingX, drawingY);
				}

				drawingY = drawingY + this.tileSize;
				indexY++;
			}

			drawingY = -tileRemainderY;
			drawingX = drawingX + this.tileSize;
			indexY = tileIndexY;
			indexX++;
		}
	}
}

