package edu.ics.uci.optimizer.memo;

import edu.ics.uci.optimizer.operator.EquivSet;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PlannerMemo<SET extends SetMemo, SUB extends SubsetMemo, OP extends OperatorMemo> implements Serializable {

    private Map<EquivSet, SET> setMemo = new HashMap<>();
    private Map<SubsetNode, SUB> subsetMemo = new HashMap<>();
    private Map<OperatorNode, OP> operatorMemo = new HashMap<>();

    public static <SET extends SetMemo, SUB extends SubsetMemo, OP extends OperatorMemo> PlannerMemo<SET, SUB, OP>  create() {
        return new PlannerMemo<>();
    }

    private PlannerMemo() {
    }


    public void setSetMemo(EquivSet set, SET setMemo) {

    }

    public void setSubsetMemo(SubsetNode operator, SUB operatorMemo) {

    }

    public void setOperatorMemo(OperatorNode operator, OP operatorMemo) {

    }

}
