package plan;

import com.google.common.collect.ImmutableList;
import operators.Operator;
import plan.triat.TraitSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class OperatorNode implements Serializable {

    private final Operator operator;
    private final TraitSet traitSet;
    private final List<SubsetNode> inputs;

    public static OperatorNode create(Operator operator, TraitSet traitSet) {
        return create(operator, traitSet, new ArrayList<>());
    }

    public static OperatorNode create(Operator operator, TraitSet traitSet, SubsetNode input) {
        return create(operator, traitSet, Collections.singletonList(input));
    }

    public static OperatorNode create(Operator operators, TraitSet traitSet, List<SubsetNode> inputs) {
        return new OperatorNode(operators, traitSet, inputs);
    }

    public OperatorNode( Operator operator, TraitSet traitSet, List<SubsetNode> inputs) {
        this.operator = operator;
        this.traitSet = traitSet;
        this.inputs = ImmutableList.copyOf(inputs);
    }

    public <T extends Operator> T getOperator() {
        //noinspection unchecked
        return (T) operator;
    }

    public TraitSet getTraitSet() {
        return traitSet;
    }

    public List<SubsetNode> getInputs() {
        return inputs;
    }

    public void acceptSelf(AndOrTreeVisitor visitor) {
        visitor.visitOperatorNode(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperatorNode that = (OperatorNode) o;
        return Objects.equals(operator, that.operator) &&
                Objects.equals(traitSet, that.traitSet) &&
                Objects.equals(inputs, that.inputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, traitSet, inputs);
    }

    @Override
    public String toString() {
        return "OperatorNode{" +
                "operator=" + operator +
                ", traitSet=" + traitSet +
                ", inputs=" + inputs +
                '}';
    }
}
