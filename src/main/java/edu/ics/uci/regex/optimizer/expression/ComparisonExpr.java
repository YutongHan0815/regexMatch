package edu.ics.uci.regex.optimizer.expression;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;

public class ComparisonExpr implements Expression {

    public enum ComparisionType implements ExprOperator {
        GT,
        GE,
        EQ,
        LT,
        LE,
        NE
    }

    private final ComparisionType type;
    private final ImmutableList<ExprOperand> operands;

    public static ComparisonExpr of(ComparisionType type, ExprOperand... operands) {
        return new ComparisonExpr(type, ImmutableList.copyOf(operands));
    }

    public static ComparisonExpr of(ComparisionType type, List<ExprOperand> operands) {
        return new ComparisonExpr(type, operands);
    }

    private ComparisonExpr(ComparisionType type, List<ExprOperand> operands) {
        this.type = type;
        this.operands = ImmutableList.copyOf(operands);
    }

    @Override
    public ComparisonExpr copyWithNewOperands(List<ExprOperand> newOperands) {
        return of(this.type, newOperands);
    }

    @Override
    public ExprOperator getOperator() {
        return type;
    }

    @Override
    public List<ExprOperand> getOperands() {
        return operands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparisonExpr that = (ComparisonExpr) o;
        return type == that.type &&
                Objects.equals(operands, that.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, operands);
    }

    @Override
    public String toString() {
        return "ComparisonExpr{" +
                "type=" + type +
                ", operands=" + operands +
                '}';
    }
}
