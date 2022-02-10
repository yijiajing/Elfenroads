/*
 * @(#)MinuetoImageFile.java
 *
 * Minueto - The Game Development Framework 
 * Copyright (c) 2004 McGill University
 * 3480 University Street, Montreal, Quebec H3A 2A7
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.minueto.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.minueto.MinuetoFileException;

/**
 * The <code>MinuetoImageFile</code> class allows you to load an image from a file.
 * Since <code>MinuetoImageFile</code> extends <code>MinuetoImage</code>, all the important image
 * manipulation fonctionnality (such as draw, drop, scale, etc) are available.
  *
 * @author Alexandre Denault
 * @version 1.1
 * @since  Minueto 0.4
 * @see  MinuetoImage
 **/
public class MinuetoImageFile extends MinuetoImage {

    /**
     * Build a <code>MinuetoImageFile</code> by loading the specified file.
     *
     * @param filename <code>String</code> denoting which file should be loaded.
     * @throws <code>MinuetoFileException</code> if it could not open the file.
     **/
    public MinuetoImageFile(String filename) throws MinuetoFileException {

        /* When subclassing to a class with multiple constructor,it's
         * important to specify which constructor to use. */
        super();

        BufferedImage bufUnacceleratedImage;
        
        if (filename == null)
            new MinuetoFileException("Could not load null file.");

        File testForExistance = new File(filename);
        
        if(testForExistance.isFile()) { 

        	try {
        		bufUnacceleratedImage = ImageIO.read(testForExistance);
        	} catch(IOException ioe) {            
        		throw new MinuetoFileException("Could not load image file " + filename + " .");
        	}
        } else {
        	
        	URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        	
        	if (url == null)
                new MinuetoFileException("Could not find file." + filename);
            
            try {
                bufUnacceleratedImage = ImageIO.read(url);
            } catch(IOException ioe) {
                throw new MinuetoFileException("Could not load image file " + url.toString() + " .");
            }
        }

        super.setUpImage(this.convert(bufUnacceleratedImage));
    }

    /**
     * Build a <code>MinuetoImageFile</code> by loading the specified file. Special thanks
           * to Adam Hooper who gave me the idea to add this function and provided the code to do it.
     *
     * @param url <code>URL</code> denoting which file should be loaded.
     * @throws <code>MinuetoFileException</code> if it could not open the file.
     **/
    public MinuetoImageFile(URL url) throws MinuetoFileException {

        /* When subclassing to a class with multiple constructor,it's
         * important to specify which constructor to use. */
        super();

        BufferedImage bufUnacceleratedImage;
        
        if (url == null)
            new MinuetoFileException("Could not load null file.");
        
        try {
            bufUnacceleratedImage = ImageIO.read(url);
        } catch(IOException ioe) {
            throw new MinuetoFileException("Could not load image file " + url.toString() + " .");
        }

        super.setUpImage(this.convert(bufUnacceleratedImage));
    }

    /**
     * We want Minueto to use a consistant color model. Thus, we copy the image 
     * to a new RBGA surface.
     *
     * @return <code>BufferedImage</code> of the RGBA type.
     * @param bufUnacceleratedImage <code>BufferedImage</code> of arbitrary type.
     **/
    private BufferedImage convert(BufferedImage bufUnacceleratedImage) {

        BufferedImage bufferedImage;
        Graphics2D  graphics2D;

        bufferedImage = ImageTools.createImage(
                       bufUnacceleratedImage.getWidth(),
                       bufUnacceleratedImage.getHeight());

        graphics2D = bufferedImage.createGraphics();

        graphics2D.drawImage(bufUnacceleratedImage,0,0,null);
        graphics2D.dispose();

        return bufferedImage;
    }
}

