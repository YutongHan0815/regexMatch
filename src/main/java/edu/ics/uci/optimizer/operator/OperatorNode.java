package edu.ics.uci.optimizer.operator;

import com.google.common.collect.ImmutableList;
import edu.ics.uci.optimizer.OptimizerContext;
import edu.ics.uci.optimizer.triat.TraitSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class OperatorNode implements Serializable {

    private final int operatorID;
    private final Operator operator;
    private final TraitSet traitSet;
    private final List<SubsetNode> inputs;

    public static OperatorNode create(OptimizerContext context, Operator operator, TraitSet traitSet) {
        return create(context, operator, traitSet, new ArrayList<>());
    }

    public static OperatorNode create(OptimizerContext context, Operator operator, TraitSet traitSet, SubsetNode input) {
        return create(context, operator, traitSet, Collections.singletonList(input));
    }

    public static OperatorNode create(OptimizerContext context, Operator operators, TraitSet traitSet, List<SubsetNode> inputs) {
        return new OperatorNode(context.nextOperatorID(), operators, traitSet, inputs);
    }

    public static OperatorNode create(int operatorID, Operator operator, TraitSet traitSet) {
        return create(operatorID, operator, traitSet, new ArrayList<>());
    }

    public static OperatorNode create(int operatorID, Operator operator, TraitSet traitSet, SubsetNode input) {
        return create(operatorID, operator, traitSet, Collections.singletonList(input));
    }

    public static OperatorNode create(int operatorID, Operator operators, TraitSet traitSet, List<SubsetNode> inputs) {
        return new OperatorNode(operatorID, operators, traitSet, inputs);
    }

//
//    private OperatorNode(OptimizerContext context, Operator operator, TraitSet traitSet, List<SubsetNode> inputs) {
//        this.operatorID = context.nextOperatorID();
//        this.operator = operator;
//        this.traitSet = traitSet;
//        this.inputs = ImmutableList.copyOf(inputs);
//    }


    public OperatorNode(int operatorID, Operator operator, TraitSet traitSet, List<SubsetNode> inputs) {
        this.operatorID = operatorID;
        this.operator = operator;
        this.traitSet = traitSet;
        this.inputs = inputs;
    }

    public int getOperatorID() {
        return operatorID;
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
        return Objects.hash( operator, traitSet, inputs);
    }

    @Override
    public String toString() {
        return "OperatorNode{" +
                "operatorID=" + operatorID +
                ", operator=" + operator +
                ", traitSet=" + traitSet +
                ", inputs=" + inputs +
                '}';
    }
}
