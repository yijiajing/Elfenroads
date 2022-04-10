package savegames;

// will be used to record a game when we save it
// will contain very similar information to a GameState, except only with stuff that's serializable

import domain.*;
import enums.GameVariant;
import enums.MagicSpellType;
import enums.RoundPhaseType;
import enums.TravelCardType;
import gamemanager.GameManager;
import networking.GameState;
import utils.GameRuleUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Savegame implements Serializable {

    private static final long serialVersionUID = 1234567890;

    // serializable fields from GameState
    private int totalRounds;
    private GameVariant gameVariant;

    private int currentRound;
    private RoundPhaseType currentPhase;
    private int passedPlayerCount;

    private int goldCardDeckCount;

    private String sessionID; // saves session ID (will be different once we load, but we can use this one to set up the decks and piles anyway)

    // fields turned into serializable version
    private ArrayList<SerializablePlayer> players;
    private SerializablePlayer currentPlayer;
    private SerializablePlayer thisPlayer;
    private ArrayList<SerializableCardUnit> travelCardDeck; // can contain both gold cards and travel cards
    private ArrayList<SerializableCounterUnit> counterPile;
    private ArrayList<SerializableTransportationCounter> faceUpCounters; // for elfenland classic games
    private ArrayList<SerializableTravelCard> faceUpCards; // for elfengold games

    // GameMap info
    private HashMap<Integer, ArrayList<SerializableCounterUnit>> stuffOnRoads;

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
        sessionID = GameManager.getInstance().getSessionID();


        // now, handle the non-serializable fields
        savePlayers(pState);
        saveTravelCardDeck(pState);
        saveTransportationCounterPile(pState);

        // save faceupcards for elfengold and faceupcounters for elfenland
        if (GameRuleUtils.isElfengoldVariant(gameVariant))
        {
            saveFaceUpCards(pState);
            goldCardDeckCount = pState.getGoldCardDeckCount();
        }
        else // if elfenland, save face up counters
        {
            saveFaceUpCounters(pState);
        }

        saveStuffOnRoads();

        // we will omit elf boots, since we can figure that out upon load by looking at each player, his current town, and his color
    }

    /**
     * reads a savegame in from a file
     * @param filename the filename of the savegame file (will depend on what I use as savegame filenames)
     * @return the savegame stored in that file
     * @throws FileNotFoundException if the file we try to read doesn't exist
     * @throws IOException if the file exists, but it contains something other than a Savegame object
     */
    public static Savegame read(String filename) throws Exception
    {
        FileInputStream reading = new FileInputStream("./out/saves/" + filename);
        ObjectInputStream readingObject = new ObjectInputStream(reading);
        Savegame save = (Savegame) readingObject.readObject();

        // close the streams
        readingObject.close();
        reading.close();

        // return the read savegame
        return save;
    }

    /**
     * reads in a savegame from a file
     * @param f the save file selected from the file chooser
     * @pre f is a .elf file (filtered by file chooser)
     * @return
     */
    public static Savegame read(File f) throws Exception
    {
        FileInputStream reading = new FileInputStream(f);
        ObjectInputStream readingObject = new ObjectInputStream(reading);
        Savegame save = (Savegame) readingObject.readObject();

        // close the streams
        readingObject.close();
        reading.close();

        // return the read savegame
        return save;
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
        String saveGameFilepath = dirPath + "/" + GameManager.getInstance().getSessionID() + "_ROUND" + save.getCurrentRound() + ".elf";
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
        players = new ArrayList<>();
        // get all the players from the GameState and turn them into serializable objects to be saved
        for (Player cur : pGameState.getPlayers())
        {
            SerializablePlayer toAdd = new SerializablePlayer(cur);
            if (pGameState.getCurrentPlayer().equals(cur))
            {
                currentPlayer = toAdd;
            }
            if (GameManager.getInstance().getThisPlayer().equals(cur))
            {
                thisPlayer = toAdd;
            }
            players.add(toAdd);
        }
    }

    private void saveTravelCardDeck(GameState pGameState)
    {
        TravelCardDeck origDeck = pGameState.getTravelCardDeck();
        travelCardDeck = new ArrayList<>();
        for (CardUnit cur : origDeck.getComponents())
        {
            if (cur instanceof TravelCard)
            {
                TravelCard tc = (TravelCard) cur;
                travelCardDeck.add(new SerializableTravelCard(tc));
            }
            else // if cur is a gold card
            {
                GoldCard gc = (GoldCard) cur;
                travelCardDeck.add(new SerializableGoldCard(gc));
            }

        }
    }

    private void saveTransportationCounterPile (GameState pGameState)
    {
        CounterUnitPile origPile = pGameState.getCounterPile();
        counterPile = new ArrayList<>();
        for (CounterUnit cur : origPile.getComponents())
        {
            if (cur instanceof TransportationCounter)
            {
                TransportationCounter curDowncasted = (TransportationCounter) cur;
                counterPile.add(new SerializableTransportationCounter(curDowncasted));
            }
            else if (cur instanceof MagicSpell)
            {
                MagicSpell curDowncasted = (MagicSpell) cur;
                counterPile.add(new SerializableMagicSpell(curDowncasted));
            }
            else if (cur instanceof GoldPiece)
            {
                GoldPiece curDowncasted = (GoldPiece) cur;
                counterPile.add(new SerializableGoldPiece(curDowncasted));
            }
            else // if cur is an obstacle
            {
                Obstacle curDowncasted = (Obstacle) cur;
                counterPile.add(new SerializableObstacle(curDowncasted));
            }
        }
    }

    private void saveFaceUpCounters (GameState pGameState)
    {
        faceUpCounters = new ArrayList<>();
        // save the list of face up counters for Elfenland classic games
        for (TransportationCounter ctr : pGameState.getFaceUpCounters())
        {
            faceUpCounters.add(new SerializableTransportationCounter(ctr));
        }
    }

    private void saveFaceUpCards (GameState pGameState)
    {
        faceUpCards = new ArrayList<>();
        for (TravelCard crd : pGameState.getFaceUpCards())
        {
            faceUpCards.add(new SerializableTravelCard(crd));
        }
    }

    private void saveStuffOnRoads()
    {
        stuffOnRoads = new HashMap<>();
        GameMap map = GameMap.getInstance();
        List<Road> roads = map.getRoadList();

        for (Road cur : roads)
        {
            Integer indexOfRoad = roads.indexOf(cur);
            stuffOnRoads.put(indexOfRoad, new ArrayList<>()); // put the list in the map

            for (CounterUnit ctr : cur.getCounters())
            {
                if (ctr instanceof Obstacle)
                {
                    Obstacle ctrDowncasted = (Obstacle) ctr;
                    stuffOnRoads.get(indexOfRoad).add(new SerializableObstacle(ctrDowncasted));
                }
                else if (ctr instanceof TransportationCounter) // if transportation counter
                {
                    TransportationCounter ctrDowncasted = (TransportationCounter) ctr;
                    stuffOnRoads.get(indexOfRoad).add(new SerializableTransportationCounter(ctrDowncasted));
                }
                else if (ctr instanceof GoldPiece)
                {
                    GoldPiece ctrDowncasted = (GoldPiece) ctr;
                    stuffOnRoads.get(indexOfRoad).add(new SerializableGoldPiece(ctrDowncasted));
                }
                else if (ctr instanceof MagicSpell)
                {
                    MagicSpell ctrDowncasted = (MagicSpell) ctr;
                    stuffOnRoads.get(indexOfRoad).add(new SerializableMagicSpell(ctrDowncasted));
                }
                else // this should never be the case
                {
                    Logger.getGlobal().info("Unexpected result in saveStuffOnRoads.");
                }
            }
        }
    }

    /**
     * get each player's hand from a savegame
     * @param playerName
     * @return
     */
    public Hand getHandByPlayer(String playerName)
    {
        // iterate through all of the player's cards, counters, and obstacle
        SerializablePlayer thatOne = getPlayerByName(playerName);
        // cards
        // counters
        // obstacle
        ArrayList<SerializableCardUnit> cards = thatOne.getCards();
        ArrayList<SerializableCounterUnit> counters = thatOne.getCounters();
        SerializableObstacle obstacle = thatOne.getObstacle();

        Hand out = new Hand();

        for (SerializableCardUnit cur : cards)
        {
            // could be a travelCard or a gold card
            if (cur instanceof SerializableTravelCard)
            {
                SerializableTravelCard curDowncasted = (SerializableTravelCard) cur;
                out.addUnit(new TravelCard(curDowncasted));
            }
            else // if cur is a gold card
            {
                SerializableGoldCard curDowncasted = (SerializableGoldCard) cur;
                out.addUnit(new GoldCard(curDowncasted));
            }
        }

        for (SerializableCounterUnit cur : counters)
        {
            // could be an Obstacle, a GoldPiece, a TransportationCounter, or a MagicSpell
            if (cur instanceof SerializableObstacle)
            {
                SerializableObstacle curDowncasted = (SerializableObstacle) cur;
                out.addUnit(new Obstacle(curDowncasted));
            }
            else if (cur instanceof SerializableGoldPiece)
            {
                SerializableGoldPiece curDowncasted = (SerializableGoldPiece) cur;
                out.addUnit(new GoldPiece(curDowncasted));
            }
            else if (cur instanceof SerializableTransportationCounter)
            {
                SerializableTransportationCounter curDowncasted = (SerializableTransportationCounter) cur;
                out.addUnit(new TransportationCounter(curDowncasted));
            }
            else if (cur instanceof SerializableMagicSpell)
            {
                SerializableMagicSpell curDowncasted = (SerializableMagicSpell) cur;
                out.addUnit(new MagicSpell(curDowncasted));
            }
        }

        if (obstacle != null)
        {
            out.addUnit(new Obstacle(obstacle));
        }

        return out;

    }

    /**
     * @pre players have been saved already
     */
    public SerializablePlayer getPlayerByName(String name)
    {
        for (SerializablePlayer cur : players)
        {
            if (cur.getName().equalsIgnoreCase(name))
            {
                return cur;
            }
        }
        Logger.getGlobal().info("Could not find a player from the savegame with the name " + name);
        return null;
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

    public ArrayList<SerializableCardUnit> getTravelCardDeck() {
        return travelCardDeck;
    }

    public ArrayList<SerializableCounterUnit> getCounterPile() {
        return counterPile;
    }

    public ArrayList<SerializableTransportationCounter> getFaceUpCounters() {
        return faceUpCounters;
    }

    public ArrayList<SerializableTravelCard> getFaceUpCards() {
        return faceUpCards;
    }

    public String getSessionID() {
        return sessionID;
    }

    public SerializablePlayer getThisPlayer() {
        return thisPlayer;
    }

    public HashMap<Integer, ArrayList<SerializableCounterUnit>> getStuffOnRoads() {
        return stuffOnRoads;
    }
}