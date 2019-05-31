package edu.ics.uci.regex.operators;

import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.Operator;

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
        return Objects.hash(getDigest());
    }
    @Override
    public String getDigest() {
        return "LogicalJoinOperator(" + condition + ")";
    }

}
