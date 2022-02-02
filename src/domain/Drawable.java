package domain;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Drawable {
    String imageFilepath;
    ImageIcon image;
    JLabel display;
    
    //TODO: Since the constructors of CounterUnit and CardUnit are almost the same, we shall implement it in this abstract class. 
    //The only difference is that they have different file path for the image.
}
