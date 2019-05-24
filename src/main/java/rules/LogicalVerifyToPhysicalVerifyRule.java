package rules;

import operators.LogicalVerifyOperator;

import operators.PhysicalVerifyOperator;
import plan.OperatorNode;
import plan.PatternNode;
import plan.rule.RuleCall;
import plan.SubsetNode;
import plan.triat.Convention;

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
        final OperatorNode logicalVerifyOpN = ruleCall.getMatchedOperator(0);
        final LogicalVerifyOperator logicalVerifyOperator = logicalVerifyOpN.getOperator();
        PhysicalVerifyOperator physicalVerifyOperator = new PhysicalVerifyOperator(
                logicalVerifyOperator.getSubRegex(), logicalVerifyOperator.getVerifyCondition());
        OperatorNode verifyOperatorNode = OperatorNode.create(physicalVerifyOperator, logicalVerifyOpN.getTraitSet().replace(Convention.PHYSICAL));


        ruleCall.transformTo(verifyOperatorNode);

    }


}
