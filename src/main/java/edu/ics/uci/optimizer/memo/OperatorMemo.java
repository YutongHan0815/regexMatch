package edu.ics.uci.optimizer.memo;

import edu.ics.uci.optimizer.operator.schema.RowType;

import java.util.Optional;

public class OperatorMemo {

    private Cost selfCost;

    private RowType outputRowType;

    public static OperatorMemo create() {
        return new OperatorMemo();
    }

    private OperatorMemo() {}

    public void setSelfCost(Cost selfCost) {
        this.selfCost = selfCost;
    }

    public void setOutputRowType(RowType outputRowType) {
        this.outputRowType = outputRowType;
    }

    public Optional<Cost> getSelfCost() {
        return Optional.ofNullable(selfCost);
    }

    public Optional<RowType> getOutputRowType() {
        return Optional.ofNullable(outputRowType);
    }

}
