package panel;// package testing;

import domain.CounterPile;

import javax.swing.*;

public class DrawButton extends JButton {

    private final CounterPile drawnFrom;

    public DrawButton(CounterPile pDrawnFrom)
    {
        this.drawnFrom = pDrawnFrom;
        // this.addActionListener(new panel.DrawListener(drawnFrom));
        this.setText("Draw");

    }

    }


