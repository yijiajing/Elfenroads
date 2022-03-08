package domain;

import commands.*;
import enums.Colour;
import enums.RoundPhaseType;
import loginwindow.*;
import networking.*;
import panel.ElfBootPanel;
import panel.GameScreen;
import utils.GameRuleUtils;
import utils.NetworkUtils;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * A Singleton class that controls the main game loop
 */
public class GameManager {

    private static GameManager INSTANCE; // Singleton instance
    private final static Logger LOGGER = Logger.getLogger("Game Manager");

    private GameState gameState;
    private ActionManager actionManager;
    private Player thisPlayer = null; // represents the Player who is using this GUI, set by ChooseBootWindow
    private boolean loaded;

    // stuff for managing networking operations
    private String sessionID;
    private CommunicationsManager coms;

    // manages choosing a boot colour
    private ArrayList<Colour> availableColours = new ArrayList<>();
    private HashMap<Colour, String> bootColours = new HashMap<>(); // <boot colour, player IP> TODO change to Player
    private ChooseBootWindow bootWindow;

    /**
     * Constructor is called when "join" is clicked
     * If the User is starting a new game, then loadedState == null
     * If the User is loading a previous game, then loadedState != null
     */
    private GameManager(Optional<GameState> loadedState, String pSessionID) {

        MainFrame.mainPanel.add(GameScreen.init(MainFrame.getInstance()), "gameScreen");
        sessionID = pSessionID;

        // start a new game if there is no state to be loaded
        if (loadedState.isEmpty()) {
            gameState = GameState.init(3, pSessionID);
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

        coms = new CommunicationsManager(this, sessionID);
    }

    /**
     * Called when "start" is clicked
     */
    public void initPlayers() {
        // Make sure not to do any player list iteration before this block of code, as players are not
        // fully initialized. If you want to do anything right after players are fully initialized,
        // add it to launch(). Also check AddPlayerCommand.

        // initialize all the players now that the game has been launched and everyone is in
        try
        {
            ArrayList<String> players = GameSession.getPlayerNames(sessionID);
            String localPlayerName = getThisPlayer().getName();
            // TODO: how to get the boot color of each player?
            for (String playerName : players)
            {
                // if the player name is the local one, do nothing. we already have his information
                if (!playerName.equals(localPlayerName))
                {
                    // otherwise, send a SendPlayerInfoCommand over
                    SendPlayerInfoCommand cmd = new SendPlayerInfoCommand();
                    try{getComs().sendCommandToIndividual(cmd, playerName);}
                    catch (Exception ugh) {ugh.printStackTrace();}
                }
            }

            // now we have ordered all the players, so we should sort them
        }
        catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Called when we have all the players in the list. Any initialization that utilizes the player list
     * should be put here.
     */
    public void launch() {
        LOGGER.info("We have all players' info ready, setting up the rounds");
        if (!loaded) setUpNewGame();

        GameScreen.getInstance().draw();
        MainFrame.cardLayout.show(MainFrame.mainPanel,"gameScreen");

        gameState.sortPlayers();
        gameState.setToFirstPlayer();
        GameScreen.getInstance().draw(); // put here because draw also utilizes the player list
        initializeElfBoots();
        setUpRound();
    }

    public static GameManager init(Optional<GameState> loadedState, String sessionID) {
        if (INSTANCE == null) {
            INSTANCE = new GameManager(loadedState, sessionID);
        }
        return INSTANCE;
    }

    /**
     * @return the Singleton instance of the GameManager
     */
    public static GameManager getInstance() {
        return INSTANCE;
    }

    /**
     * Called by ChooseBootWindow after the user chooses a boot colour
     * Sets the Player of "this" GUI
     */
    public void setThisPlayer(Player p) {
        thisPlayer = p;
        gameState.addPlayer(p);
    }

    private void setUpNewGame() {
        // put 5 counters face up, these are shared across peers
        for (int i=0; i<5; i++) {
            this.gameState.addFaceUpCounterFromPile();
        }

        // give all players (each peer) an obstacle
        thisPlayer.getHand().addUnit(new Obstacle(MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900));
    }

    /**
     * PHASE 1 & 2
     */
    public void setUpRound() {
        gameState.setCurrentPhase(RoundPhaseType.DEAL_CARDS);
        gameState.setToFirstPlayer();
        gameState.getTravelCardDeck().shuffle(); // only shuffle once at the beginning of each round

        // Triggered only on one instance (the first player)
        if (isLocalPlayerTurn()) {
            distributeTravelCards(); // distribute cards to each player (PHASE 1)
            GameScreen.getInstance().updateAll();
        }
    }

    /**
     * PHASE 1
     * Distribute travel cards to each Player by popping from the TravelCardDeck
     * Fills the Player's hand to have 8 travel cards
     */
    public void distributeTravelCards() {
        if (!(isLocalPlayerTurn() && gameState.getCurrentPhase() == RoundPhaseType.DEAL_CARDS)) return;

        int numCards = thisPlayer.getHand().getNumTravelCards();
        for (int i = numCards; i < 8; i++) {
            thisPlayer.getHand().addUnit(gameState.getTravelCardDeck().draw());
        }

        int numDrawn = 8 - numCards;
        DrawCardCommand drawCardCommand = new DrawCardCommand(numDrawn);
        try {
            coms.sendGameCommandToAllPlayers(drawCardCommand);
        } catch (IOException e) {
            LOGGER.info("There was a problem sending the command to draw cards!");
            e.printStackTrace();
        }
        endTurn();
    }


    /**
     * PHASE 2
     * Distribute 1 face-down transportation counter to each Player by popping from the CounterPile
     */
    public void distributeHiddenCounter() {
        if (!(isLocalPlayerTurn() && gameState.getCurrentPhase() == RoundPhaseType.DEAL_HIDDEN_COUNTER)) return;
        thisPlayer.getHand().addUnit(gameState.getCounterPile().draw());
        //TODO: remove one counter from all peers
        endTurn();
    }

    /**
     * PHASE 3
     */
    public void drawCounters() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && GameRuleUtils.isDrawCountersPhase()
                && isLocalPlayerTurn()) {

            updateGameState();
            System.out.println("Current phase: DRAW COUNTERS");

            // display message to let the user know that they need to select a counter
            GameScreen.displayMessage("Please select a transportation counter to add to your hand. You may choose one of " +
                    "the face-up counters or a counter from the deck, shown on the right side of the screen.", false, false);

            // all logic is implemented in the mouse listeners of the counters
        }
    }

