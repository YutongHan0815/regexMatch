package rules;


import plan.LogicalMatchOperator;
import plan.PhysicalMatchOperator;

import java.io.Serializable;




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
        final LogicalMatchOperator logicalMatchOperator = ruleCall.getMatchedOperator(0);
        //PhysicalMatchOperator physicalMatchOperator = new PhysicalMatchOperator();
        //ruleCall.transformTo(physicalMatchOperator);

    }
}
