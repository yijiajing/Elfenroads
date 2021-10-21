import java.util.ArrayList;
import java.util.Stack;

public class Deck {

    // this is a preliminary version of Deck designed for the UI demo

    // it will be used to simulate the deck of transportation counters

    // will have limited functionality for now

    private Stack<TransportationCounter> contents;

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
    }

    public Deck (Stack<TransportationCounter> pContents)
    {
        for (TransportationCounter cur : pContents)
        {
            this.contents.push(cur);
        }
    }


    public TransportationCounter draw (Object destination)
    {
        return contents.pop();
    }



}
