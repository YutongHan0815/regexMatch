package edu.ics.uci.regex.rules;


import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.regex.operators.*;

import java.io.Serializable;
import java.util.*;

import static edu.ics.uci.optimizer.rule.PatternNode.any;
import static edu.ics.uci.optimizer.rule.PatternNode.operand;

public class JoinCommutativeRule implements TransformRule, Serializable {

    public static final JoinCommutativeRule INSTANCE = new JoinCommutativeRule();

    private final String description;
    private final PatternNode matchPattern;

    public JoinCommutativeRule() {
        this.description = this.getClass().getName();
        this.matchPattern = operand().withClass(LogicalJoinOperator.class).children(any()).build();
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
        List<SubsetNode> inputs = logicalJoinOpN.getInputs();
        if(inputs.size() != 2)
            throw new UnsupportedOperationException("the inputs of Join not equal to 2 is not implements");
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

        OperatorNode joinOperatorNode = OperatorNode.create(ruleCall.getContext(), newJoin, logicalJoinOpN.getTraitSet(), Arrays.asList(inputs.get(1), inputs.get(0)));

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
