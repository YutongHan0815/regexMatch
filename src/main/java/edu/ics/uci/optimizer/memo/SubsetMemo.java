package edu.ics.uci.optimizer.memo;

import edu.ics.uci.optimizer.operator.OperatorNode;

import java.io.Serializable;
import java.util.Optional;

public class SubsetMemo implements Serializable {

    private Cost bestCost;
    private OperatorNode bestOperator;

    public static SubsetMemo create() {
        return new SubsetMemo();
    }

    private SubsetMemo() {}

    public void setBestCost(Cost bestCost) {
        this.bestCost = bestCost;
    }

    public void setBestOperator(OperatorNode bestOperator) {
        this.bestOperator = bestOperator;
    }

    public Optional<Cost> getBestCost() {
        return Optional.ofNullable(bestCost);
    }

    public Optional<OperatorNode> getBestOperator() {
        return Optional.ofNullable(bestOperator);
    }
}
