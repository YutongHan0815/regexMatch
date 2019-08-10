package edu.ics.uci.optimizer.operator;


import com.google.common.collect.ImmutableList;
import edu.ics.uci.optimizer.OptimizerContext;
import edu.ics.uci.optimizer.memo.OperatorMemo;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.optimizer.triat.TraitSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



public class OperatorNode implements Serializable {

    private final int operatorID;
    private final Operator operator;
    private final TraitSet traitSet;
    private final List<SubsetNode> inputs;
    private final OperatorMemo operatorMemo;

    public static OperatorNode create(OptimizerContext context, Operator operator, TraitSet traitSet) {
        return create(context, operator, traitSet, new ArrayList<>());
    }

    public static OperatorNode create(OptimizerContext context, Operator operator, TraitSet traitSet, SubsetNode... inputs) {
        return new OperatorNode(context, operator, traitSet, ImmutableList.copyOf(inputs));
    }

    public static OperatorNode create(OptimizerContext context, Operator operator, TraitSet traitSet, List<SubsetNode> inputs) {
        return new OperatorNode(context, operator, traitSet, inputs);
    }

    private OperatorNode(OptimizerContext context, Operator operator, TraitSet traitSet, List<SubsetNode> inputs) {
        this.operatorID = context.nextOperatorID();
        this.operator = operator;
        this.traitSet = traitSet;
        this.inputs = ImmutableList.copyOf(inputs);
        this.operatorMemo = OperatorMemo.create();

        setRowTypeInMemo();

    }


    private void setRowTypeInMemo() {
        List<RowType> inputRowTypeList = this.inputs.stream()
                .map(subset -> subset.getEquivSet().getSetMemo().getOutputRowType().get())
                .collect(Collectors.toList());

        this.operatorMemo.setOutputRowType(this.operator.deriveRowType(inputRowTypeList));
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

    public OperatorMemo getOperatorMemo() {
        return operatorMemo;
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
                "operatorID=" + operatorID +
                ", operator=" + operator +
                ", traitSet=" + traitSet +
                ", inputs=" + inputs +
                '}';
    }
}
