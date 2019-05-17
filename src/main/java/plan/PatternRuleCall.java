package plan;


import operators.Operator;
import rules.JoinCommutativeRule;
import rules.TransformationRule;

import java.io.Serializable;
import java.util.List;
import java.util.Map;



public class PatternRuleCall implements RuleCall, Serializable {

    private final OptimizerPlanner planner;
    protected final PatternNode mainPattern;
    protected Map<PatternNode, List<PatternNode>> nodeListMap;
    public List<Operator> matchedOperators;


    private PatternRuleCall(OptimizerPlanner planner, PatternNode mainPattern, Map<PatternNode, List<PatternNode>> nodeListMap,
                            List<Operator> matchedOperators) {
        this.mainPattern = mainPattern;
        this.nodeListMap = nodeListMap;
        this.matchedOperators = matchedOperators;
        this.planner = planner;
    }

    public TransformationRule getMatchedRule() {
        return new JoinCommutativeRule();
    }

    @Override
    public OperatorNode getMatchedOperator(int ordinal) {
        return null;
    }

    @Override
    public void transformTo(OperatorNode equivalentOperator) {
        this.planner.addToEquivSet(SetNode.create(equivalentOperator), 0);
    }

}
