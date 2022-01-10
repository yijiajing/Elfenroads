package panel;// package testing;

import domain.Deck;

import javax.swing.*;

public class DrawButton extends JButton {

    private final Deck drawnFrom;

    public DrawButton(Deck pDrawnFrom)
    {
        this.drawnFrom = pDrawnFrom;
        // this.addActionListener(new panel.DrawListener(drawnFrom));
        this.setText("Draw");

    }

    }


