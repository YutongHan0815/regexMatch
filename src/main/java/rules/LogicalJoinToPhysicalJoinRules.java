package rules;

import plan.LogicalJoinOperator;
import plan.PhysicalJoinOperator;


public class LogicalJoinToPhysicalJoinRules implements TransformationRule{

    private final String description;

    private final PatternNode matchPattern;

    public LogicalJoinToPhysicalJoinRules() {

        this.description = this.getClass().toString();

        this.matchPattern = PatternNode.any(LogicalJoinOperator.class);
    }

    @Override
    public PatternNode getMatchPattern() {

        return matchPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final LogicalJoinOperator logicalJoinOperator = ruleCall.getMatchedOperator(0);

        //PhysicalJoinOperator physicalJoinOperator = new PhysicalJoinOperator();
        //ruleCall.transformTo(physicalJoinOperator);

    }

    public String getDescription() {
        return description;
    }
}
