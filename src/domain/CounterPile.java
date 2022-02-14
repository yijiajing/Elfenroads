package domain;

import enums.CounterType;
import loginwindow.MainFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * The face-down pile of transportation counters
 */
public class CounterPile {

    private Stack<TransportationCounter> counters;

    public CounterPile() {
        this.counters = new Stack<>();

        for (CounterType type : CounterType.values()) {
            for (int i = 0; i < 8; i++) {
                counters.add(new TransportationCounter(type, MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*52/900));
            }
        }

        Collections.shuffle(counters);
    }

    public TransportationCounter draw() {return counters.pop();}

    public Stack<TransportationCounter> getCounters() {return this.counters;}

    public void shuffle() {Collections.shuffle(this.counters);}

    public int getSize() {
        return counters.size();
    }
}
