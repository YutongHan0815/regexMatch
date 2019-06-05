package edu.ics.uci.regex.rules;


import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.*;
import edu.ics.uci.optimizer.operator.EquivSet;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;

import java.io.Serializable;

import java.util.*;

public class MatchToMatchVerifyRule implements TransformRule, Serializable {

    public static final MatchToMatchVerifyRule INSTANCE = new MatchToMatchVerifyRule();

    private final String description;
    private final PatternNode mainPattern;

    public MatchToMatchVerifyRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(LogicalMatchOperator.class, op-> op.isComposable());
    }

    public String getDescription() {
        return description;
    }

    @Override
    public PatternNode getMatchPattern() {
        return mainPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final OperatorNode logicalMatchOpN = ruleCall.getOperator(0);
        final LogicalMatchOperator logicalMatchOperator = logicalMatchOpN.getOperator();

        List<String> subRegexList = logicalMatchOperator.decompose();

        LogicalVerifyOperator newVerify = new LogicalVerifyOperator(subRegexList.get(1), Condition.AFTER);
        LogicalMatchOperator newMatch = new LogicalMatchOperator(subRegexList.get(0));
        OperatorNode matchOperatorNode = OperatorNode.create(ruleCall.getContext(), newMatch,
                logicalMatchOpN.getTraitSet(), logicalMatchOpN.getInputs());
        EquivSet matchEquivSet = EquivSet.create(ruleCall.getContext(), matchOperatorNode);
        SubsetNode matchSubsetNode = SubsetNode.create(matchEquivSet, matchOperatorNode.getTraitSet());
        OperatorNode verifyOperatorNode = OperatorNode.create(ruleCall.getContext(), newVerify,
                matchOperatorNode.getTraitSet(), Collections.singletonList(matchSubsetNode));

        ruleCall.transformTo(verifyOperatorNode);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchToMatchVerifyRule that = (MatchToMatchVerifyRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
