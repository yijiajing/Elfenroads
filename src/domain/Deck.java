package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public abstract class Deck<E extends Drawable> {

    // basically a composite version of drawable()

    long seed; // the seed for any random operations. will be the game session ID
    Random rand; // the actual random object, created with the seed, that will be used to perform shuffle operations
    Stack<E> components; // the actual deck

    protected Deck (String sessionID, ArrayList<E> pComponents) // the sessionID will be turned into an integer and used as a seed
    {
        sessionID = sessionID.trim(); // just in case there were some lingering whitespaces or something
        seed = Long.parseLong(sessionID);
        rand = new Random(seed);
        components = new Stack<>();
        components.addAll(pComponents);

        // shuffle upon creation
        shuffle();
    }

    protected Deck (String sessionID) // to initialize an empty deck
    {
        sessionID = sessionID.trim();
        seed = Long.parseLong(sessionID);
        rand = new Random(seed);
        components = new Stack<>();
    }

    public void addDrawable(E toAdd) {this.components.add(toAdd);}

    // shuffle every time we draw or just upon initialization?
    public Drawable draw () {return components.pop();}

    public void shuffle() {Collections.shuffle(components, rand);}

    public int getSize() {return components.size();}

    // should probably not return the actual reference
    public Stack<E> getComponents() {return components;}










}
