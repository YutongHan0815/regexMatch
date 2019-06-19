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

public class LogicalJoinToPhysicalVerifyRule implements TransformRule, Serializable {
    public static final LogicalJoinToPhysicalVerifyRule INSTANCE = new LogicalJoinToPhysicalVerifyRule();
    private final String description;
    private final PatternNode matchPattern;

    public LogicalJoinToPhysicalVerifyRule() {
        this.description = this.getClass().getName();

        this.matchPattern = operand(LogicalJoinOperator.class)
                .children(exact(Arrays.asList(operand(Operator.class).children(any()),
                        operand(LogicalMatchOperator.class).children(any()))))
                .build();
    }

    @Override
    public PatternNode getMatchPattern() {
        return matchPattern;
    }
    public String getDescription() {
        return description;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final OperatorNode logicalJoinOpN = ruleCall.getOperator(0);
        final LogicalJoinOperator logicalJoinOperator = logicalJoinOpN.getOperator();
        final List<SubsetNode> inputs = logicalJoinOpN.getInputs();
        PhysicalVerifyJoinOperator physicalVerifyJoinOperator = PhysicalVerifyJoinOperator.create(logicalJoinOperator.getCondition(), new SubRegex(""));
        inputs.forEach(subsetNode -> subsetNode.getTraitSet().replace(Convention.PHYSICAL));
        OperatorNode verifyJoinOperatorNode = OperatorNode.create(ruleCall.getContext(), physicalVerifyJoinOperator,
                logicalJoinOpN.getTraitSet().replace(Convention.PHYSICAL), inputs);

        ruleCall.transformTo(verifyJoinOperatorNode);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalJoinToPhysicalVerifyRule that = (LogicalJoinToPhysicalVerifyRule) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(matchPattern, that.matchPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, matchPattern);
    }
}
