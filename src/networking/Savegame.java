package networking;

// will be used to record a game when we save it
// will contain very similar information to a GameState, except only with stuff that's serializable

import java.io.Serializable;

public class Savegame implements Serializable {

    /**
     * creates a savegame from a GameState
     * @param state
     */
    private Savegame(GameState state)
    {

    }



}
