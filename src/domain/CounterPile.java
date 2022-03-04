package domain;

import enums.CounterType;
import loginwindow.MainFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 * The face-down pile of transportation counters
 */
public class CounterPile {

    private Stack<TransportationCounter> counters;
    private int seed = 3; // TODO somehow make the seed different for each game

    public CounterPile() {
        this.counters = new Stack<>();

        for (CounterType type : CounterType.values()) {
            for (int i = 0; i < 8; i++) {
                counters.add(new TransportationCounter(type, MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900));
            }
        }

        shuffle();
    }

    public TransportationCounter draw() {return counters.pop();}

    public Stack<TransportationCounter> getCounters() {return this.counters;}

    public void shuffle() {Collections.shuffle(this.counters, new Random(seed));}

    public int getSize() {
        return counters.size();
    }
}
