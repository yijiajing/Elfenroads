package savegames;

import domain.MagicSpell;
import domain.TransportationCounter;
import enums.CounterType;
import enums.MagicSpellType;

public class SerializableMagicSpell extends SerializableCounterUnit
{
        private MagicSpellType type;

        public SerializableMagicSpell(MagicSpell original)
        {
            super(original, original.getType().toString());
            type = (MagicSpellType) original.getType();
        }

        public MagicSpellType getType() {
            return type;
        }




}
