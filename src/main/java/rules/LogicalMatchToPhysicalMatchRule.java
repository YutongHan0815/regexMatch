package rules;


import operators.LogicalMatchOperator;
import operators.PhysicalMatchOperator;
import plan.OperatorNode;
import plan.PatternNode;
import plan.RuleCall;
import plan.SetNode;

import java.io.Serializable;
import java.util.ArrayList;


public class LogicalMatchToPhysicalMatchRule implements TransformationRule, Serializable {

    public static final LogicalMatchToPhysicalMatchRule INSTANCE = new LogicalMatchToPhysicalMatchRule();
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
        final LogicalMatchOperator logicalMatchOperator = ruleCall.getMatchedOperator(0).getOperator();

        PhysicalMatchOperator physicalMatchOperator = new PhysicalMatchOperator(logicalMatchOperator.getSubRegex());
        OperatorNode matchOperatorNode = OperatorNode.create(physicalMatchOperator, ruleCall.getMatchedOperator(0).getInputs());

        ruleCall.transformTo(matchOperatorNode);

    }
}
