package rules;

import operators.*;
import plan.MetaSet;
import plan.OperatorNode;
import plan.PatternNode;
import plan.rule.RuleCall;
import plan.SubsetNode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

public class MatchVerifyToMatchVerifyRule implements TransformationRule, Serializable {

    public static final MatchVerifyToMatchVerifyRule INSTANCE = new MatchVerifyToMatchVerifyRule();
    private final String description;
    private final PatternNode mainPattern;

    public MatchVerifyToMatchVerifyRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.exact(PhysicalVerifyOperator.class,
                Arrays.asList(PatternNode.any(PhysicalMatchOperator.class)));
    }
    @Override
    public PatternNode getMatchPattern() {
        return mainPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {

        final OperatorNode logicalMatchOpN = ruleCall.getMatchedOperator(0);
        final OperatorNode logicalVerifyOpN = ruleCall.getMatchedOperator(1);

        final LogicalMatchOperator logicalMatchOperator = logicalMatchOpN.getOperator();
        final LogicalVerifyOperator logicalVerifyOperator = logicalVerifyOpN.getOperator();

        LogicalVerifyOperator newVerify = new LogicalVerifyOperator(logicalMatchOperator.getSubRegex(), VerifyCondition.VERIFY_BEFORE);
        LogicalMatchOperator newMatch = new LogicalMatchOperator(logicalVerifyOperator.getSubRegex());

        OperatorNode matchOperatorNode = OperatorNode.create(newMatch, logicalMatchOpN.getTraitSet());

        MetaSet matchMetaSet = MetaSet.create(matchOperatorNode);
        SubsetNode matchSubsetNode = SubsetNode.create(matchMetaSet, matchOperatorNode.getTraitSet());
        OperatorNode verifyOperatorNode = OperatorNode.create(newVerify, matchOperatorNode.getTraitSet(), Collections.singletonList(matchSubsetNode));


        ruleCall.transformTo(verifyOperatorNode);

    }
}
