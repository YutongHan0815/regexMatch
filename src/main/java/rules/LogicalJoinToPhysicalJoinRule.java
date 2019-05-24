package rules;

import operators.LogicalJoinOperator;
import operators.PhysicalJoinOperator;

import plan.OperatorNode;
import plan.PatternNode;
import plan.rule.RuleCall;
import plan.SubsetNode;
import plan.triat.Convention;
import plan.triat.Trait;
import plan.triat.TraitDef;
import plan.triat.TraitSet;

import java.util.Arrays;
import java.util.List;


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
        final  OperatorNode logicalJoinOpN = ruleCall.getMatchedOperator(0);
        final LogicalJoinOperator logicalJoinOperator = logicalJoinOpN.getOperator();
        final List<SubsetNode> inputs = logicalJoinOpN.getInputs();

        if(inputs.stream().allMatch(subsetNode -> subsetNode.getTraitSet().equals(Convention.PHYSICAL))) {

            PhysicalJoinOperator physicalJoinOperator = new PhysicalJoinOperator(logicalJoinOperator.getJoinCondition());


            OperatorNode joinOperatorNode = OperatorNode.create(physicalJoinOperator, logicalJoinOpN.getTraitSet().replace(Convention.PHYSICAL));

            ruleCall.transformTo(joinOperatorNode);
        }

    }

    public String getDescription() {
        return description;
    }
}
