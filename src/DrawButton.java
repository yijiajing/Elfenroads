// package testing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawButton extends JButton {

    private final Deck drawnFrom;

    public DrawButton(Deck pDrawnFrom)
    {
        this.drawnFrom = pDrawnFrom;
        // this.addActionListener(new DrawListener(drawnFrom));
        this.setText("Draw");

    }

    }


