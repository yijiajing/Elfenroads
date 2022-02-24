package test;

import loginwindow.MainFrame;
import networking.GameState;
import panel.GameScreen;

public class TestSerialization {

    public static void main (String [] args)
    {}




    public static void serializeGameScreen()
    {
        MainFrame main = MainFrame.getInstance();
        GameScreen screen = GameScreen.init(main);

        GameState test = GameState.init(3);
        System.out.println(test.serialize());


    }




}
