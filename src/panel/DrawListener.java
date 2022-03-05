package panel;// package testing;

import domain.TransportationCounterPile;
import domain.CounterUnit;
import domain.TransportationCounter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawListener implements ActionListener {
    private final TransportationCounterPile drawnFrom;
    private JPanel[] destination;

    public DrawListener(TransportationCounterPile pDrawnFrom, JPanel[] pDestination)

    {
        this.drawnFrom = pDrawnFrom;
        this.destination = pDestination;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.moveCardToSpot(this.destination);
    }

    private void moveCardToSpot(JPanel [] toPlace) // this is a dumb method, but it's a temporary solution.
    {

        for (int i = 0; i < toPlace.length; i++)
        {
            // if we find an empty spot, put the card there and break
            if (toPlace[i].getComponentCount() == 0)
            {
                CounterUnit toMove = this.drawnFrom.draw();
                toPlace[i].add(toMove.getDisplay());
                break;
            }
            // if we can't find an empty spot, then just break and leave it alone for now
        }

        // we couldn't find an empty spot, so we aren't going to put the card anywhere

    }
}

