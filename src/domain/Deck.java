package domain;

import java.util.*;

public abstract class Deck<E extends Drawable> {

    // basically a composite version of drawable()

    long seed; // the seed for any random operations. will be the game session ID
    Random rand; // the actual random object, created with the seed, that will be used to perform shuffle operations
    LinkedList<E> components; // the actual deck

    protected Deck (String sessionID, ArrayList<E> pComponents) // the sessionID will be turned into an integer and used as a seed
    {
        sessionID = sessionID.trim(); // just in case there were some lingering whitespaces or something
        seed = Long.parseLong(sessionID);
        rand = new Random(seed);
        components = new LinkedList<>();
        components.addAll(pComponents);

        // shuffle upon creation
        shuffle();
    }

    protected Deck (String sessionID) // to initialize an empty deck
    {
        sessionID = sessionID.trim();
        seed = Long.parseLong(sessionID);
        rand = new Random(seed);
        components = new LinkedList<>();
    }

    public void addDrawable(E toAdd) {this.components.add(toAdd);}

    public void addDrawables(List<? extends E> toAdds) {toAdds.forEach(this::addDrawable);}

    // shuffle every time we draw or just upon initialization?
    public E draw () {return components.removeFirst();}

    public void shuffle() {Collections.shuffle(components, rand);}

    public int getSize() {return components.size();}

    // should probably not return the actual reference
    public List<E> getComponents() {return components;}

    public void removeFirst(int num) {
        for (int i = 0; i < num; i++) {
            components.removeFirst();
        }
    }
}