    /**
     * PHASE 4
     */
    public void planTravelRoutes() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && gameState.getCurrentPhase().equals(RoundPhaseType.PLAN_ROUTES)
                && isLocalPlayerTurn()) {

            updateGameState();
            System.out.println("Current phase: PLAN TRAVEL ROUTES");

            // display message
            GameScreen.displayMessage("""
                    It is time to plan your travel routes! Begin by clicking the transportation counter in your hand that you want to use, then click on the road that you want to travel.
                    The chart in the bottom right corner indicates which transportation counters may be used on which road.
                    Alternatively, you may choose to place your Obstacle on a road that already has a counter. But be warned... you can only do this once!
                    Alternatively, you can pass your turn.
                    """, true, false);

            // TODO implement all logic in listeners and action manager
        }
    }

    /**
     * PHASE 5
     */
    public void moveOnMap() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && gameState.getCurrentPhase().equals(RoundPhaseType.MOVE)
                && gameState.getCurrentPlayer().equals(thisPlayer)) {

            updateGameState();
            System.out.println("Current phase: MOVE ON MAP");

            // display message
            GameScreen.displayMessage("""
                    It is time to travel across the map and collect your town pieces! Begin by clicking the travel card(s) that you want to use, then click on the town that you want to travel to.
                    The number of required travel cards depends on the region and is indicated by the chart in the bottom right corner. 
                    You can repeat this as many times as you want. When you are done travelling, click "DONE". 
                    """, false, true);

            // TODO implement all logic in listeners and action manager
        }
    }

    /**
     * PHASE 6
     * Return all counters except one
     */
    public void returnCountersPhase() {
        if (!(isLocalPlayerTurn() && gameState.getCurrentPhase() == RoundPhaseType.RETURN_COUNTERS)) return;

        // no need to return the counters if we are at the end of the game
        if (gameState.getCurrentRound() == gameState.getTotalRounds()) {
            endTurn();
            return;
        }

        actionManager.clearSelection();

        GameScreen.displayMessage("""
                The round is over! All of your transportation counters must be returned except for one. 
                Please select the transportation counter from your hand that you wish to keep.
                """, false, false);

        // once the player clicks a transportation counter it will call returnAllCountersExceptOne()
    }

    /**
     * Part of phase 6, called when a transportation counter from the player's hand is clicked
     * @param toKeep
     * @throws IllegalArgumentException
     */
    public void returnAllCountersExceptOne(TransportationCounter toKeep) throws IllegalArgumentException {
        List<TransportationCounter> myCounters = thisPlayer.getHand().getCounters();

        if (!myCounters.contains(toKeep)) { // the user selected a counter that is not in their hand
            throw new IllegalArgumentException();
        }

        for ( TransportationCounter c : myCounters ) {
            if (c.equals(toKeep)) continue;

            thisPlayer.getHand().removeUnit(c); // remove counter from my hand
            GameState.instance().getCounterPile().addDrawable(c); // put counter back in the deck
            try {
                coms.sendGameCommandToAllPlayers(new ReturnTransportationCounterCommand(c));
            } catch (IOException e) {
                System.out.println("There was a problem sending the ReturnTransportationCounterCommand to all players.");
                e.printStackTrace();
            }
        }

        endTurn();
    }

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
            endPhase();
            GameCommand endPhaseCommand = this::endPhase;
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

    /**
     * Triggered for every peer. One peer (the last player) calls it directly in endTurn
     * and others call it through command execution (endPhaseCommand in endTurn).
     * If we are still in the same round, the first player will take action in the new phase.
     */
    private void endPhase() {
        actionManager.clearSelection();
        int nextOrdinal = gameState.getCurrentPhase().ordinal() + 1;
        if (nextOrdinal == RoundPhaseType.values().length) {
            // all phases are done, go to the next round
            endRound();
        } else { // go to the next phase within the same round
            gameState.setCurrentPhase(RoundPhaseType.values()[nextOrdinal]);
            LOGGER.info("...Going to the next phase : " + gameState.getCurrentPhase());
            gameState.setToFirstPlayer();
            // the first player will take action
            if (isLocalPlayerTurn()) {
                NotifyTurnCommand notifyTurnCommand = new NotifyTurnCommand(gameState.getCurrentPhase());
                notifyTurnCommand.execute(); // notify themself to take action
            }
        }
    }

    /**
     * Trigger for every peers when endPhase is called
     */
    public void endRound() {
        gameState.incrementCurrentRound();
        LOGGER.info("...Going to the next round #" + gameState.getCurrentRound());

        actionManager.clearSelection();

        GameMap.getInstance().clearAllCounters();
        GameState.instance().getCounterPile().shuffle();

        GameScreen.getInstance().initializeRoundCardImage(gameState.getCurrentRound()); // update round card image

        if (gameState.getCurrentRound() > gameState.getTotalRounds()) {
            endGame();
        } else {
            setUpRound(); // start next round
        }
    }

    private void endGame() {
        LOGGER.info("Game ends in " + gameState.getCurrentRound() + " rounds");
        //TODO: finishes game ending
    }

    public void initializeElfBoots() {

        ElfBootPanel elvenholdBootPanel = GameMap.getInstance().getTown("Elvenhold").getPanel().getElfBootPanel();

        for (Player p : gameState.getPlayers()) {
            gameState.addElfBoot(new ElfBoot(p.getColour(), GameScreen.getInstance().getWidth(), GameScreen.getInstance().getHeight(), elvenholdBootPanel, GameScreen.getInstance()));
        }
    }

    private void updateGameState() {
        // TODO implement get game state from peers

        GameScreen.getInstance().updateAll();
    }

    private void sendGameState() {
        // TODO implement
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

                if (availableColours.size() == 7-numPlayers) { // we have received the boot colours from all players who have joined
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

    public void setChooseBootWindow (ChooseBootWindow window)
    {
        bootWindow = window;
    }

    public void showBootWindow() {
        ChooseBootWindow window = new ChooseBootWindow(sessionID, availableColours);
        MainFrame.mainPanel.add(window, "choose-boot");
        MainFrame.cardLayout.show(MainFrame.mainPanel, "choose-boot");
    }
}
