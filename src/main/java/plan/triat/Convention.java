package plan.triat;

import java.util.Objects;

import static plan.triat.ConventionDef.CONVENTION_DEF;

public class Convention implements Trait {

    public static final Convention LOGICAL = new Convention("logical");

    public static final Convention PHYSICAL = new Convention("physical");

    private final String a;

    private Convention(String a) {
        this.a = a;
    }

    @Override
    public TraitDef<? extends Trait> getTraitDef() {
        return CONVENTION_DEF;
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
