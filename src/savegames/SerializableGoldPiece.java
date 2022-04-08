package savegames;

import domain.GoldPiece;
import enums.GoldPieceType;

public class SerializableGoldPiece extends SerializableCounterUnit
{
    private GoldPieceType aType;

    public SerializableGoldPiece (GoldPiece original)
    {

        super(original, original.getType().toString());
        aType = (GoldPieceType) original.getType();
    }

    public GoldPieceType getType() {
        return aType;
    }
}