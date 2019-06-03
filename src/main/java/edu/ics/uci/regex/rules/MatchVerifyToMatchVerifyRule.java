package edu.ics.uci.regex.rules;

import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.*;
import edu.ics.uci.optimizer.operator.MetaSet;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

public class MatchVerifyToMatchVerifyRule implements TransformRule, Serializable {

    public static final MatchVerifyToMatchVerifyRule INSTANCE = new MatchVerifyToMatchVerifyRule();
    private final String description;
    private final PatternNode mainPattern;

    public MatchVerifyToMatchVerifyRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.exact(LogicalVerifyOperator.class, op->op.getCondition().equals(Condition.AFTER),
                Arrays.asList(PatternNode.any(LogicalMatchOperator.class)));
    }
    @Override
    public PatternNode getMatchPattern() {
        return mainPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {

        final OperatorNode logicalVerifyOpN = ruleCall.getOperator(0);
        final OperatorNode logicalMatchOpN = ruleCall.getOperator(1);

        final LogicalMatchOperator logicalMatchOperator = logicalMatchOpN.getOperator();
        final LogicalVerifyOperator logicalVerifyOperator = logicalVerifyOpN.getOperator();

        LogicalVerifyOperator newVerify = new LogicalVerifyOperator(logicalMatchOperator.getSubRegex(), Condition.BEFORE);
        LogicalMatchOperator newMatch = new LogicalMatchOperator(logicalVerifyOperator.getSubRegex());

        OperatorNode matchOperatorNode = OperatorNode.create(newMatch, logicalMatchOpN.getTraitSet(), logicalMatchOpN.getInputs());

        MetaSet matchMetaSet = MetaSet.create(matchOperatorNode);
        SubsetNode matchSubsetNode = SubsetNode.create(matchMetaSet, matchOperatorNode.getTraitSet());
        OperatorNode verifyOperatorNode = OperatorNode.create(newVerify, matchOperatorNode.getTraitSet(), matchSubsetNode);

        ruleCall.transformTo(verifyOperatorNode);


    }

}
