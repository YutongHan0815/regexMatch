package plan;


import com.google.common.collect.ImmutableList;
import rules.JoinCommutativeRule;
import rules.TransformationRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




public class PatternRuleCall implements RuleCall, Serializable {

    private final OptimizerPlanner planner;
    protected final PatternNode mainPattern;
    public List<OperatorNode> matchedOperators;


    public PatternRuleCall create(OptimizerPlanner planner, PatternNode mainPattern) {
        return new PatternRuleCall(planner, mainPattern, new ArrayList<>());
    }
    public PatternRuleCall create(OptimizerPlanner planner, PatternNode mainPattern, List<OperatorNode> matchedOperators) {
        return new PatternRuleCall(planner,mainPattern, matchedOperators);
    }
    public PatternRuleCall(OptimizerPlanner planner, PatternNode mainPattern, List<OperatorNode> matchedOperators) {
        this.mainPattern = mainPattern;
        this.planner = planner;
        this.matchedOperators = ImmutableList.copyOf(matchedOperators);
    }


    public TransformationRule getMatchedRule() {
        return new JoinCommutativeRule();
    }

    //TODO
    void match(OperatorNode operatorNode) {

    }
    @Override
    public OperatorNode getMatchedOperator(int ordinal) {

        return matchedOperators.get(ordinal);
    }

    @Override
    public void transformTo(OperatorNode equivalentOperator) {
        this.planner.addToEquivSet(SetNode.create(equivalentOperator), 0);
    }

}
