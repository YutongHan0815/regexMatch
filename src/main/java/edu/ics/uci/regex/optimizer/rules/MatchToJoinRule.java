package edu.ics.uci.regex.optimizer.rules;

import edu.ics.uci.optimizer.operator.EquivSet;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.optimizer.operators.*;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;

import java.io.Serializable;
import java.util.*;

import static edu.ics.uci.optimizer.rule.PatternNode.any;
import static edu.ics.uci.optimizer.rule.PatternNode.operand;

public class MatchToJoinRule implements TransformRule, Serializable {
    public static final MatchToJoinRule INSTANCE = new MatchToJoinRule();
    private final String description;
    private final PatternNode matchPattern;

    public MatchToJoinRule() {
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

        LogicalJoinOperator newJoin = new LogicalJoinOperator(Condition.AFTER);
        List<SubRegex> subRegexList = logicalMatchOperator.decompose();
        LogicalMatchOperator newLeftMatch = new LogicalMatchOperator(subRegexList.get(0));
        LogicalMatchOperator newRightMatch = new LogicalMatchOperator(subRegexList.get(1));

        OperatorNode leftOperatorNode = OperatorNode.create(ruleCall.getContext(), newLeftMatch,
                logicalMatchOpN.getTraitSet(), logicalMatchOpN.getInputs());
        OperatorNode rightOperatorNode = OperatorNode.create(ruleCall.getContext(), newRightMatch,
                logicalMatchOpN.getTraitSet(), logicalMatchOpN.getInputs());
        EquivSet leftEquivSet = EquivSet.create(ruleCall.getContext(), leftOperatorNode);
        EquivSet rightEquivSet = EquivSet.create(ruleCall.getContext(), rightOperatorNode);

        SubsetNode leftMatchSubsetNode = SubsetNode.create(leftEquivSet, leftOperatorNode.getTraitSet());
        SubsetNode rightMatchSubsetNode = SubsetNode.create(rightEquivSet, rightOperatorNode.getTraitSet());

        OperatorNode joinOperatorNode = OperatorNode.create(ruleCall.getContext(), newJoin,
                leftOperatorNode.getTraitSet(), Arrays.asList(leftMatchSubsetNode, rightMatchSubsetNode));

        ruleCall.transformTo(joinOperatorNode);


    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchToJoinRule that = (MatchToJoinRule) o;
        return description.equals(that.description) &&
                matchPattern.equals(that.matchPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, matchPattern);
    }
}
