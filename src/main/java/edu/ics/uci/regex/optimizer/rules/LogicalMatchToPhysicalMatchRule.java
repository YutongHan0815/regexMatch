package edu.ics.uci.regex.optimizer.rules;


import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.optimizer.operators.LogicalJoinOperator;
import edu.ics.uci.regex.optimizer.operators.LogicalMatchOperator;
import edu.ics.uci.regex.optimizer.operators.PhysicalMatchOperator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.triat.Convention;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static edu.ics.uci.optimizer.rule.PatternNode.*;


public class LogicalMatchToPhysicalMatchRule implements TransformRule, Serializable {

    public static final LogicalMatchToPhysicalMatchRule INSTANCE = new LogicalMatchToPhysicalMatchRule();
    private final String description;
    private final PatternNode matchPattern;

    public LogicalMatchToPhysicalMatchRule() {

        this.description = this.getClass().getName();
        this.matchPattern = operand(LogicalMatchOperator.class).children(any()).build();
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
        final List<SubsetNode> inputs = logicalMatchOpN.getInputs();

        PhysicalMatchOperator physicalMatchOperator = PhysicalMatchOperator.create(logicalMatchOperator.getSubRegex());
        inputs.forEach(subsetNode -> subsetNode.getTraitSet().replace(Convention.PHYSICAL));

        OperatorNode matchOperatorNode = OperatorNode.create(ruleCall.getContext(), physicalMatchOperator,
                logicalMatchOpN.getTraitSet().replace(Convention.PHYSICAL), inputs);

        ruleCall.transformTo(matchOperatorNode);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalMatchToPhysicalMatchRule that = (LogicalMatchToPhysicalMatchRule) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(matchPattern, that.matchPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, matchPattern);
    }
}
