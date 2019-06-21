package edu.ics.uci.regex.optimizer.expression;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;

public class BooleanExpr implements Expression {

    public enum BooleanType implements ExprOperator {
        AND,
        OR,
        NOT
    }

    private final BooleanType type;
    private final ImmutableList<ExprOperand> operands;

    public static BooleanExpr of(BooleanType type, List<ExprOperand> operands) {
        return new BooleanExpr(type, operands);
    }

    private BooleanExpr(BooleanType type, List<ExprOperand> operands) {
        this.type = type;
        this.operands = ImmutableList.copyOf(operands);
    }

    @Override
    public BooleanType getOperator() {
        return type;
    }

    @Override
    public ImmutableList<ExprOperand> getOperands() {
        return operands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanExpr that = (BooleanExpr) o;
        return type == that.type &&
                Objects.equals(operands, that.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, operands);
    }

    @Override
    public String toString() {
        return "BooleanExpr{" +
                "type=" + type +
                ", operands=" + operands +
                '}';
    }
}
