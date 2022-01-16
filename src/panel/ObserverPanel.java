package panel;

/**
 * Represents a JPanel that observes the game model and gets notified whenever the model changes
 * The list of ObserverPanels is held and notified by the GameScreen
 */
public interface ObserverPanel {

    void updateView();

}
