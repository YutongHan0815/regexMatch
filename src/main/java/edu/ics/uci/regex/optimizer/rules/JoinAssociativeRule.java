package edu.ics.uci.regex.optimizer.rules;

import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.optimizer.operators.Condition;
import edu.ics.uci.regex.optimizer.operators.LogicalJoinOperator;
import edu.ics.uci.regex.optimizer.operators.LogicalMatchOperator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;


public class JoinAssociativeRule implements TransformRule, Serializable {
    public static final JoinAssociativeRule INSTANCE = new JoinAssociativeRule();

    private final String description;
    private final PatternNode mainPattern;

    public JoinAssociativeRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.exact(LogicalJoinOperator.class, op->op.getCondition().equals(Condition.AFTER),
                Arrays.asList(PatternNode.leaf(LogicalMatchOperator.class),
                        PatternNode.exact(LogicalJoinOperator.class, op->op.getCondition().equals(Condition.BEFORE),
                        Arrays.asList(PatternNode.leaf(LogicalMatchOperator.class), PatternNode.leaf(LogicalMatchOperator.class)))));
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
        final OperatorNode logicalJoinAfterOpN = ruleCall.getOperator(0);
        final OperatorNode logicalMatchOpNA = ruleCall.getOperator(1);
        final OperatorNode logicalJoinBeforeOpN = ruleCall.getOperator(2);
        final OperatorNode logicalMatchOpNC = ruleCall.getOperator(3);
        final OperatorNode logicalMatchOpNB = ruleCall.getOperator(4);

        OperatorNode matchOperatorA = OperatorNode.create(ruleCall.getContext(), logicalMatchOpNA.getOperator(),
                logicalJoinAfterOpN.getTraitSet(), logicalMatchOpNA.getInputs());
        OperatorNode matchOperatorC = OperatorNode.create(ruleCall.getContext(), logicalMatchOpNC.getOperator(),
                logicalJoinAfterOpN.getTraitSet(), logicalMatchOpNC.getInputs());

        SubsetNode subsetNodeA = SubsetNode.create(ruleCall.getContext(), matchOperatorA);
        SubsetNode subsetNodeC = SubsetNode.create(ruleCall.getContext(), matchOperatorC);

        OperatorNode logicalJoinAGOpN = OperatorNode.create(ruleCall.getContext(),
                new LogicalJoinOperator(Condition.GAP_AFTER), logicalJoinAfterOpN.getTraitSet(), Arrays.asList(subsetNodeA, subsetNodeC));
        SubsetNode subsetNodeJoin = SubsetNode.create(ruleCall.getContext(), logicalJoinAGOpN);

        OperatorNode matchOperatorB = OperatorNode.create(ruleCall.getContext(), logicalMatchOpNB.getOperator(),
                logicalJoinAfterOpN.getTraitSet(), logicalMatchOpNB.getInputs());
        SubsetNode subsetNodeB = SubsetNode.create(ruleCall.getContext(), matchOperatorB);

        OperatorNode logicalJoinEqualOpN = OperatorNode.create(ruleCall.getContext(),
                new LogicalJoinOperator(Condition.EQUAL), logicalJoinAfterOpN.getTraitSet(), Arrays.asList(subsetNodeJoin, subsetNodeB));

        ruleCall.transformTo(logicalJoinEqualOpN);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinAssociativeRule that = (JoinAssociativeRule) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(mainPattern, that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
