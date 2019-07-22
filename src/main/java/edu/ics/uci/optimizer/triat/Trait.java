package edu.ics.uci.optimizer.triat;

import java.io.Serializable;

/**
 * Trait represent the manifestation of a regex operator expression within a trait definition
 *
 */
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
