package edu.ics.uci.regex.rules;


import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.operator.MetaSet;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.regex.operators.*;
import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.util.*;

public class JoinCommutativeRule implements TransformRule, Serializable {

    public static final JoinCommutativeRule INSTANCE = new JoinCommutativeRule();

    private final String description;
    private final PatternNode mainPattern;

    public JoinCommutativeRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(LogicalJoinOperator.class);
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
//
//        final OperatorNode leftMatchOpN = ruleCall.getOperator(1);
//        final OperatorNode rightMatchOpN = ruleCall.getOperator(2);
//        final LogicalMatchOperator leftMatchOpt = leftMatchOpN.getOperator();
//        final LogicalMatchOperator rightMatchOpt = rightMatchOpN.getOperator();
//
//
          LogicalJoinOperator newJoin = new LogicalJoinOperator(condition);
//        LogicalMatchOperator newLeftMatch = new LogicalMatchOperator(rightMatchOpt.getSubRegex());
//        LogicalMatchOperator newRightMatch = new LogicalMatchOperator(leftMatchOpt.getSubRegex());
//        OperatorNode leftOperatorNode = OperatorNode.create(newLeftMatch, rightMatchOpN.getTraitSet(), rightMatchOpN.getInputs());
//        OperatorNode rightOperatorNode = OperatorNode.create(newRightMatch, leftMatchOpN.getTraitSet(), leftMatchOpN.getInputs());
//
//        MetaSet leftMatchNode = MetaSet.create(leftOperatorNode);
//        MetaSet rightMatchNode = MetaSet.create(rightOperatorNode);
//        SubsetNode leftSetNode = SubsetNode.create(leftMatchNode, leftOperatorNode.getTraitSet());
//        SubsetNode rightSetNode = SubsetNode.create(rightMatchNode, rightOperatorNode.getTraitSet());

        OperatorNode joinOperatorNode = OperatorNode.create(newJoin, logicalJoinOpN.getTraitSet(),Arrays.asList(inputs.get(1), inputs.get(0)));

        ruleCall.transformTo(joinOperatorNode);


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinCommutativeRule that = (JoinCommutativeRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
