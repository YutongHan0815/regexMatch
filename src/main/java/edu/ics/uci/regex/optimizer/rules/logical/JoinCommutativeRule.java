package edu.ics.uci.regex.optimizer.rules.logical;


import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.regex.optimizer.expression.BooleanExpr;
import edu.ics.uci.regex.optimizer.expression.ComparisonExpr;

import edu.ics.uci.regex.optimizer.expression.Expression;
import edu.ics.uci.regex.optimizer.expression.SpanInputRef;
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

        Expression condition = null;


        final LogicalJoinOperator logicalJoinOperator = logicalJoinOpN.getOperator();

        final SpanInputRef spanInputRef0 = SpanInputRef.of(0, SpanInputRef.SpanAccess.START);
        final SpanInputRef spanInputRef1 = SpanInputRef.of(1, SpanInputRef.SpanAccess.END);

        System.out.println(logicalJoinOperator.getCondition().toString());

        if(logicalJoinOperator.getCondition().getOperator() instanceof BooleanExpr.BooleanType)
            System.out.println("boolean expression");
        else {
            switch ((ComparisonExpr.ComparisionType) logicalJoinOperator.getCondition().getOperator()) {
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
        System.out.println(newJoin.getCondition().toString());


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
