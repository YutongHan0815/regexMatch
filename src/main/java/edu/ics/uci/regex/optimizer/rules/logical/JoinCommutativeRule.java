package edu.ics.uci.regex.optimizer.rules.logical;


import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.regex.optimizer.expression.*;

import edu.ics.uci.regex.optimizer.operators.*;

import java.io.Serializable;
import java.util.*;

import static edu.ics.uci.optimizer.rule.PatternNode.*;
import static edu.ics.uci.regex.optimizer.expression.ComparisonExpr.ComparisionType.*;

/**
 * Logical JoinCommutativeRule that exchange the left child and right child of Join and rewrite the join ComparisonExpr{@link ComparisonExpr}
 *
 * ComparisonExpr1 {@link ComparisonExpr} and ComparisonExpr2 {@link ComparisonExpr} satisfied:
 *       ComparisonExpr,       SpanInputRef0,       SpanInputRef1
 * 1)     EQ|LT|LE|GT|GE,           (0, END)       (1, START)
 *    =>  EQ|GT|GE|LT|LE,           (0, START)      (1, END)
 *
 * 2) BooleanExp  (EQ (0, END), (2, START)) AND (EQ (2, END), (1, START))
 *     =>         (EQ (0, END), (2, START)) AND (EQ (0, START), (1, END))
 *
 *          Join (ComparisonExpr1)        <=>            Join (ComparisonExpr2)
 *          /     \                                       /     \
 *         /       \                                     /       \
 *        /         \                                   /         \
 *       ANY1       ANY2                               ANY2       ANY1
 */
public class JoinCommutativeRule implements TransformRule, Serializable {

    public static final JoinCommutativeRule INSTANCE = new JoinCommutativeRule();

    private final String description;
    private final PatternNode matchPattern;

    public JoinCommutativeRule() {
        this.description = this.getClass().getName();
        this.matchPattern = operand(LogicalJoinOperator.class)
                .children(exact(Arrays.asList(operand(Operator.class)
                                .children(any()),
                        operand(Operator.class)
                                .children(any()))))
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

        Expression condition = null;


        final LogicalJoinOperator logicalJoinOperator = logicalJoinOpN.getOperator();
        Expression joinExpression = logicalJoinOperator.getCondition();
        if (joinExpression instanceof BooleanExpr
                && ((BooleanExpr) joinExpression).getOperator() == BooleanExpr.BooleanType.AND) {

            ExprOperand exprOperand = ComparisonExpr.of(EQ,
                        SpanInputRef.of(1, SpanInputRef.SpanAccess.END),
                        SpanInputRef.of(0, SpanInputRef.SpanAccess.START));

            condition = BooleanExpr.of(BooleanExpr.BooleanType.AND,
                    Arrays.asList(joinExpression.getOperands().get(0), exprOperand));

           // System.out.println("boolean expression" + condition);
        } else {
            final SpanInputRef firstSpanInputRef = (SpanInputRef) joinExpression.getOperands().get(0);
            final SpanInputRef secondSpanInputRef = (SpanInputRef) joinExpression.getOperands().get(1);

            SpanInputRef spanInputRef0 = SpanInputRef.of(0, SpanInputRef.SpanAccess.START);
            SpanInputRef spanInputRef1 = SpanInputRef.of(1, SpanInputRef.SpanAccess.END);

            if (firstSpanInputRef.getSpanAccess() == SpanInputRef.SpanAccess.START
                    && secondSpanInputRef.getSpanAccess() == SpanInputRef.SpanAccess.END) {
                spanInputRef0 = SpanInputRef.of(0, SpanInputRef.SpanAccess.END);
                spanInputRef1 = SpanInputRef.of(1, SpanInputRef.SpanAccess.START);
            }


            // System.out.println(logicalJoinOperator.getCondition().toString());

            switch ((ComparisonExpr.ComparisionType) joinExpression.getOperator()) {
                case EQ:
                    condition = ComparisonExpr.of(EQ, spanInputRef0, spanInputRef1);
                    break;
                case LT:
                    condition = ComparisonExpr.of(GT, spanInputRef0, spanInputRef1);
                    break;
                case LE:
                    condition = ComparisonExpr.of(GE, spanInputRef0, spanInputRef1);
                    break;
                case GE:
                    condition = ComparisonExpr.of(LE, spanInputRef0, spanInputRef1);
                    break;
                case GT:
                    condition = ComparisonExpr.of(LT, spanInputRef0, spanInputRef1);
                    break;
            }
        }
        LogicalJoinOperator newJoin = new LogicalJoinOperator(condition);

        SubsetNode newLeft = SubsetNode.create(ruleCall.getContext(), logicalRightOpN);
        SubsetNode newRight = SubsetNode.create(ruleCall.getContext(), logicalLeftOpN);
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
