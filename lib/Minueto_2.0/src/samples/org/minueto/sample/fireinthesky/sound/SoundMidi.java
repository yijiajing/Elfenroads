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
 
package org.minueto.sample.fireinthesky.sound;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import javax.sound.midi.*;

/**
 * The <code>MinuetoMidi</code> class describes a Midi file that can be played. 
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.44
 * @see		MinuetoWindow
 **/

public class SoundMidi {

	private Sequence	midiSequence;	
	private Sequencer	midiSequencer;
	
	private double		volume;
	
	private long		length;
	
	/**
	 * Load the specified Midi file.
	 *	 
	 * @param filename <code>String</code> denoting the name and location of the file to load.	 
	 * @throws <code>MinuetoFileException</code> if the Midi is unavailable or corrupt.
	 * @throws <code>MinuetoSoundSystemUnavailableException</code> if the computer is unable to play Midi files.
	 **/	
	public SoundMidi(String filename) 
		throws SoundFileException, SoundSystemUnavailableException {
		
		File soundFile = new File(filename);
		    
    	try {    	
        	this.midiSequence = MidiSystem.getSequence(soundFile);
        } catch (InvalidMidiDataException e) {
        	throw new SoundFileException("Unable to load audio file " + filename);
        } catch (IOException e) {
        	throw new SoundFileException("Error with audio file " + filename);
        }
    
        this.load();	
	}
	
    /**
	 * Load the specified Midi file.
	 *	 
	 * @param url <code>URL</code> denoting the name and location of the file to load.	 
	 * @throws <code>MinuetoFileException</code> if the Midi is unavailable or corrupt.
	 * @throws <code>MinuetoSoundSystemUnavailableException</code> if the computer is unable to play Midi files.
	 **/	
	public SoundMidi(URL url) 
		throws SoundFileException, SoundSystemUnavailableException {
		    
    	try {    	
        	this.midiSequence = MidiSystem.getSequence(url);
        } catch (InvalidMidiDataException e) {
        	throw new SoundFileException("Unable to load audio file " + url.toString());
        } catch (IOException e) {
        	throw new SoundFileException("Error with audio file " + url.toString());
        }
    
        this.load();	
	}
    
    private void load() 
        throws SoundFileException, SoundSystemUnavailableException {
        
        try {
    		this.midiSequencer = MidiSystem.getSequencer();
    		this.midiSequencer.open();
    	} catch (MidiUnavailableException e) {    		
    		throw new SoundSystemUnavailableException("Unable to access/initialize MIDI sound system.");
    	}
               
       	try {
       		this.midiSequencer.setSequence(this.midiSequence);
       	} catch (InvalidMidiDataException e) {
        	throw new SoundFileException("Error with audio file");
        } 
        
		this.volume = 1;
		this.length = this.midiSequencer.getMicrosecondLength();
    }
    
	/**
	 * Play the Midi.
	 **/
	public void play() {
		
		this.setVolume(this.volume);
		this.midiSequencer.start();		
	}
	
	/**
	 * Stop the Midi. Reset its position to the beginning.
	 **/
	public void stop() {
		
		this.midiSequencer.stop();
		this.midiSequencer.setMicrosecondPosition(0);
	}
	
	/**
	 * Pause the Midi.
	 **/
	public void pause() {
		
		this.midiSequencer.stop();
	}
	
	/**
	 * Get the current position of the Midi.
	 *
	 * @return <code>long</code> denoting the current position in seconds.
	 **/
	public long getPosition() {
	
		return this.midiSequencer.getMicrosecondPosition()/1000;
	}
	
	/**
	 * Set the playback position of the Midi.
	 *
	 * @param value <code>long</code> denoting the desired position of the Midi.
	 **/
	public void setPosition(long value) {
		
		value = value * 1000;

		if ((value < 0) || (value > this.length)) return;
		
		this.midiSequencer.setMicrosecondPosition(value);
	}
	
	/**
	 * Get the lenght of the Midi.
	 *
	 * @return <code>long</code> denoting the lenght of the Midi in seconds.
	 **/
	public long getLength() {
		
		return this.length/1000;
	}
	
	/**
	 * Set the volume of the Midi playback. This function is not supported by
	 * all JDKs.
	 *
	 * @param value <code>double</code> denoting the desired volume with a value
	 * found between 0 (silence) and 1 (max volume).
	 **/
	public void setVolume(double value) {
		
		if ( (value <0) || (value >1) ) return;
		
		this.volume = value;
		
		if (this.midiSequencer instanceof Synthesizer) {
			
			Synthesizer synthesizer = (Synthesizer)this.midiSequencer;
			MidiChannel[] channels = synthesizer.getChannels();

			for (int i=0; i<channels.length; i++) {
				if (channels[i] != null) {
					channels[i].controlChange(7, (int)(value * 127.0));
				}
			}
		}		
	}	
}