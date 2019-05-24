package plan;

import operators.Operator;
import plan.triat.TraitSet;

import java.io.Serializable;
import java.util.List;
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

}
