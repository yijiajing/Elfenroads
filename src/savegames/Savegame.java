package savegames;

// will be used to record a game when we save it
// will contain very similar information to a GameState, except only with stuff that's serializable

import domain.*;
import enums.GameVariant;
import enums.MagicSpellType;
import enums.RoundPhaseType;
import enums.TravelCardType;
import gamemanager.GameManager;
import networking.CommunicationsManager;
import networking.GameSession;
import networking.GameState;
import networking.User;
import utils.GameRuleUtils;
import windows.LobbyWindow;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private String saveGameID; // saves savegameid for later
    private String creatorName; // used to determine who can create the session upon load

    private String gameName; // the game name in the LS (we can obtain this using the variant)

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
        gameName = LobbyWindow.variantToGameName(gameVariant);


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

        // generate a saveGameID and register a savegame to the LS
        // we will use the session ID as the savegameid. It's the easiest thing to do
        saveGameID = sessionID;
        try {creatorName = GameSession.getCreatorName(sessionID);}
        catch (Exception e)
        {
            Logger.getGlobal().info("There was a problem recording the creator name to save to the file.");
        }

        // register at the LS
        try {registerSavegame(saveGameID);}
        catch (IOException e)
        {
            Logger.getGlobal().severe("There was a problem registering the savegame at the LS.");
            e.printStackTrace();
        }
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
        // we will save the file according to a certain convention
        String saveGameFilepath = dirPath + "/" + GameManager.getInstance().getSessionID() + "_" + GameManager.getInstance().getGameState().getGameVariant() + "_ROUND" + GameManager.getInstance().getGameState().getCurrentRound();
        // first, we need to create the file itself so that we can write to it
        File saved = new File(saveGameFilepath);
        if (saved.exists()) // should be triggered every time
        {
            saved.createNewFile();
            saved = new File(saveGameFilepath);
        }
        FileOutputStream write = new FileOutputStream(saved);
        ObjectOutputStream stuff = new ObjectOutputStream(write);
        stuff.writeObject(save);
        stuff.close();

    }

    /**
     * registers the savegame at the LS (will allow us to load it later.)
     */
    public static void registerSavegame(String pSaveGameID) throws IOException
    {
        String gameName = LobbyWindow.variantToGameName(GameManager.getInstance().getGameState().getGameVariant());
        String listOfPlayersForRegister = getPlayersListForLS();
        String token = User.getAccessTokenUsingCreds(gameName, "abc123_ABC123");

        URL url = new URL("http://3.99.137.208:4242/api/gameservices/" + gameName + "/savegames/" + pSaveGameID + "?access_token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("content-type", "application/json");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("{\n");
        out.writeBytes("\"gamename\": \"" + gameName + "\",\n");
        out.writeBytes("\"players\": " + getPlayersListForLS() + ",\n");
        out.writeBytes("\"savegameid\": \""+ pSaveGameID + "\"\n");
        out.writeBytes("}");
        out.flush();
        out.close();

        Logger.getGlobal().info("The body of the request: \n" + out.toString());

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
    }



    private void savePlayers (GameState pGameState)
    {
        players = new ArrayList<>();
        // get all the players from the GameState and turn them into serializable objects to be saved
        for (Player cur : pGameState.getPlayers())
        {
            SerializablePlayer toAdd = new SerializablePlayer(cur);
            Logger.getGlobal().info("Saving player " + toAdd.getName() + " of color " + toAdd.getColor());
            if (pGameState.getCurrentPlayer().equals(cur))
            {
                Logger.getGlobal().info(toAdd.getName() + " is the current player before saving.");
                currentPlayer = toAdd;
            }
            if (GameManager.getInstance().getThisPlayer().equals(cur))
            {
                Logger.getGlobal().info(toAdd.getName() + " is thisPlayer before saving.");
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
            Logger.getGlobal().info(crd.getType().toString());
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
     * constructs the players list as a string in a way that we can use to pass to the LS
     * will have to look like: ["player1name", "player2name"]
     */
    private static String getPlayersListForLS()
    {
        ArrayList<String> players = GameManager.getInstance().getGameState().getPlayerNames();
        String out = "[";
        int counter = 0;
        int maxCounter = players.size() - 1;
        for (String playerName : players)
        {
            if (counter < maxCounter)
            {
                out = out + "\"" + playerName + "\",";
            }
            else // last player to add, so no comma at the end
            {
                out = out + "\"" + playerName + "\"]";
            }
            counter++;
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

    public String getSaveGameID() {
        return saveGameID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getGameName() {
        return gameName;
    }
}