import javax.swing.*;
import java.awt.*;

public class TransportationCounter {

    public enum CounterType {GIANTPIG, ELFCYCLE, MAGICCLOUD, UNICORN, TROLLWAGON, DRAGON}

    // another rudimentary class to go along with Deck

    // will represent, for now, a Transportation Counter. It will contain information about how to display said counter.

    private CounterType type;
    private String imageFilepath;
    private ImageIcon image;
    private JLabel display;
    // add JLabel

    public TransportationCounter (CounterType pType, int width, int height)
    {
         this.type = pType;

         // find the picture of the card based on what type it is
        // since the images are named similarly and ordered the same way as they are in the enum declaration, we can get the filepath just by using the type.

        int imageNumber = this.type.ordinal() + 1; // since the images start from M01, not M00
        this.imageFilepath = ("/Users/charlescouture/eclipse-workspace/COMP361/assets/sprites/M0" + imageNumber + ".png");
        this.image = new ImageIcon (this.imageFilepath);
        Image toResize = this.image.getImage();
        Image resized = toResize.getScaledInstance(width*67/1440, height*52/900,  java.awt.Image.SCALE_SMOOTH);
        this.image = new ImageIcon(resized);

        // add JLabel
        
        this.display = new JLabel(this.image);
    }

    private ImageIcon getImage() {return this.image;}

    public CounterType getType() {return this.type;}

    public String getImageFilepath() {return this.imageFilepath;}

    public JLabel getDisplay() {return this.display;}





}
