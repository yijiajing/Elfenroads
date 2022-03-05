package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public abstract class Deck {

    // basically a composite version of drawable()

    long seed; // the seed for any random operations. will be the game session ID
    Random rand; // the actual random object, created with the seed, that will be used to perform shuffle operations
    Stack<Drawable> components; // the actual deck

    protected Deck (String sessionID, ArrayList<Drawable> pComponents) // the sessionID will be turned into an integer and used as a seed
    {
        sessionID = sessionID.trim(); // just in case there were some lingering whitespaces or something
        seed = Long.parseLong(sessionID);
        rand = new Random(seed);
        for (Drawable toAdd : pComponents)
        {
            components.add(toAdd);
        }

        // shuffle upon creation
        shuffle();
    }

    public void addDrawable(Drawable toAdd) {this.components.add(toAdd);}

    // shuffle every time we draw or just upon initialization?
    public Drawable draw () {return components.pop();}

    public void shuffle() {Collections.shuffle(components, rand);}










}
