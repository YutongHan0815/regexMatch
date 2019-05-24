package edu.ics.uci.optimizer.triat;

import java.io.Serializable;

public interface TraitDef<T extends Trait> extends Serializable {

    String getTraitName();

    T getDefault();

    Class<T> getTraitClass();

}
