package edu.ics.uci.optimizer.memo;

import edu.ics.uci.optimizer.operator.MetaSet;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;

import java.io.Serializable;
import java.util.Map;

public class PlannerMemo implements Serializable {

    private Map<MetaSet, SetMemo> setMemo;
    private Map<SubsetNode, SubsetMemo> subsetMemo;
    private Map<OperatorNode, OperatorMemo> operatorMemo;


}
