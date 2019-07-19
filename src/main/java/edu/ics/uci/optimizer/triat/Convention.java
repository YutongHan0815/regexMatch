package edu.ics.uci.optimizer.triat;

import java.util.Objects;

/**
 * Convention is the instance the trait of the {@link edu.ics.uci.optimizer.operator.Operator}:
 * Logical
 * Physical
 */
public class Convention implements Trait {

    public static final Convention LOGICAL = new Convention("logical");

    public static final Convention PHYSICAL = new Convention("physical");

    //Description of a trait
    private final String a;

    private Convention(String a) {
        this.a = a;
    }

    @Override
    public TraitDef<? extends Trait> getTraitDef() {
        return ConventionDef.CONVENTION_TRAIT_DEF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Convention that = (Convention) o;
        return Objects.equals(a, that.a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public String toString() {
        return a;
    }
}
