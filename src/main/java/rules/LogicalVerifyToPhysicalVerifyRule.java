package rules;

import operators.LogicalVerifyOperator;

import operators.PhysicalVerifyOperator;
import plan.OperatorNode;
import plan.PatternNode;
import plan.rule.RuleCall;
import plan.SubsetNode;

import java.io.Serializable;

public class LogicalVerifyToPhysicalVerifyRule implements TransformationRule, Serializable {

    public static final LogicalVerifyToPhysicalVerifyRule INSTANCE = new LogicalVerifyToPhysicalVerifyRule();
    private final String description;
    private final PatternNode matchPattern;

    public LogicalVerifyToPhysicalVerifyRule() {

        this.description = this.getClass().getName();
        this.matchPattern = PatternNode.any(LogicalVerifyOperator.class);
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
        final LogicalVerifyOperator logicalVerifyOperator = ruleCall.getMatchedOperator(0);
        PhysicalVerifyOperator physicalVerifyOperator = new PhysicalVerifyOperator(
                logicalVerifyOperator.getSubRegex(), logicalVerifyOperator.getVerifyCondition());
        OperatorNode verifyOperatorNode = OperatorNode.create(physicalVerifyOperator);

        SubsetNode verifySubsetNode = SubsetNode.create(verifyOperatorNode);

        ruleCall.transformTo(verifySubsetNode);

    }


}
