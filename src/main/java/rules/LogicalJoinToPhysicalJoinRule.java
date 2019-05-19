package rules;

import operators.LogicalJoinOperator;
import operators.PhysicalJoinOperator;

import plan.OperatorNode;
import plan.PatternNode;
import plan.RuleCall;
import plan.SetNode;




public class LogicalJoinToPhysicalJoinRule implements TransformationRule{
    public static final LogicalJoinToPhysicalJoinRule INSTANCE = new LogicalJoinToPhysicalJoinRule();
    private final String description;
    private final PatternNode matchPattern;

    public LogicalJoinToPhysicalJoinRule() {
        this.description = this.getClass().getName();
        this.matchPattern = PatternNode.any(LogicalJoinOperator.class);
    }

    @Override
    public PatternNode getMatchPattern() {

        return matchPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final LogicalJoinOperator logicalJoinOperator = ruleCall.getMatchedOperator(0).getOperator();
        PhysicalJoinOperator physicalJoinOperator = new PhysicalJoinOperator(logicalJoinOperator.getJoinCondition());
        OperatorNode joinOperatorNode = OperatorNode.create(physicalJoinOperator, ruleCall.getMatchedOperator(0).getInputs());

        ruleCall.transformTo(joinOperatorNode);

    }

    public String getDescription() {
        return description;
    }
}
