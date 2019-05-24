package edu.ics.uci.regex.rules;


import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.LogicalMatchOperator;
import edu.ics.uci.regex.operators.PhysicalMatchOperator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.triat.Convention;

import java.io.Serializable;


public class LogicalMatchToPhysicalMatchRule implements TransformRule, Serializable {

    public static final LogicalMatchToPhysicalMatchRule INSTANCE = new LogicalMatchToPhysicalMatchRule();
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
        final OperatorNode logicalMatchOpN = ruleCall.getOperator(0);
        final LogicalMatchOperator logicalMatchOperator = logicalMatchOpN.getOperator();

        PhysicalMatchOperator physicalMatchOperator = new PhysicalMatchOperator(logicalMatchOperator.getSubRegex());
        OperatorNode matchOperatorNode = OperatorNode.create(physicalMatchOperator, logicalMatchOpN.getTraitSet().replace(Convention.PHYSICAL));

        ruleCall.transformTo(matchOperatorNode);

    }
}