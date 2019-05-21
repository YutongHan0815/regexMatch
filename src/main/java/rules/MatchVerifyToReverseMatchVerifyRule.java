package rules;

import operators.*;
import plan.OperatorNode;
import plan.PatternNode;
import plan.rule.RuleCall;
import plan.SubsetNode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

public class MatchVerifyToReverseMatchVerifyRule implements TransformationRule, Serializable {

    public static final MatchVerifyToReverseMatchVerifyRule INSTANCE = new MatchVerifyToReverseMatchVerifyRule();
    private final String description;
    private final PatternNode mainPattern;

    public MatchVerifyToReverseMatchVerifyRule() {
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
        final LogicalMatchOperator logicalMatchOperator = ruleCall.getMatchedOperator(0);
        final LogicalVerifyOperator logicalVerifyOperator = ruleCall.getMatchedOperator(1);

        LogicalVerifyOperator newVerify = new LogicalVerifyOperator(logicalMatchOperator.getSubRegex(), VerifyCondition.VERIFY_BEFORE);
        LogicalMatchOperator newMatch = new LogicalMatchOperator(logicalVerifyOperator.getSubRegex());

        OperatorNode matchOperatorNode = OperatorNode.create(newMatch);

        SubsetNode matchSubsetNode = SubsetNode.create(matchOperatorNode);
        OperatorNode verifyOperatorNode = OperatorNode.create(newVerify, Collections.singletonList(matchSubsetNode));
        SubsetNode verifySubsetNode = SubsetNode.create(verifyOperatorNode);


        ruleCall.transformTo(verifySubsetNode);

    }
}
