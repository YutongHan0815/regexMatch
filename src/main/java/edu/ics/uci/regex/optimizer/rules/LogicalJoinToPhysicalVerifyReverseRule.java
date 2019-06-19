package edu.ics.uci.regex.optimizer.rules;

import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.triat.Convention;
import edu.ics.uci.regex.optimizer.operators.LogicalJoinOperator;
import edu.ics.uci.regex.optimizer.operators.LogicalMatchOperator;
import edu.ics.uci.regex.optimizer.operators.PhysicalVerifyJoinOperator;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static edu.ics.uci.optimizer.rule.PatternNode.*;

public class LogicalJoinToPhysicalVerifyReverseRule implements TransformRule, Serializable {
    public static final LogicalJoinToPhysicalVerifyReverseRule INSTANCE = new LogicalJoinToPhysicalVerifyReverseRule();
    private final String description;
    private final PatternNode matchPattern;

    public LogicalJoinToPhysicalVerifyReverseRule() {
        this.description = this.getClass().getName();

        this.matchPattern = operand(LogicalJoinOperator.class)
                .children(exact(Arrays.asList(operand(Operator.class).children(any()),
                        operand(LogicalMatchOperator.class).children(none()))))
                .build();

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

        final OperatorNode logicalJoinOpN = ruleCall.getOperator(0);
        final LogicalJoinOperator logicalJoinOperator = logicalJoinOpN.getOperator();
        final List<SubsetNode> inputs = logicalJoinOpN.getInputs();

        PhysicalVerifyJoinOperator physicalVerifyJoinOperator = new PhysicalVerifyJoinOperator(logicalJoinOperator.getCondition(), false, new SubRegex("ll"));
        inputs.forEach(subsetNode -> subsetNode.getTraitSet().replace(Convention.PHYSICAL));
        OperatorNode verifyJoinOperatorNode = OperatorNode.create(ruleCall.getContext(), physicalVerifyJoinOperator,
                logicalJoinOpN.getTraitSet().replace(Convention.PHYSICAL), inputs);

        ruleCall.transformTo(verifyJoinOperatorNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalJoinToPhysicalVerifyReverseRule that = (LogicalJoinToPhysicalVerifyReverseRule) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(matchPattern, that.matchPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, matchPattern);
    }
}
