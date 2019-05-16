package plan;


import operators.Operator;
import rules.JoinCommutativeRule;
import rules.TransformationRule;

import java.io.Serializable;
import java.util.List;
import java.util.Map;



public class PatternRuleCall implements RuleCall, Serializable {

    private final RegexPlanner regexPlanner;
    protected final PatternNode mainPattern;
    protected Map<PatternNode, List<PatternNode>> nodeListMap;
    public List<Operator> matchedOperators;


    private PatternRuleCall(RegexPlanner planner, PatternNode mainPattern, Map<PatternNode, List<PatternNode>> nodeListMap,
                            List<Operator> matchedOperators) {
        this.mainPattern = mainPattern;
        this.nodeListMap = nodeListMap;
        this.matchedOperators = matchedOperators;
        this.regexPlanner = planner;
    }

    public TransformationRule getMatchedRule() {
        return new JoinCommutativeRule();
    }

    @Override
    public <T extends Operator> T getMatchedOperator(int ordinal) {
        return null;
    }

    @Override
    public void transformTo(Operator equivalentOperator) {


    }

    @Override
    public void transformTo(SetNode setNode) {

        regexPlanner.registerSet(setNode, 1);


    }
}
