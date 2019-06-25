package edu.ics.uci.regex.optimizer.operators;

import edu.ics.uci.optimizer.operator.PhysicalOperator;
import edu.ics.uci.regex.optimizer.expression.Expression;
import edu.ics.uci.regex.runtime.regexMatcher.execution.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.execution.JoinRelations;

public class PhysicalJoinOperator extends JoinOperator implements PhysicalOperator {

    public PhysicalJoinOperator(Expression condition) {
        super(condition);
    }

    @Override
    public ExecutionOperator getExecution() {
        JoinRelations matcher = new JoinRelations(this);
        return matcher;
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
        return "PhysicalJoinOperator{} " + super.toString();
    }
}
