package edu.ics.uci.regex.optimizer.rules;


import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.regex.optimizer.expression.ComparisonExpr;

import edu.ics.uci.regex.optimizer.expression.Expression;
import edu.ics.uci.regex.optimizer.expression.InputRef;
import edu.ics.uci.regex.optimizer.operators.*;

import java.io.Serializable;
import java.util.*;

import static edu.ics.uci.optimizer.rule.PatternNode.*;
import static edu.ics.uci.regex.optimizer.expression.ComparisonExpr.ComparisionType.*;

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

        Expression condition = ComparisonExpr.of(GE,
                InputRef.of(0, InputRef.SpanAccess.END), InputRef.of(1, InputRef.SpanAccess.START));


        final LogicalJoinOperator logicalJoinOperator = logicalJoinOpN.getOperator();

        final InputRef inputRef0 = InputRef.of(0, InputRef.SpanAccess.START);
        final InputRef inputRef1 = InputRef.of(1, InputRef.SpanAccess.END);

        switch ((ComparisonExpr.ComparisionType)logicalJoinOperator.getCondition().getOperator()) {
            case EQ:
                condition = ComparisonExpr.of(EQ, inputRef0, inputRef1);
                break;
            case LT:
                condition = ComparisonExpr.of(GT, inputRef0, inputRef1);
                break;
            case LE:
                condition = ComparisonExpr.of(GE, inputRef0, inputRef1);
                break;
            case GE:
                condition = ComparisonExpr.of(LE, inputRef0, inputRef1);
                break;
            case GT:
                condition = ComparisonExpr.of(LT, inputRef0, inputRef1);
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
