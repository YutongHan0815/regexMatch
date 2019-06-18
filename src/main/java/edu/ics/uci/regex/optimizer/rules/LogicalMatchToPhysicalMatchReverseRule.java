package edu.ics.uci.regex.optimizer.rules;

import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.triat.Convention;
import edu.ics.uci.regex.optimizer.operators.LogicalMatchOperator;
import edu.ics.uci.regex.optimizer.operators.PhysicalMatchOperator;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class LogicalMatchToPhysicalMatchReverseRule implements TransformRule, Serializable {
    public static final LogicalMatchToPhysicalMatchReverseRule INSTANCE = new LogicalMatchToPhysicalMatchReverseRule();
    private final String description;
    private final PatternNode matchPattern;

    public LogicalMatchToPhysicalMatchReverseRule() {

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
        final List<SubsetNode> inputs = logicalMatchOpN.getInputs();

        PhysicalMatchOperator physicalMatchOperator = new PhysicalMatchOperator(logicalMatchOperator.getSubRegex(), false);
        inputs.forEach(subsetNode -> subsetNode.getTraitSet().replace(Convention.PHYSICAL));

        OperatorNode matchOperatorNode = OperatorNode.create(ruleCall.getContext(), physicalMatchOperator,
                logicalMatchOpN.getTraitSet().replace(Convention.PHYSICAL),inputs);

        ruleCall.transformTo(matchOperatorNode);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalMatchToPhysicalMatchReverseRule that = (LogicalMatchToPhysicalMatchReverseRule) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(matchPattern, that.matchPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, matchPattern);
    }
}
