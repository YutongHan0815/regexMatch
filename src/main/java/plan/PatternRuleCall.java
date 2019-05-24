package plan;


import plan.rule.RuleCall;
import rules.TransformationRule;
import java.io.Serializable;
import java.util.Map;

public class PatternRuleCall implements RuleCall, Serializable {

    private final OptimizerPlanner planner;
    private final TransformationRule rule;
    private final Map<Integer, OperatorNode> matchedOperators;


    public PatternRuleCall(OptimizerPlanner planner, TransformationRule rule, Map<Integer, OperatorNode> matchedOperators) {
        this.planner = planner;
        this.rule = rule;
        this.matchedOperators = matchedOperators;
    }

    @Override
    public TransformationRule getMatchedRule() {
        return rule;
    }
    @Override
    public OperatorNode getMatchedOperator(int ordinal) {
        return matchedOperators.get(ordinal);
    }

    @Override
    public void transformTo(OperatorNode equivalentOperator) {
        this.planner.registerOperator(equivalentOperator, 0);
    }

}
