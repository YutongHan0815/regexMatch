package rules;

import operators.LogicalVerifyOperator;

import plan.PatternNode;
import plan.RuleCall;

import java.io.Serializable;

public class LogicalVerifyToPhysicalVerifyRule implements TransformationRule, Serializable {

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
        final LogicalVerifyOperator logicalMatchOperator = ruleCall.getMatchedOperator(0);

        //PhysicalVerifyOperator physicalVerifyOperator = new PhysicalVerifyOperator();
        //ruleCall.transformTo(physicalVerifyOperator);

    }


}
