package savegames;

import javax.swing.*;
import java.io.Serializable;

public abstract class SerializableDrawable implements Serializable {

    String imageFilepath;

    protected SerializableDrawable(String pImageFilepath)
    {
        imageFilepath = pImageFilepath;
    }





}
