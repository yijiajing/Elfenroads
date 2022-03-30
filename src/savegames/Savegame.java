package savegames;

// will be used to record a game when we save it
// will contain very similar information to a GameState, except only with stuff that's serializable

import domain.*;
import enums.GameVariant;
import enums.RoundPhaseType;
import enums.TravelCardType;
import gamemanager.GameManager;
import networking.GameState;

import java.io.*;
import java.util.ArrayList;

public class Savegame implements Serializable {

    // serializable fields from GameState
    private int totalRounds;
    private GameVariant gameVariant;

    private int currentRound;
    private RoundPhaseType currentPhase;
    private int passedPlayerCount;

    private int goldCardDeckCount;

    // fields turned into serializable version
    private ArrayList<SerializablePlayer> players;
    private SerializablePlayer currentPlayer;
    private ArrayList<SerializableTravelCard> travelCardDeck;
    private ArrayList<SerializableCounterUnit> counterPile;


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

        // now, handle the non-serializable fields
        savePlayers(pState);
        saveTravelCardPile(pState);
        saveTransportationCounterPile(pState);

        // we will omit elf boots, since we can figure that out upon load by looking at each player, his current town, and his color
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

    private void savePlayers (GameState pGameState)
    {
        // get all the players from the GameState and turn them into serializable objects to be saved
        for (Player cur : pGameState.getPlayers())
        {
            SerializablePlayer toAdd = new SerializablePlayer(cur);
            if (pGameState.getCurrentPlayer().equals(cur))
            {
                currentPlayer = toAdd;
            }

            players.add(toAdd);
        }
    }

    private void saveTravelCardPile(GameState pGameState)
    {
        TravelCardDeck origDeck = pGameState.getTravelCardDeck();
        for (CardUnit cur : origDeck.getComponents()) // I think we can safely downcast this to a TravelCard
        {
            TravelCard tc = (TravelCard) cur;
            travelCardDeck.add(new SerializableTravelCard(tc));
        }
    }

    private void saveTransportationCounterPile (GameState pGameState)
    {
        CounterUnitPile origPile = pGameState.getCounterPile();
        for (CounterUnit cur : origPile.getComponents())
        {
            counterPile.add(new SerializableCounterUnit(cur));
        }
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public GameVariant getGameVariant() {
        return gameVariant;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public RoundPhaseType getCurrentPhase() {
        return currentPhase;
    }

    public int getPassedPlayerCount() {
        return passedPlayerCount;
    }

    public int getGoldCardDeckCount() {
        return goldCardDeckCount;
    }

    public ArrayList<SerializablePlayer> getPlayers() {
        return players;
    }

    public SerializablePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<SerializableTravelCard> getTravelCardDeck() {
        return travelCardDeck;
    }

    public ArrayList<SerializableCounterUnit> getCounterPile() {
        return counterPile;
    }
}
