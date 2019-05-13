package operators;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;

public class PhysicalJoinOperator implements Operator, Serializable {

    private Operator leftOperator;
    private Operator rightOperator;

    private final JoinCondition joinCondition;


    public PhysicalJoinOperator(Operator leftOperator, Operator rightOperator, JoinCondition joinCondition) {
        Preconditions.checkNotNull(leftOperator);
        Preconditions.checkNotNull(rightOperator);
        Preconditions.checkNotNull(joinCondition);

        this.leftOperator = leftOperator;
        this.rightOperator = rightOperator;
        this.joinCondition = joinCondition;

    }

    public Operator getLeftOperator() {
        return leftOperator;
    }

    public void setLeftOperator(Operator leftOperator) {
        this.leftOperator = leftOperator;
    }

    public Operator getRightOperator() {
        return rightOperator;
    }

    public void setRightOperator(Operator rightOperator) {
        this.rightOperator = rightOperator;
    }

    public JoinCondition getJoinCondition() {
        return joinCondition;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalJoinOperator that = (PhysicalJoinOperator) o;
        return leftOperator.equals(that.leftOperator) &&
                rightOperator.equals(that.rightOperator) &&
                joinCondition.equals(that.joinCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDigest());
    }
    public String getDigest() {
        return "PhysicalJoinOperator(" + leftOperator.toString()+rightOperator.toString() + ")";
    }

}
