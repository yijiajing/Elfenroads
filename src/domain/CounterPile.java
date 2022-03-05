package domain;

import enums.CounterType;
import loginwindow.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 * The face-down pile of transportation counters
 */
public class CounterPile <CounterUnit> extends Deck <CounterUnit> {

    public CounterPile(String sessionID) {

        super(sessionID);

        for (CounterType type : CounterType.values()) {
            for (int i = 0; i < 8; i++) {
                components.add(new TransportationCounter(type, MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900));
            }
        }

        shuffle();
    }
}
