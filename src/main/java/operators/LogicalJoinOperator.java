package operators;

import java.io.Serializable;
import java.util.Objects;

public class LogicalJoinOperator implements Operator, Serializable {

    private final Operator leftOperator;
    private final Operator rightOperator;

    private final JoinCondition joinCondition;

    public LogicalJoinOperator(Operator leftOperator, Operator rightOperator, JoinCondition joinCondition) {
        this.leftOperator = leftOperator;
        this.rightOperator = rightOperator;
        this.joinCondition = joinCondition;
    }

    public Operator getLeftOperator() {
        return leftOperator;
    }

    public Operator getRightOperator() {
        return rightOperator;
    }

    public JoinCondition getJoinCondition() {
        return joinCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalJoinOperator that = (LogicalJoinOperator) o;
        return Objects.equals(leftOperator, that.leftOperator) &&
                Objects.equals(rightOperator, that.rightOperator) &&
                joinCondition == that.joinCondition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDigest());
    }

    public String getDigest() {
        return "LogicalJoinOperator(" + leftOperator.toString() + rightOperator.toString() + joinCondition + ")";
    }

}
