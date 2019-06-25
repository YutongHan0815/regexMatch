package edu.ics.uci.regex.optimizer.operators;

import edu.ics.uci.regex.optimizer.expression.Expression;

public class LogicalJoinOperator extends JoinOperator {

    public LogicalJoinOperator(Expression condition) {
        super(condition);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "LogicalJoinOperator{} " + super.toString();
    }
}
