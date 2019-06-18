package edu.ics.uci.regex.optimizer.operators;

import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.regex.runtime.regexMatcher.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.JoinRelations;

import java.io.Serializable;
import java.util.Objects;

public class PhysicalJoinOperator implements Operator, Serializable {

    private final Condition condition;

    public static PhysicalJoinOperator create(Condition condition) {
        return new PhysicalJoinOperator(condition);
    }
    public PhysicalJoinOperator(Condition condition) {
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
        PhysicalJoinOperator that = (PhysicalJoinOperator) o;
        return condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDigest());
    }

    @Override
    public String getDigest() {
        return "PhysicalJoinOperator(" + condition  +")";
    }
    @Override
    public ExecutionOperator getExecution() {
        JoinRelations matcher = new JoinRelations(this);
        return matcher;
    }
}
