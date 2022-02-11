/*
 * @(#)SoundMidi.java
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

/* http://www.jsresources.org/examples/SimpleAudioPlayer.java.html */
/* http://javaalmanac.com/egs/javax.sound.sampled/pkg.html */
 
package org.minueto.sample.fireinthesky.sound;

import java.net.URL;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

/**
 * The <code>MinuetoWave</code> class describes a Wave file that can be played.
 * <code>MinuetoWave</code> supports the maximum playback of 32 simultaneous waves files.
 * These simultaneous playback are described as channels. In other words, no more
 * than 32 channels should be allocated in total when creating <code>MinuetoWave</code>.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.44
 * @see		MinuetoWindow
 **/

public class SoundWave {
	
	private AudioInputStream[]	audioInputStream;
	private AudioFormat			audioFormat;
	private int					audioStreamSize;
	
	private Clip[]				clipLine;
	private DataLine.Info[]		dataLineInfo;
	
	private int					currentChannel;
	private int					numberChannel;
	
	private static int			channelAllocated = 0;
	private final static int	MAXCHANNEL = 32;

	/**
	 * Load the specified Wave file. The wave file must be one of the following
	 * formats:
	 * <ul>
	 * <li> PCM signed
	 * <li> PCM unsigned
	 * <li> A-law
	 * <li> u-Law
	 * </ul>
	 * The maximum number of simultaneous playback for that wave must also be
	 * given as a parameter. Note that the maximum number of channel must not
	 * exceed 32 for all <code>MinuetoWave</code>.
	 *	 
	 * @param filename <code>String</code> denoting the name and location of the file to load.
	 * @throws <code>MinuetoFileException</code> if the Wave is unavailable or corrupt.
	 * @throws <code>MinuetoSoundSystemUnavailableException</code> if the computer is unable to play Midi files.
	 * @throws <code>MinuetoSoundOutOfChannelException</code> not enough channels are availables.
	 **/
	public SoundWave(String filename, int iNumChannel) 
		throws SoundFileException, SoundSystemUnavailableException, 
		SoundOutOfChannelException {
		
        int max = iNumChannel;
        File filSoundFile;
						
		this.initChannels(iNumChannel);
		
		filSoundFile = new File(filename);
		        
		for (int i = 0; i < max; i++) {
			try {
				this.audioInputStream[i] = AudioSystem.getAudioInputStream(filSoundFile);
			} catch (Exception e) {			
				throw new SoundFileException("Unable to load audio file " + filename);
			}
		}
		
        this.load();
	}
    
    /**
	 * Load the specified Wave file. The wave file must be one of the following
	 * formats:
	 * <ul>
	 * <li> PCM signed
	 * <li> PCM unsigned
	 * <li> A-law
	 * <li> u-Law
	 * </ul>
	 * The maximum number of simultaneous playback for that wave must also be
	 * given as a parameter. Note that the maximum number of channel must not
	 * exceed 32 for all <code>MinuetoWave</code>.
	 *	 
	 * @param url <code>URL</code> denoting the name and location of the file to load.
	 * @throws <code>MinuetoFileException</code> if the Wave is unavailable or corrupt.
	 * @throws <code>MinuetoSoundSystemUnavailableException</code> if the computer is unable to play Midi files.
	 * @throws <code>MinuetoSoundOutOfChannelException</code> not enough channels are availables.
	 **/

	public SoundWave(URL url, int iNumChannel) 
		throws SoundFileException, SoundSystemUnavailableException, 
		SoundOutOfChannelException {
		
        int max = iNumChannel;        
						
		this.initChannels(iNumChannel);
				        
		for (int i = 0; i < max; i++) {
			try {
				this.audioInputStream[i] = AudioSystem.getAudioInputStream(url);
			} catch (Exception e) {			
				throw new SoundFileException("Unable to load audio file " + url.toString());
			}
		}
		
        this.load();
	}
    
    private void initChannels(int iNumChannel) throws SoundOutOfChannelException {
        
        this.numberChannel = iNumChannel;
		
		if ((SoundWave.channelAllocated + iNumChannel) > SoundWave.MAXCHANNEL) {
			throw new SoundOutOfChannelException("Could not allocate " + iNumChannel + 
				" additionnal channels.");
		}
		
		SoundWave.channelAllocated = SoundWave.channelAllocated + iNumChannel;
		
		this.currentChannel = 0;
        
        this.audioInputStream = new AudioInputStream[iNumChannel];
    }
    
    public void load() 
		throws SoundSystemUnavailableException {
				
        int max = this.numberChannel;
        
		// At present, ALAW and ULAW encodings must be converted
        // to PCM_SIGNED before it can be played
		
        this.audioFormat = this.audioInputStream[0].getFormat();
        
		if (this.audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            this.audioFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    this.audioFormat.getSampleRate(),
                    this.audioFormat.getSampleSizeInBits()*2,
                    this.audioFormat.getChannels(),
                    this.audioFormat.getFrameSize()*2,
                    this.audioFormat.getFrameRate(),
                    true);        // big endian
			
			for (int i = 0; i < max; i++) {
				this.audioInputStream[i] = 
					AudioSystem.getAudioInputStream(this.audioFormat, this.audioInputStream[0]);
			}
        }
        		
