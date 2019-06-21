package edu.ics.uci.regex.optimizer.operators;

import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.regex.runtime.regexMatcher.ExecutionOperator;

import java.io.Serializable;
import java.util.Objects;

public class LogicalJoinOperator implements Operator, Serializable {

    private final Condition condition;


    public LogicalJoinOperator(Condition condition) {
        Preconditions.checkNotNull(condition);
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalJoinOperator that = (LogicalJoinOperator) o;
        return condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition);
    }

    @Override
    public String toString() {
        return "LogicalJoinOperator{" +
                "condition=" + condition +
                '}';
    }
}
