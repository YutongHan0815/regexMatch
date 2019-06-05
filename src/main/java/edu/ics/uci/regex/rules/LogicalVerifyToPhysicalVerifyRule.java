package edu.ics.uci.regex.rules;

import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.LogicalVerifyOperator;

import edu.ics.uci.regex.operators.PhysicalVerifyOperator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.triat.Convention;

import java.io.Serializable;
import java.util.List;

public class LogicalVerifyToPhysicalVerifyRule implements TransformRule, Serializable {

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
        final OperatorNode logicalVerifyOpN = ruleCall.getOperator(0);
        final LogicalVerifyOperator logicalVerifyOperator = logicalVerifyOpN.getOperator();
        final List<SubsetNode> inputs = logicalVerifyOpN.getInputs();

        PhysicalVerifyOperator physicalVerifyOperator = new PhysicalVerifyOperator(
                logicalVerifyOperator.getSubRegex(), logicalVerifyOperator.getCondition());
        OperatorNode verifyOperatorNode = OperatorNode.create(ruleCall.getContext(), physicalVerifyOperator,
                logicalVerifyOpN.getTraitSet().replace(Convention.PHYSICAL),inputs);

        ruleCall.transformTo(verifyOperatorNode);

    }


}
