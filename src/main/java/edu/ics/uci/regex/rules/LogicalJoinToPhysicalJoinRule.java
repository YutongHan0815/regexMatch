package edu.ics.uci.regex.rules;

import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.LogicalJoinOperator;
import edu.ics.uci.regex.operators.PhysicalJoinOperator;

import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.triat.Convention;

import java.util.List;

import static edu.ics.uci.optimizer.rule.PatternNode.any;
import static edu.ics.uci.optimizer.rule.PatternNode.operand;


public class LogicalJoinToPhysicalJoinRule implements TransformRule {
    public static final LogicalJoinToPhysicalJoinRule INSTANCE = new LogicalJoinToPhysicalJoinRule();
    private final String description;
    private final PatternNode matchPattern;

    public LogicalJoinToPhysicalJoinRule() {
        this.description = this.getClass().getName();
        this.matchPattern = operand().withClass(LogicalJoinOperator.class).children(any()).build();
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

        PhysicalJoinOperator physicalJoinOperator = new PhysicalJoinOperator(logicalJoinOperator.getCondition());
        inputs.forEach(subsetNode -> subsetNode.getTraitSet().replace(Convention.PHYSICAL));
        OperatorNode joinOperatorNode = OperatorNode.create(ruleCall.getContext(), physicalJoinOperator,
                logicalJoinOpN.getTraitSet().replace(Convention.PHYSICAL), inputs);

        ruleCall.transformTo(joinOperatorNode);

    }

    public String getDescription() {
        return description;
    }
}
