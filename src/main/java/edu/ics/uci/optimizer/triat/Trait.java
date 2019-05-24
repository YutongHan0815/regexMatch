package edu.ics.uci.optimizer.triat;

import java.io.Serializable;

public interface Trait extends Serializable {

    TraitDef<? extends Trait> getTraitDef();

    default boolean satisfy(Trait trait) {
        if (this.equals(trait)) {
            return true;
        }
        return false;
    }

    default boolean canConvertTo(Trait toTrait) {
        return false;
    }

}
