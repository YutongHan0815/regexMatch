package edu.ics.uci.regex.rules;

import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.Condition;
import edu.ics.uci.regex.operators.LogicalMatchOperator;
import edu.ics.uci.regex.operators.LogicalVerifyOperator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class MatchVerifyToReverseVerifyRule implements TransformRule, Serializable {
    private final String description;
    private final PatternNode mainPattern;
    public static final MatchVerifyToReverseVerifyRule INSTANCE = new MatchVerifyToReverseVerifyRule();

    public MatchVerifyToReverseVerifyRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.exact(LogicalVerifyOperator.class, op->op.getCondition().equals(Condition.AFTER) && op.isComposable(),
                Arrays.asList(PatternNode.any(LogicalMatchOperator.class)));
    }

    public String getDescription() {
        return description;
    }

    public PatternNode getMainPattern() {
        return mainPattern;
    }


    @Override
    public PatternNode getMatchPattern() {
        return mainPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final OperatorNode logicalVerifyOpN = ruleCall.getOperator(0);
        final OperatorNode logicalMatchOpN = ruleCall.getOperator(1);
        final LogicalVerifyOperator logicalVerifyOperator = logicalVerifyOpN.getOperator();
        final LogicalMatchOperator logicalMatchOperator = logicalMatchOpN.getOperator();

        List<String> subRegexList = logicalVerifyOperator.decompose();

        SubsetNode subsetNode = SubsetNode.create(ruleCall.getContext(), logicalMatchOpN);

        String subRegexRight= subRegexList.get(1);
        LogicalVerifyOperator verifyGapOp = new LogicalVerifyOperator(subRegexRight, Condition.GAP_AFTER);
        OperatorNode verifyGapOpN = OperatorNode.create(ruleCall.getContext(),
                verifyGapOp, logicalVerifyOpN.getTraitSet(), subsetNode);
        SubsetNode subsetNodeGap = SubsetNode.create(ruleCall.getContext(), verifyGapOpN);

        String subRegexLeft= subRegexList.get(0);
        LogicalVerifyOperator verifyEqualOp = new LogicalVerifyOperator(subRegexLeft, Condition.EQUAL);
        OperatorNode verifyEqualOpN = OperatorNode.create(ruleCall.getContext(),
                verifyEqualOp, logicalVerifyOpN.getTraitSet(), subsetNodeGap);
        SubsetNode subsetNodeEqual = SubsetNode.create(ruleCall.getContext(), verifyEqualOpN);

        //TODO get left subRegex in the mainRegex
        String subRegexMatch= logicalMatchOperator.getSubRegex();
        LogicalVerifyOperator operatorLeft = new LogicalVerifyOperator(subRegexMatch, Condition.BEFORE);
        OperatorNode newVerifyOpN = OperatorNode.create(ruleCall.getContext(),
                operatorLeft, logicalVerifyOpN.getTraitSet(), subsetNodeEqual);
        SubsetNode subsetNodeLeft = SubsetNode.create(ruleCall.getContext(), newVerifyOpN);

        //TODO get left subRegex in the mainRegex
        LogicalVerifyOperator operatorRight = new LogicalVerifyOperator(subRegexRight, Condition.AFTER);
        OperatorNode rightVerifyOpN = OperatorNode.create(ruleCall.getContext(),
                operatorRight, logicalVerifyOpN.getTraitSet(), subsetNodeLeft);

        ruleCall.transformTo(rightVerifyOpN);


    }
}
