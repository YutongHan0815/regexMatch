package edu.ics.uci.optimizer.operator;

import edu.ics.uci.optimizer.OptimizerContext;
import edu.ics.uci.optimizer.memo.SubsetMemo;
import edu.ics.uci.optimizer.triat.TraitSet;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


public class SubsetNode implements Serializable {

    public static SubsetNode create(EquivSet equivSet, TraitSet traitSet) {
        return new SubsetNode(equivSet, traitSet);
    }

    public static SubsetNode create(OptimizerContext context, OperatorNode operator) {
        return new SubsetNode(EquivSet.create(context, operator), operator.getTraitSet());
    }

    private final EquivSet equivSet;
    private final TraitSet traitSet;
    private final SubsetMemo subsetMemo;

    private SubsetNode(EquivSet equivSet, TraitSet traitSet) {
        this.equivSet = equivSet;
        this.traitSet = traitSet;
        this.subsetMemo = SubsetMemo.create();
    }

    public EquivSet getEquivSet() {
        return equivSet;
    }

    public TraitSet getTraitSet() {
        return traitSet;
    }

    public Set<OperatorNode> getOperators() {
        return this.equivSet.getOperators(this.traitSet);
    }

    public SubsetMemo getSubsetMemo() {
        return subsetMemo;
    }

    public void acceptSelf(AndOrTreeVisitor visitor) {
        visitor.visitSetNode(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubsetNode that = (SubsetNode) o;
        return Objects.equals(equivSet, that.equivSet) &&
                Objects.equals(traitSet, that.traitSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equivSet, traitSet);
    }
}
