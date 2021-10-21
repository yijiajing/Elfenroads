import org.w3c.dom.css.Counter;

public class TransportationCounter {

    public enum CounterType {GIANTPIG, ELFCYCLE, MAGICCLOUD, UNICORN, TROLLWAGON, DRAGON, RAFT}



    // another rudimentary class to go along with Deck

    // will represent, for now, a Transportation Counter. It will contain information about how to display said counter.


    // this is a bad constructor, but again just for the UI demo

    private CounterType type;
    private String imageFilepath;


    public TransportationCounter (CounterType pType)
    {
         this.type = pType;

         // find the picture of the card based on what type it is
        if (this.type.equals(CounterType.GIANTPIG))
        {this.imageFilepath = "M01";}

        else if (this.type.equals(CounterType.ELFCYCLE))
        {this.imageFilepath = "M02";}

        else if (this.type.equals(CounterType.MAGICCLOUD))
        {this.imageFilepath = "M03";}

        else if (this.type.equals(CounterType.UNICORN))
        {this.imageFilepath = "M04";}

        else if (this.type.equals(CounterType.TROLLWAGON))
        {this.imageFilepath = "M05";}

        else if (this.type.equals(CounterType.DRAGON))
        {this.imageFilepath = "M06";}

        else if (this.type.equals(CounterType.RAFT))
        {this.imageFilepath = "M07";}


    }

    public CounterType getType() {return this.type;}

    public String getImageFilepath() {return this.imageFilepath;}







}
