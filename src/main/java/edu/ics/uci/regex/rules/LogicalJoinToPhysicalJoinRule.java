package edu.ics.uci.regex.rules;

import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.LogicalJoinOperator;
import edu.ics.uci.regex.operators.PhysicalJoinOperator;

import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.triat.Convention;

import java.util.List;


public class LogicalJoinToPhysicalJoinRule implements TransformRule {
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
        final  OperatorNode logicalJoinOpN = ruleCall.getOperator(0);
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