package operators;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;

public class LogicalJoinOperator implements Operator, Serializable {

    private final JoinCondition joinCondition;

    public LogicalJoinOperator(JoinCondition joinCondition) {
        Preconditions.checkNotNull(joinCondition);
        this.joinCondition = joinCondition;
    }

    public JoinCondition getJoinCondition() {
        return joinCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalJoinOperator that = (LogicalJoinOperator) o;
        return joinCondition == that.joinCondition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(joinCondition);
    }

    public String getDigest() {
        return "LogicalJoinOperator(" + joinCondition + ")";
    }

}
