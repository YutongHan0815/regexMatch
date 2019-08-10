package edu.ics.uci.regex.optimizer.rules.physical;

import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.triat.Convention;
import edu.ics.uci.regex.optimizer.operators.LogicalProjectOperator;
import edu.ics.uci.regex.optimizer.operators.PhysicalProjectOperator;

import java.io.Serializable;
import java.util.List;

import static edu.ics.uci.optimizer.rule.PatternNode.any;
import static edu.ics.uci.optimizer.rule.PatternNode.none;
import static edu.ics.uci.optimizer.rule.PatternNode.operand;

public class LogicalProjectToPhysicalProjectRule implements TransformRule, Serializable {
    public static final LogicalProjectToPhysicalProjectRule INSTANCE = new LogicalProjectToPhysicalProjectRule();

    private final String description;
    private final PatternNode matchPattern;

    public LogicalProjectToPhysicalProjectRule() {

        this.description = this.getClass().getName();
        this.matchPattern = operand(LogicalProjectOperator.class).children(any()).build();
    }

    @Override
    public PatternNode getMatchPattern() {
        return matchPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final OperatorNode logicalProjectOpN = ruleCall.getOperator(0);
        final LogicalProjectOperator logicalProjectOperator = logicalProjectOpN.getOperator();
        final List<SubsetNode> inputs = logicalProjectOpN.getInputs();

        PhysicalProjectOperator physicalProjectOperator = new PhysicalProjectOperator(
                logicalProjectOperator.getLeftIndex(), logicalProjectOperator.getRightIndex(),
                logicalProjectOperator.getResultIndex());
        inputs.forEach(subsetNode -> subsetNode.getTraitSet().replace(Convention.PHYSICAL));

        OperatorNode projectOperatorNode = OperatorNode.create(ruleCall.getContext(), physicalProjectOperator,
                logicalProjectOpN.getTraitSet().replace(Convention.PHYSICAL), inputs);

        ruleCall.transformTo(projectOperatorNode);
    }
}