		this.audioStreamSize = (int)this.audioInputStream[0].getFrameLength() 
									* this.audioFormat.getFrameSize();
		
		this.dataLineInfo = new DataLine.Info[max];
		this.clipLine = new Clip[max];
		
		for(int i = 0; i < max; i ++) {

			this.dataLineInfo[i] = new DataLine.Info(Clip.class, this.audioFormat, this.audioStreamSize);

			try {
				this.clipLine[i] = (Clip) AudioSystem.getLine(this.dataLineInfo[i]);
				this.clipLine[i].open(this.audioInputStream[i]);
			} catch (LineUnavailableException e) {
				throw new SoundSystemUnavailableException("Unable to access/initialize sound system.");
			} catch (IOException e) {
				throw new SoundSystemUnavailableException("Unable to access/initialize sound system.");
			}
			
			this.clipLine[i].setFramePosition(0);
		}
	}
	
	
	/**
	 * Play the Wave using the next available channel.
	 *
	 * @return <code>int</code> denoting the channel to play the wave.
	 **/
	public int play() {
		
		int i = this.currentChannel;
		this.currentChannel = (this.currentChannel + 1)%this.numberChannel;
		
		this.clipLine[i].stop();
		//if (this.getPosition(i) == 0) {
		this.clipLine[i].setFramePosition(0);
		//}
	
		clipLine[i].start();
		
		return i;
	}
	
	/**
	 * Stop the Wave playing in a given channel.
	 *
	 * @param iChannel <code>int</code> denoting the channel to stop playback.
	 * @throws <code>MinuetoSoundInvalidChannelException</code> if an inexistant channel is accessed.
	 **/
	public void stop(int iChannel) throws SoundInvalidChannelException {
	
		if (iChannel < 0) { throw new SoundInvalidChannelException(
			"Channel " + iChannel + " does not exist."); }
		if (iChannel >= this.numberChannel) { throw new SoundInvalidChannelException(
			"Channel " + iChannel + " does not exist."); }
		
		this.clipLine[iChannel].stop();
		this.clipLine[iChannel].setFramePosition(0);
	}
	
	/**
	 * Set the playback position of a Wave playing in a given channel.
	 *
	 * @param iChannel <code>int</code> denoting the channel to change the playback position.
	 * @throws <code>MinuetoSoundInvalidChannelException</code> if an inexistant channel is accessed.
	 **/	
	public void setPosition(int lValue, int iChannel) throws SoundInvalidChannelException {
	
		if (iChannel < 0) { throw new SoundInvalidChannelException(
			"Channel " + iChannel + " does not exist."); }
		if (iChannel >= this.numberChannel) { throw new SoundInvalidChannelException(
			"Channel " + iChannel + " does not exist."); }
			
		this.clipLine[iChannel].setFramePosition((int)lValue);
	}
	
	/**
	 * Get the lenght of the Wave.
	 *
	 * @return <code>long</code> denoting the lenght of the Wave in seconds.
	 **/
	public int getLength() {
		
		return this.clipLine[0].getFrameLength();
	}
	
	/**
	 * Get the current position of the Wave playing of a given channel.
	 *
	 * @param iChannel <code>int</code> denoting the channel to change the playback position.	 
	 * @return <code>long</code> denoting the current position in seconds.
	 * @throws <code>MinuetoSoundInvalidChannelException</code> if an inexistant channel is accessed.
	 **/
	public int getPosition(int iChannel) throws SoundInvalidChannelException {
	
		if (iChannel < 0) { throw new SoundInvalidChannelException(
			"Channel " + iChannel + " does not exist."); }
		if (iChannel >= this.numberChannel) { throw new SoundInvalidChannelException(
			"Channel " + iChannel + " does not exist."); }
	
		return this.clipLine[iChannel].getFramePosition();
	}
	
	/**
	 * Set the volume of the Wave playing of a given channel. This function is 
	 * not supported by all JDKs.
	 *
	 * @param volume <code>double</code> denoting the desired volume with a value
	 * found between 0 (silence) and 1 (max volume).
	 * @param iChannel <code>int</code> denoting the channel to change the playback position.
	 * @throws <code>MinuetoSoundInvalidChannelException</code> if an inexistant channel is accessed.
	 **/
	
	public void setVolume(double volume) throws SoundInvalidChannelException {
		
		if ( (volume <0) || (volume >1) ) return;
		
		for(int i = 0; i <this.numberChannel; i++) {
			FloatControl gainControl = (FloatControl)this.clipLine[i].getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float)(Math.log(volume)/Math.log(10.0)*20.0);
			gainControl.setValue(dB);	
		}		
	}
	
	/**
	 * Free all channels allocated to this Wave. Please not that the wave should not be
	 * played once the channels have been freed.
	 **/
	public void close() {
	
		for(int i = 0; i < this.numberChannel; i++) {
			this.clipLine[i].close();
		}
		
		SoundWave.channelAllocated = SoundWave.channelAllocated - this.numberChannel;
	}
	
	
}