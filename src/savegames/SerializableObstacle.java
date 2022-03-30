package savegames;

import domain.Obstacle;

// the same as the regular obstacle, this class only exists so that we can draw a distinction between obstacles and other types of counter units

public class SerializableObstacle extends SerializableCounterUnit
{
    public SerializableObstacle (Obstacle original)
    {
        super (original);

    }



}
