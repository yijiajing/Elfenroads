package gamemanager;

import commands.*;
import domain.*;
import enums.Colour;
import enums.EGRoundPhaseType;
import enums.ELRoundPhaseType;
import enums.GameVariant;
import savegames.Savegame;
import utils.NetworkUtils;
import windows.*;
import networking.*;
import panel.ElfBootPanel;
import gamescreen.GameScreen;
import utils.GameRuleUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private int bootNotifsReceived;
    private int cardNotifsReceived;


    GameManager(Optional<Savegame> savegame, String pSessionID, GameVariant variant, String pLocalAddress) {
        MainFrame.mainPanel.add(GameScreen.init(MainFrame.getInstance(), variant), "gameScreen");
        sessionID = pSessionID;
        this.variant = variant;

        // start a new game if there is no state to be loaded
        if (savegame.isEmpty()) {
            gameState = GameState.init(pSessionID, variant);
            actionManager = ActionManager.init(gameState, this);

            loaded = false;

            availableColours.addAll(Arrays.asList(Colour.values())); // all colours are available
        }

        // load state
        else {
            gameState = GameState.initFromSave(savegame.get(), this);
            loaded = true;
            actionManager = ActionManager.init(gameState, this);
            // set up a session with the savegame id
            // if the local user is the creator, then we can start a session
            if (User.getInstance().getUsername().equals(savegame.get().getCreatorName()))
            {
                try {
                    Logger.getGlobal().info("The savegame ID we should create a session for is " + savegame.get().getSaveGameID());
                    Logger.getGlobal().info("The game variant of the savegame is " + savegame.get().getGameVariant());
                    // GameSession loadedSession = new GameSession(User.getInstance(), LobbyWindow.variantToGameName(savegame.get().getGameVariant()), savegame.get().getSaveGameID());
                    GameSession loadedSession = new GameSession(User.getInstance(), savegame.get().getGameName(), savegame.get().getSaveGameID());
                    sessionID = loadedSession.getId();
                    MainFrame.mainPanel.add(new HostWaitWindow(sessionID), "hostWaitingRoom");
                    MainFrame.cardLayout.show(MainFrame.mainPanel, "hostWaitingRoom");
                }
                catch (Exception e)
                {
                    Logger.getGlobal().severe("There was a problem setting up the GameSession for the loaded game.");
                    e.printStackTrace();
                }

            }
            else // if we were not the creator of the original session, we need to join the host.
            {
                // check if the session with the savegameid already exists. if so, join it
                try
                {
                    String idOfTheSession = GameSession.lookupSessionBySavegame(savegame.get().getSaveGameID());
                    // if that returned null, the host has not created the session and we need to wait for him.
                    if (idOfTheSession == null)
                    {
                        // tell the user that we need to wait until the game creator starts up the session
                        JOptionPane.showMessageDialog(null, "You must wait for the original creator, " + savegame.get().getCreatorName() + ", to load that game first. Going back to the LobbyWindow.");
                        LobbyWindow reinitialized = new LobbyWindow();
                        MainFrame.setLobbyWindow(reinitialized);
                        MainFrame.mainPanel.add(reinitialized, "lobby");
                        MainFrame.cardLayout.show(MainFrame.mainPanel, "lobby");
                    }

                    // we found the session, so we can join it.
                    String localIP = NetworkUtils.getLocalIPAddPort();
                    GameSession.joinSession(MainFrame.loggedIn, idOfTheSession, localIP);
                    // now that we've joined, set the session ID to the proper value
                    sessionID = idOfTheSession;
                    // take the player to the waiting window (bypass boot selection)
                    // initialize a new PlayerWaitWindow
                    PlayerWaitWindow updated = new PlayerWaitWindow(sessionID);
                    MainFrame.setPlayerWaitWindow(updated);
                    MainFrame.mainPanel.add(updated, "playerWaitingRoom");
                    MainFrame.cardLayout.show(MainFrame.mainPanel, "playerWaitingRoom");
                }
                catch (Exception e)
                {
                    Logger.getGlobal().info("There was a problem finding and joining a session with that saveGameID");
                    e.printStackTrace();
                }

            }
        }

        coms = CommunicationsManager.init(this, sessionID, pLocalAddress);
    }

    public static GameManager init(Optional<Savegame> savegame, String sessionID, GameVariant variant, String pLocalAddress) {
        if (INSTANCE == null) {
            Logger.getGlobal().info("Initializing GameManager");
            if (GameRuleUtils.isElfengoldVariant(variant)) {
                INSTANCE = new EGGameManager(savegame, sessionID, variant, pLocalAddress);
            } else {
                INSTANCE = new ELGameManager(savegame, sessionID, variant, pLocalAddress);
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
            // ArrayList<String> players = GameSession.getPlayerNames(sessionID);
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

        // if the game was loaded from a save, jump right into the GameScreen
        if (!loaded)
        {
            LOGGER.info("We have all players' info ready, setting up the rounds");
            gameState.sortPlayers();
            gameState.setToFirstPlayer();
            System.out.print(gameState.getPlayers());

            setUpNewGame();
        }

        GameScreen.getInstance().draw();
        MainFrame.cardLayout.show(MainFrame.mainPanel, "gameScreen");

        if (!loaded) {
            initializeElfBoots();
            Logger.getGlobal().info("Setting up round, no saved game.");
            setUpRound();
        } else {
            Logger.getGlobal().info("Setting up round from saved game.");
            setUpRoundFromSaved();
        }

    }

    /**
     * Called by ChooseBootWindow after the user chooses a boot colour
     * Sets the Player of "this" GUI
     */
    public void setThisPlayer(Player p) {
        thisPlayer = p;
        gameState.addPlayer(p);
    }

    /**
     * special method to get around some weirdness with singleton intialization orders
     * will take a GameState as parameter
     * @param p
     */
    public void setThisPlayerLoaded(Player p, GameState pGameState)
    {
        thisPlayer = p;
        pGameState.addPlayer(p);
    }

    public abstract void setUpNewGame();

    public abstract void setUpRound();

    public void setUpRoundFromSaved() {
        // inform player of their destination town
        if (gameState.getGameVariant() == GameVariant.ELFENGOLD_DESTINATION) {
            GameScreen.displayMessage("Your destination Town is " +
                    thisPlayer.getDestinationTown().getName() + ". Please collect town pieces and have your travel " +
                    "route end in a town as close as possible to the destination at the end of the game.");
        }

        Logger.getGlobal().info("In setUpRoundFromSaved()");

        // Triggered only on one instance (the first player)
        if (isLocalPlayerTurn()) {
            Logger.getGlobal().info("Executing notify turn command locally with phase " + gameState.getCurrentPhase());
            NotifyTurnCommand cmd = new NotifyTurnCommand(gameState.getCurrentPhase());
            cmd.execute();
            GameScreen.getInstance().updateAll();
        }
    }

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
            LOGGER.info("Executing end phase command locally");
            endPhaseCommand.execute(); // execute locally before sending to everyone else
            try {
                LOGGER.info("Sending the end phase command to all players");
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
                LOGGER.severe("There was a problem sending the command to take turns!");
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

    /**
     * PHASE 4 OF ELFENLAND
     * PHASE 6 OF ELFENGOLD
     */
    public void planTravelRoutes() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && (gameState.getCurrentPhase() == ELRoundPhaseType.PLAN_ROUTES
                || gameState.getCurrentPhase() == EGRoundPhaseType.PLAN_ROUTES)
                && isLocalPlayerTurn()) {

            updateGameScreen();
            System.out.println("Current phase: PLAN TRAVEL ROUTES");

            // display message
            if (gameState.getCurrentPhase() == ELRoundPhaseType.PLAN_ROUTES
                    || gameState.getCurrentPhase() == EGRoundPhaseType.PLAN_ROUTES) {

                if (GameManager.getInstance().getThisPlayer().getHand().getCounters().size() > 0) {
                    GameScreen.displayMessage("""
                        It is time to plan your travel routes! Begin by clicking the transportation counter in your hand that you want to use, then click on the road that you want to travel.
                        The chart in the bottom right corner indicates which transportation counters may be used on which road.
                        Alternatively, you may choose to place your Obstacle on a road that already has a counter. But be warned... you can only do this once!
                        When you are done placing one counter, click "End Turn". Alternatively, you can pass your turn by clicking "End Turn".
                        """);
                } else {
                    PassTurnCommand command = new PassTurnCommand();
                    command.execute();
                    try {
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(command);
                    } catch (IOException e) {
                        System.out.println("There was a problem sending the PassTurnCommand to all players.");
                        e.printStackTrace();
                    }
                    endTurn();
                }
            }
        }
    }

    /**
     * PHASE 5 OF ELFENLAND
     * PHASE 7 OF ELFENGOLD
     */
    public void moveOnMap() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && (gameState.getCurrentPhase() == ELRoundPhaseType.MOVE
                || gameState.getCurrentPhase() == EGRoundPhaseType.MOVE)
                && gameState.getCurrentPlayer().equals(thisPlayer)) {

            updateGameScreen();
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

    protected void updateGameScreen() {
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
                String localAddress = CommunicationsManager.getLocalAddress();
                coms.sendGameCommandToAllPlayers(new GetBootColourCommand(localAddress));
                // for players who are not the host, we should check the colors available at this time
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

    public ArrayList<Colour> getAvailableColours() {
        return availableColours;
    }

    public void removeAvailableColour(Colour c) {
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

    public int getBootNotifsReceived() {
        return bootNotifsReceived;
    }

    public void receivedBootNotif()
    {
        bootNotifsReceived++;
    }

    public boolean isLoaded() 
    {
        return loaded;
    }
    public int getCardNotifsReceived() 
    {
        return cardNotifsReceived;
    }

    public void incrementCardNotifsReceived() 
    {
        this.cardNotifsReceived++;
    }
}
