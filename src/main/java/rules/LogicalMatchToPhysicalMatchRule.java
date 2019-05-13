package rules;


import operators.LogicalMatchOperator;
import operators.PhysicalJoinOperator;
import operators.PhysicalMatchOperator;
import plan.OperatorInput;
import plan.PatternNode;
import plan.RuleCall;
import plan.SetNode;

import java.io.Serializable;
import java.util.ArrayList;


public class LogicalMatchToPhysicalMatchRule implements TransformationRule, Serializable {

    private final String description;

    private final PatternNode matchPattern;

    public LogicalMatchToPhysicalMatchRule() {

        this.description = this.getClass().getName();
        this.matchPattern = PatternNode.any(LogicalMatchOperator.class);
    }

    public String getDescription() {
        return description;
    }

    @Override
    public PatternNode getMatchPattern() {
        return matchPattern;
    }


    @Override
    public void onMatch(RuleCall ruleCall) {
        final PhysicalMatchOperator physicalMatchOperator = ruleCall.getMatchedOperator(0);
        final SetNode matchSetNode = new SetNode();
        OperatorInput optInput = new OperatorInput(physicalMatchOperator, new ArrayList<>());
        matchSetNode.operatorList.add(optInput);
        ruleCall.transformTo(matchSetNode);

    }
}
