package panel;// package testing;

import domain.TransportationCounterPile;

import javax.swing.*;

public class DrawButton extends JButton {

    private final TransportationCounterPile drawnFrom;

    public DrawButton(TransportationCounterPile pDrawnFrom)
    {
        this.drawnFrom = pDrawnFrom;
        // this.addActionListener(new panel.DrawListener(drawnFrom));
        this.setText("Draw");

    }

    }


