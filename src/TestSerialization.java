
public class TestSerialization {

    public static void main (String [] args)
    {}




    public static void serializeGameScreen()
    {
        MainFrame main = new MainFrame();
        GameScreen screen = new GameScreen(main);

        GameState test = new GameState(screen);
        System.out.println(test.serialize());


    }




}
