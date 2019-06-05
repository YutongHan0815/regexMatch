package edu.ics.uci.regex.rules;

import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.Condition;
import edu.ics.uci.regex.operators.LogicalJoinOperator;
import edu.ics.uci.regex.operators.LogicalMatchOperator;
import edu.ics.uci.regex.operators.LogicalVerifyOperator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VerifyToJoinRule implements TransformRule, Serializable {
    private final String description;
    private final PatternNode mainPattern;
    public static final VerifyToJoinRule INSTANCE = new VerifyToJoinRule();

    public VerifyToJoinRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(LogicalVerifyOperator.class, op->!op.getCondition().equals(Condition.EQUAL));
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
        final LogicalVerifyOperator logicalVerifyOperator = logicalVerifyOpN.getOperator();
        String subRegex = logicalVerifyOperator.getSubRegex();


        LogicalMatchOperator matchOperator= new LogicalMatchOperator(subRegex);
        OperatorNode newMatchOpN = OperatorNode.create(ruleCall.getContext(), matchOperator,
                logicalVerifyOpN.getTraitSet());
        SubsetNode subsetNode = SubsetNode.create(ruleCall.getContext(), newMatchOpN);
        List<SubsetNode> newSubsets = new ArrayList<>();
        logicalVerifyOpN.getInputs().forEach(subsetNode1->newSubsets.add(subsetNode1));
        newSubsets.add(subsetNode);
        LogicalJoinOperator joinOperator = new LogicalJoinOperator(logicalVerifyOperator.getCondition());
        OperatorNode joinOperatorNode = OperatorNode.create(ruleCall.getContext(), joinOperator,
                logicalVerifyOpN.getTraitSet(), newSubsets);

        ruleCall.transformTo(joinOperatorNode);

    }
}
