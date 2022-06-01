package windows;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;
// Code from https://introcs.cs.princeton.edu/java/faq/mp3/MP3.java.html
import javazoom.jl.player.advanced.AdvancedPlayer;

public class MP3Player {
    private String filename;
    private Player player;


    // constructor that takes the name of an MP3 file
    public MP3Player(String filename) {
        this.filename = filename;
    }

    public void close() {
        if (player != null) player.close();
    }

    // play the MP3 file to the sound card
    public void playReapeated() {
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        } catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try {
                    do {
                        FileInputStream buff = new FileInputStream(filename);
                        AdvancedPlayer prehravac = new AdvancedPlayer(buff);
                        prehravac.play();
                    } while (true);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();


    }

    public void play() {
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        } catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try {
                    player.play();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();
    }
}