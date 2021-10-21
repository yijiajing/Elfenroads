import java.util.ArrayList;

public class Deck {

    // this is a preliminary version of Deck designed for the UI demo

    // it will be used to simulate the deck of transportation counters

    // will have limited functionality for now

    private ArrayList<TransportationCounter> contents;

    public Deck()
    {
        this.contents = new ArrayList<TransportationCounter>();
    }

    public Deck (ArrayList<TransportationCounter> pContents)
    {
        for (int i = 0; i < pContents.size(); i++)
        {
            TransportationCounter cur = pContents.get(i);
            this.contents.set(i, cur);
        }
    }

    public draw ()



}
