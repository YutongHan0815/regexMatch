package rules;


import operators.LogicalMatchOperator;
import operators.PhysicalMatchOperator;
import plan.OperatorNode;
import plan.PatternNode;
import plan.rule.RuleCall;
import plan.SubsetNode;

import java.io.Serializable;


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
        final LogicalMatchOperator logicalMatchOperator = ruleCall.getMatchedOperator(0);

        PhysicalMatchOperator physicalMatchOperator = new PhysicalMatchOperator(logicalMatchOperator.getSubRegex());
        OperatorNode matchOperatorNode = OperatorNode.create(physicalMatchOperator);

        SubsetNode matchSubsetNode = SubsetNode.create(matchOperatorNode);

        ruleCall.transformTo(matchSubsetNode);

    }
}
