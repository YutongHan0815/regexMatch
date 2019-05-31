package edu.ics.uci.regex.rules;

import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.Condition;
import edu.ics.uci.regex.operators.LogicalVerifyOperator;

import java.io.Serializable;


public class VerifyWithEqualConditionRule implements TransformRule, Serializable {
    private final String description;
    private final PatternNode mainPattern;
    public static final VerifyWithEqualConditionRule INSTANCE = new VerifyWithEqualConditionRule();

    public VerifyWithEqualConditionRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(LogicalVerifyOperator.class, op->op.getCondition().equals(Condition.EQUAL));
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
        SubsetNode subsetNode = SubsetNode.create(logicalVerifyOpN);

        //TODO get left subRegex in the mainRegex
        String subRegexLeft = "left";
        LogicalVerifyOperator operatorLeft = new LogicalVerifyOperator(subRegexLeft, Condition.BEFORE);
        OperatorNode newVerifyOpN = OperatorNode.create(operatorLeft, logicalVerifyOpN.getTraitSet(), subsetNode);
        SubsetNode subsetNodeLeft = SubsetNode.create(newVerifyOpN);

        //TODO get left subRegex in the mainRegex
        String subRegexRight= "right";
        LogicalVerifyOperator operatorRight = new LogicalVerifyOperator(subRegexRight, Condition.AFTER);
        OperatorNode rightVerifyOpN = OperatorNode.create(operatorRight, logicalVerifyOpN.getTraitSet(), subsetNodeLeft);

        ruleCall.transformTo(rightVerifyOpN);


    }
}
