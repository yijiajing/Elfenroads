package networking;

// will be used to record a game when we save it
// will contain very similar information to a GameState, except only with stuff that's serializable

import gamemanager.GameManager;

import java.io.*;

public class Savegame implements Serializable {

    private GameState state;

    /**
     * creates a savegame from a GameState
     * @param pState
     */
    private Savegame(GameState pState)
    {
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
