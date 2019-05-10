package operators;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;

public class PhysicalJoinOperator implements Operator, Serializable {

    private Operator leftOpertor;
    private Operator rightOperator;

    private final JoinCondition joinCondition;


    public PhysicalJoinOperator(Operator leftOpertor, Operator rightOperator, JoinCondition joinCondition) {
        Preconditions.checkNotNull(leftOpertor);
        Preconditions.checkNotNull(rightOperator);
        Preconditions.checkNotNull(joinCondition);

        this.leftOpertor = leftOpertor;
        this.rightOperator = rightOperator;
        this.joinCondition = joinCondition;

    }

    public Operator getLeftOpertor() {
        return leftOpertor;
    }

    public void setLeftOpertor(Operator leftOpertor) {
        this.leftOpertor = leftOpertor;
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
        return leftOpertor.equals(that.leftOpertor) &&
                rightOperator.equals(that.rightOperator) &&
                joinCondition.equals(that.joinCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOpertor, rightOperator, joinCondition);
    }
}
