/*
 * @(#)MusicStore.java
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
import java.util.Hashtable;

import org.minueto.sample.fireinthesky.sound.SoundFileException;
import org.minueto.sample.fireinthesky.sound.SoundMidi;
import org.minueto.sample.fireinthesky.sound.SoundSystemUnavailableException;
import org.minueto.sample.fireinthesky.sound.SoundWave;

class MusicStore {

	private Hashtable<String, SoundMidi> midis;
	private Hashtable<String, SoundWave> waves;
		
	public MusicStore() {
				
		this.midis = new Hashtable<String, SoundMidi>();
		this.waves = new Hashtable<String, SoundWave>();
		
		this.loadMidi("intro", "sounds/intro.mid");
		this.loadMidi("attack", "sounds/attack.mid");
		
		this.loadWave("gun", "sounds/gun.wav", 12);
		this.loadWave("gun2", "sounds/gun2.wav", 12);
		this.loadWave("hit", "sounds/hit.wav", 4);
		this.loadWave("ding", "sounds/ding.wav", 4);
				
        this.setWaveVolume("hit", 0.5);
        
	}
	
	public void loadMidi(String name, String location) {
		
		SoundMidi midi = null;
		URL midiLocation = Thread.currentThread().getContextClassLoader().getResource(location);
		        
        if (midiLocation != null) {
            try {
            	midi = new SoundMidi(midiLocation);
			} catch (SoundFileException e) {
				System.err.println("Could not find sound file " + location);
			} catch (SoundSystemUnavailableException e) {
				System.err.println("Midi not available");
			}
        }
        
        if (midi != null) {
        	this.midis.put(name, midi);
        }
        
        midi.setVolume(0.5);
	}
	
	public void loadWave(String name, String location, int concurrent) {
		
		SoundWave wave = null;
		URL waveLocation = Thread.currentThread().getContextClassLoader().getResource(location);
		        
        if (waveLocation != null) {
            try {
            	wave = new SoundWave(waveLocation, concurrent);
			} catch (SoundFileException e) {
				System.err.println("Could not find sound file " + location);
			} catch (SoundSystemUnavailableException e) {
				System.err.println("Wave not available");
			}
        }
           
        if (wave != null) {
        	this.waves.put(name, wave);        
        }
	}
	
	public void setWaveVolume(String name, double volume) {

		SoundWave wave = this.waves.get(name);
		
		if(wave != null) {
			wave.setVolume(volume);
		}
	}
	
	public void playMidi(String name) {

		SoundMidi midi = this.midis.get(name);
		
		if(midi != null) {
			midi.play();
		}
	}
	
	public void stopMidi(String name) {
		
		SoundMidi midi = this.midis.get(name);
		
		if(midi != null) {
			midi.stop();
		}
	}
	
	public void playWave(String name) {

		SoundWave wave = this.waves.get(name);
		
		if(wave != null) {
			wave.play();
		}
	}
	
	

}