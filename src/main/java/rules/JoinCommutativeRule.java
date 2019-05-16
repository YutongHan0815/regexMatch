package rules;


import operators.*;
import plan.*;

import java.io.Serializable;
import java.util.*;

public class JoinCommutativeRule implements TransformationRule, Serializable {

    public static final JoinCommutativeRule INSTANCE = new JoinCommutativeRule();

    private final String description;
    private final PatternNode mainPattern;

    public JoinCommutativeRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.exact(PhysicalJoinOperator.class,
                Arrays.asList(PatternNode.any(PhysicalMatchOperator.class),
                        PatternNode.any(PhysicalMatchOperator.class)));
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
        final LogicalJoinOperator logicalJoinOperator = ruleCall.getMatchedOperator(0);
        final LogicalMatchOperator leftMatchOpt = ruleCall.getMatchedOperator(1);
        final LogicalMatchOperator rightMatchOpt = ruleCall.getMatchedOperator(2);
        JoinCondition condition = JoinCondition.JOIN_AFTER;
       if(logicalJoinOperator.getJoinCondition() == JoinCondition.JOIN_BEFORE)
            condition = JoinCondition.JOIN_AFTER;

        LogicalJoinOperator newJoin = new LogicalJoinOperator(condition);
        LogicalMatchOperator newLeftMatch = new LogicalMatchOperator(rightMatchOpt.getSubRegex());
        LogicalMatchOperator newRightMatch = new LogicalMatchOperator(leftMatchOpt.getSubRegex());
        OperatorNode leftOperatorNode = OperatorNode.create(newLeftMatch);
        OperatorNode rightOperatorNode = OperatorNode.create(newRightMatch);

        SetNode leftMatchNode = SetNode.create(leftOperatorNode);
        SetNode rightMatchNode = SetNode.create(rightOperatorNode);

        OperatorNode joinOperatorNode = OperatorNode.create(newJoin, Arrays.asList(leftMatchNode, rightMatchNode));

        SetNode joinSetNode = SetNode.create(joinOperatorNode);

        ruleCall.transformTo(joinSetNode);


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
