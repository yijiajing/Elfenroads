package panel;// package testing;

import domain.CounterUnitPile;

import javax.swing.*;

public class DrawButton extends JButton {

    private final CounterUnitPile drawnFrom;

    public DrawButton(CounterUnitPile pDrawnFrom) {
        this.drawnFrom = pDrawnFrom;
        // this.addActionListener(new panel.DrawListener(drawnFrom));
        this.setText("Draw");

    }

}


