package domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


public class Deck {

    // this is a preliminary version of Deck designed for the UI demo

    // it will be used to simulate the deck of transportation counters

    // will have limited functionality for now

    private Stack<TransportationCounter> contents = new Stack<TransportationCounter>();

    public Deck()
    {
        this.contents = new Stack<TransportationCounter>();
    }

    public Deck (ArrayList<TransportationCounter> pContents)
    {
        for (int i = 0; i < pContents.size(); i++)
        {
            TransportationCounter cur = pContents.get(i);
            this.contents.push(cur);
        }
        shuffle();
    }

    public Deck (Stack<TransportationCounter> pContents)
    {
        for (TransportationCounter cur : pContents)
        {
            this.contents.push(cur);
        }
        
        shuffle();
    }


    public TransportationCounter draw () {return contents.pop();}

    /* public void drawToDestination(Container toPut)
    {
        toPut.add(this.draw());
    }
    */

    public Stack<TransportationCounter> getContents() {return this.contents;}

    public void shuffle() {Collections.shuffle(this.contents);}

}
