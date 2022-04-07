package gamemanager;

import commands.*;
import domain.*;
import enums.Colour;
import enums.EGRoundPhaseType;
import enums.ELRoundPhaseType;
import enums.GameVariant;
import windows.ChooseBootWindow;
import windows.MainFrame;
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


    GameManager(Optional<GameState> loadedState, String pSessionID, GameVariant variant, String pLocalAddress) {
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

        coms = CommunicationsManager.init(this, sessionID, pLocalAddress);
    }

    public static GameManager init(Optional<GameState> loadedState, String sessionID, GameVariant variant, String pLocalAddress) {
        if (INSTANCE == null) {
            Logger.getGlobal().info("Initializing GameManager");
            if (GameRuleUtils.isElfengoldVariant(variant)) {
                INSTANCE = new EGGameManager(loadedState, sessionID, variant, pLocalAddress);
            } else {
                INSTANCE = new ELGameManager(loadedState, sessionID, variant, pLocalAddress);
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

    public abstract void setUpNewGame();

    public abstract void setUpRound();

    public abstract void returnCounter(CounterUnit toKeep);

    /**
     * Called once by each peer within a phase. From here we might go to the next phase and next round.
     * If we are still in the same phase, we notify the next peer in list to take action.
     */
    // totalRounds Round <--in-- numOfRoundPhaseType Phases <--in-- numOfPlayer Turns
    public void endTurn() {
        GameScreen.getInstance().updateAll(); // update the GUI
        actionManager.clearSelection();

        // all players have passed their turn in the current phase
        if (gameState.getCurrentPlayerIdx() + 1 == gameState.getNumOfPlayers()) {
            // Since players take turns, only one player will first reach endPhase from endTurn.
            // We then tell everyone to end phase.
            GameCommand endPhaseCommand = new EndPhaseCommand();
            endPhaseCommand.execute(); // execute locally before sending to everyone else
            try {
                coms.sendGameCommandToAllPlayers(endPhaseCommand);
            } catch (IOException e) {
                System.out.println("There was a problem sending the endPhaseCommand to all players.");
                e.printStackTrace();
            }
        } else {
            // within the same phase, next player will take action
            gameState.setToNextPlayer();
            NotifyTurnCommand notifyTurnCommand = new NotifyTurnCommand(gameState.getCurrentPhase());
            try {
                LOGGER.info("Notifying " + gameState.getCurrentPlayer().getName() + " to take action.");
                coms.sendCommandToIndividual(notifyTurnCommand, gameState.getCurrentPlayer().getName());
            } catch (IOException e) {
                LOGGER.info("There was a problem sending the command to take turns!");
                e.printStackTrace();
            }
        }
    }

    public abstract void endPhase();

    /**
     * Trigger for every peers when endPhase is called
     */
    public void endRound() {
        gameState.incrementCurrentRound();
        LOGGER.info("...Going to the next round #" + gameState.getCurrentRound());
        LOGGER.info("Total: " + gameState.getTotalRounds() + ", Current: " + gameState.getCurrentRound());
        if (gameState.getCurrentRound() > gameState.getTotalRounds()) {
            LOGGER.info("Total: " + gameState.getTotalRounds() + ", Current: " + gameState.getCurrentRound());
            endGame();
            return;
        }
        actionManager.clearSelection();

        GameMap.getInstance().clearAllCounters();
        GameState.instance().getCounterPile().shuffle();

        GameScreen.getInstance().initializeRoundCardImage(gameState.getCurrentRound()); // update round card image
        setUpRound(); // start next round
    }

    public abstract void endGame();

    public void initializeElfBoots() {

        ElfBootPanel elvenholdBootPanel = GameMap.getInstance().getTown("Elvenhold").getElfBootPanel();

        for (Player p : gameState.getPlayers()) {
            gameState.addElfBoot(new ElfBoot(p.getColour(), GameScreen.getInstance().getWidth(), GameScreen.getInstance().getHeight(), elvenholdBootPanel, GameScreen.getInstance()));
        }
    }

    public void planTravelRoutes() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && (gameState.getCurrentPhase() == ELRoundPhaseType.PLAN_ROUTES
                || gameState.getCurrentPhase() == EGRoundPhaseType.PLAN_ROUTES)
                && isLocalPlayerTurn()) {

            updateGameState();
            System.out.println("Current phase: PLAN TRAVEL ROUTES");

            // display message
            if (gameState.getCurrentPhase() == ELRoundPhaseType.PLAN_ROUTES
                    || gameState.getCurrentPhase() == EGRoundPhaseType.PLAN_ROUTES) {
                GameScreen.displayMessage("""
                        It is time to plan your travel routes! Begin by clicking the transportation counter in your hand that you want to use, then click on the road that you want to travel.
                        The chart in the bottom right corner indicates which transportation counters may be used on which road.
                        Alternatively, you may choose to place your Obstacle on a road that already has a counter. But be warned... you can only do this once!
                        When you are done placing one counter, click "End Turn". Alternatively, you can pass your turn by clicking "End Turn".
                        """);
            }
        }
    }

    public void moveOnMap() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && (gameState.getCurrentPhase() == ELRoundPhaseType.MOVE
                || gameState.getCurrentPhase() == EGRoundPhaseType.MOVE)
                && gameState.getCurrentPlayer().equals(thisPlayer)) {

            updateGameState();
            System.out.println("Current phase: MOVE ON MAP");

            // display message
            GameScreen.displayMessage("""
                    It is time to travel across the map and collect your town pieces! Begin by clicking the travel card(s) that you want to use, then click on the town that you want to travel to.
                    The number of required travel cards depends on the region and is indicated by the chart in the bottom right corner. 
                    You can repeat this as many times as you want. When you are done travelling, click "End Turn". 
                    """);

            // logic implemented in ActionManager
        }
    }

    public abstract void returnCountersPhase();

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
