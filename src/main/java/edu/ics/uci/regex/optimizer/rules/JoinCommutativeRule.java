package edu.ics.uci.regex.optimizer.rules;


import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.regex.optimizer.operators.*;

import java.io.Serializable;
import java.util.*;

import static edu.ics.uci.optimizer.rule.PatternNode.*;

public class JoinCommutativeRule implements TransformRule, Serializable {

    public static final JoinCommutativeRule INSTANCE = new JoinCommutativeRule();

    private final String description;
    private final PatternNode matchPattern;

    public JoinCommutativeRule() {
        this.description = this.getClass().getName();
        this.matchPattern = operand(LogicalJoinOperator.class)
                .children(exact(Arrays.asList(operand(Operator.class).children(any()),
                        operand(Operator.class).children(any()))))
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
        final OperatorNode logicalLeftOpN = ruleCall.getOperator(1);
        final OperatorNode logicalRightOpN = ruleCall.getOperator(2);

        Condition condition = Condition.EQUAL;

        final LogicalJoinOperator logicalJoinOperator = logicalJoinOpN.getOperator();
        switch (logicalJoinOperator.getCondition()) {
            case AFTER:
                condition = Condition.BEFORE;
                break;
            case BEFORE:
                condition = Condition.AFTER;
                break;
            case GAP_AFTER:
                condition = Condition.GAP_BEFORE;
                break;
            case GAP_BEFORE:
                condition = Condition.GAP_AFTER;
                break;
        }
        LogicalJoinOperator newJoin = new LogicalJoinOperator(condition);

        SubsetNode newLeft = SubsetNode.create(ruleCall.getContext(), logicalLeftOpN);
        SubsetNode newRight = SubsetNode.create(ruleCall.getContext(), logicalRightOpN);
        OperatorNode joinOperatorNode = OperatorNode.create(ruleCall.getContext(), newJoin, logicalJoinOpN.getTraitSet(), Arrays.asList(newLeft, newRight));

        ruleCall.transformTo(joinOperatorNode);


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinCommutativeRule that = (JoinCommutativeRule) o;
        return description.equals(that.description) &&
                matchPattern.equals(that.matchPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, matchPattern);
    }
}
