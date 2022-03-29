package networking;

// will be used to record a game when we save it
// will contain very similar information to a GameState, except only with stuff that's serializable

import enums.GameVariant;
import enums.RoundPhaseType;
import gamemanager.GameManager;

import java.io.*;

public class Savegame implements Serializable {

    private GameState state;

    // serializable fields from GameState
    private int totalRounds;
    private GameVariant gameVariant;

    private int currentRound;
    private RoundPhaseType currentPhase;
    private int passedPlayerCount;

    private int goldCardDeckCount;


    /**
     * creates a savegame from a GameState
     * @param pState
     */
    private Savegame(GameState pState)
    {
        // use the game state information to make this serializable
        // record the serializable fields directly from the state
        totalRounds = pState.getTotalRounds();
        gameVariant = pState.getGameVariant();
        currentRound = pState.getCurrentRound();
        currentPhase = pState.getCurrentPhase();
        passedPlayerCount = pState.getPassedPlayerCount();
        

        state = pState;
    }

    /**
     * @pre GameState instance is not null
     */
    public static void saveGameToFile() throws IOException
    {
        // create a savegame object and write it to a file
        Savegame save = new Savegame(GameState.instance());
        // if the directory for savegames does not exist locally, create it
        String dirPath = "out/saves";
        File dir = new File(dirPath);
        if (!dir.exists())
        {
            dir.mkdir(); // create the directory for savegames
        }

        // now, we can write the game to a file
        String saveGameFilepath = dirPath + "/" + GameManager.getInstance().getSessionID();
        // first, we need to create the file itself so that we can write to it
        File saved = new File(saveGameFilepath);
        if (saved.exists()) // should be triggered every time
        {
            saved.createNewFile();
        }
        FileOutputStream write = new FileOutputStream(saveGameFilepath);
        ObjectOutputStream stuff = new ObjectOutputStream(write);
        stuff.writeObject(save);
        stuff.close();

    }


}
