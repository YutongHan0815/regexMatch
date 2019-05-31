package edu.ics.uci.optimizer.operator;

import edu.ics.uci.optimizer.triat.TraitSet;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


public class SubsetNode implements Serializable {

    public static SubsetNode create(MetaSet metaSet, TraitSet traitSet) {
        return new SubsetNode(metaSet, traitSet);
    }

    public static SubsetNode create(OperatorNode operator) {
        return new SubsetNode(MetaSet.create(operator), operator.getTraitSet());
    }

    private final MetaSet metaSet;
    private final TraitSet traitSet;

    private SubsetNode(MetaSet metaSet, TraitSet traitSet) {
        this.metaSet = metaSet;
        this.traitSet = traitSet;
    }

    public MetaSet getMetaSet() {
        return metaSet;
    }

    public TraitSet getTraitSet() {
        return traitSet;
    }

    public Set<OperatorNode> getOperators() {
        return this.metaSet.getOperators(this.traitSet);
    }

    public void acceptSelf(AndOrTreeVisitor visitor) {
        visitor.visitSetNode(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubsetNode that = (SubsetNode) o;
        return Objects.equals(metaSet, that.metaSet) &&
                Objects.equals(traitSet, that.traitSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metaSet, traitSet);
    }
}
