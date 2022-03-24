package gamemanager;

import commands.AddPlayerCommand;
import commands.GetBootColourCommand;
import domain.*;
import enums.Colour;
import enums.GameVariant;
import loginwindow.ChooseBootWindow;
import loginwindow.MainFrame;
import networking.*;
import panel.ElfBootPanel;
import gamescreen.GameScreen;
import utils.GameRuleUtils;
import utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class GameManager {

    private static GameManager INSTANCE; // Singleton instance
    private final static Logger LOGGER = Logger.getLogger("Game Manager");

    protected GameState gameState;
    protected ActionManager actionManager;
    protected Player thisPlayer = null; // represents the Player who is using this GUI, set by ChooseBootWindow
    protected boolean loaded;
    protected GameVariant variant;

    // stuff for managing networking operations
    protected String sessionID;
    protected CommunicationsManager coms;

    // manages choosing a boot colour
    protected ArrayList<Colour> availableColours = new ArrayList<>();
    protected HashMap<Colour, String> bootColours = new HashMap<>(); // <boot colour, player IP> TODO change to Player

    GameManager(Optional<GameState> loadedState, String pSessionID, GameVariant variant) {
        MainFrame.mainPanel.add(GameScreen.init(MainFrame.getInstance(), variant), "gameScreen");
        sessionID = pSessionID;
        this.variant = variant;

        // start a new game if there is no state to be loaded
        if (loadedState.isEmpty()) {
            gameState = GameState.init(pSessionID, variant);
            actionManager = ActionManager.init(gameState, this);

            loaded = false;

            availableColours.addAll(Arrays.asList(Colour.values())); // all colours are available
        }

        // load state
        else {
            gameState = loadedState.get();
            loaded = true;
            //TODO implement
        }

        coms = CommunicationsManager.init(this, sessionID);
    }

    public static GameManager init(Optional<GameState> loadedState, String sessionID, GameVariant variant) {
        if (INSTANCE == null) {
            if (GameRuleUtils.isElfengoldVariant(variant)) {
                INSTANCE = new EGGameManager(loadedState, sessionID, variant);
            } else {
                INSTANCE = new ELGameManager(loadedState, sessionID, variant);
            }
        }
        return INSTANCE;
    }

    public static GameManager getInstance() {
        return INSTANCE;
    }

    /**
     * Called when "start" is clicked
     */
    public void initPlayers() {
        // Make sure not to do any player list iteration before this block of code, as players are not
        // fully initialized. If you want to do anything right after players are fully initialized,
        // add it to launch(). Also check AddPlayerCommand.

        // initialize all the players now that the game has been launched and everyone is in
        try {
            ArrayList<String> players = GameSession.getPlayerNames(sessionID);
            String localPlayerName = getThisPlayer().getName();

            AddPlayerCommand cmd = new AddPlayerCommand(localPlayerName, thisPlayer.getColour());
            try {
                getComs().sendGameCommandToAllPlayers(cmd);
            } catch (Exception ugh) {
                ugh.printStackTrace();
            }

            // now we have ordered all the players, so we should sort them
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when we have all the players in the list. Any initialization that utilizes the player list
     * should be put here.
     */
    public void launch() {
        LOGGER.info("We have all players' info ready, setting up the rounds");
        gameState.sortPlayers();
        gameState.setToFirstPlayer();
        System.out.print(gameState.getPlayers());

        if (!loaded) setUpNewGame();

        GameScreen.getInstance().draw();
        MainFrame.cardLayout.show(MainFrame.mainPanel, "gameScreen");

        initializeElfBoots();
        setUpRound();
    }

    /**
     * Called by ChooseBootWindow after the user chooses a boot colour
     * Sets the Player of "this" GUI
     */
    public void setThisPlayer(Player p) {
        thisPlayer = p;
        gameState.addPlayer(p);
    }

    protected abstract void setUpNewGame();

    public abstract void setUpRound();

    public abstract void returnCounter(CounterUnit toKeep);

    public abstract void endTurn();

    public abstract void endPhase();

    public abstract void endRound();

    public abstract void endGame();

    public void initializeElfBoots() {

        ElfBootPanel elvenholdBootPanel = GameMap.getInstance().getTown("Elvenhold").getElfBootPanel();

        for (Player p : gameState.getPlayers()) {
            gameState.addElfBoot(new ElfBoot(p.getColour(), GameScreen.getInstance().getWidth(), GameScreen.getInstance().getHeight(), elvenholdBootPanel, GameScreen.getInstance()));
        }
    }

    protected void updateGameState() {
        GameScreen.getInstance().updateAll();
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void requestAvailableColours() {
        try {
            if (GameSession.isCreator(User.getInstance(), sessionID)) { // I am the creator of the session
                showBootWindow(); // all colours are available, don't need to send any commands
            } else {
                // ask the existing players for their colours
                String localAddress = NetworkUtils.getLocalIPAddPort();
                coms.sendGameCommandToAllPlayers(new GetBootColourCommand(localAddress));
            }
        } catch (IOException e) {
            System.out.println("There was a problem sending the command to get players' boot colours!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("There was a problem getting the local IP.");
            e.printStackTrace();
        }
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public CommunicationsManager getComs() {
        return coms;
    }


    public void removeAvailableColour(Colour c, String playerIP) {
        availableColours.remove(c);
        addPairToBootColours(c, playerIP);

        try {
            if (thisPlayer == null) { // I haven't chosen a boot colour yet
                int numPlayers = GameSession.getPlayerNames(sessionID).size();

                if (availableColours.size() == 7 - numPlayers) { // we have received the boot colours from all players who have joined
                    showBootWindow();
                }
            }
        } catch (IOException e) {
            System.out.println("There was a problem getting the players' names in the session.");
            e.printStackTrace();
        }
    }

    public void addPairToBootColours(Colour c, String playerIP) {
        bootColours.put(c, playerIP);
    }

    public String getSessionID() {
        return sessionID;
    }

    public boolean isLocalPlayerTurn() {
        return thisPlayer.equals(gameState.getCurrentPlayer());
    }

    public void showBootWindow() {
        ChooseBootWindow window = new ChooseBootWindow(sessionID, availableColours);
        MainFrame.mainPanel.add(window, "choose-boot");
        MainFrame.cardLayout.show(MainFrame.mainPanel, "choose-boot");
    }

    public GameVariant getVariant() {
        return this.variant;
    }

}
