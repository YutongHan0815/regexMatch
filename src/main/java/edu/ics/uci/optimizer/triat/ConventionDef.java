package edu.ics.uci.optimizer.triat;

public class ConventionDef implements TraitDef<Convention> {

    public static ConventionDef CONVENTION_TRAIT_DEF = new ConventionDef();

    @Override
    public String getTraitName() {
        return "Convention";
    }

    @Override
    public Convention getDefault() {
        return Convention.LOGICAL;
    }

    @Override
    public Class<Convention> getTraitClass() {
        return Convention.class;
    }
}
