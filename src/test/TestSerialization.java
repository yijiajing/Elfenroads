package test;

import loginwindow.MainFrame;
import networking.GameState;
import panel.GameScreen;

public class TestSerialization {

    public static void main (String [] args)
    {}




    public static void serializeGameScreen()
    {
        MainFrame main = new MainFrame();
        GameScreen screen = GameScreen.init(main);

        GameState test = new GameState(screen);
        System.out.println(test.serialize());


    }




}
