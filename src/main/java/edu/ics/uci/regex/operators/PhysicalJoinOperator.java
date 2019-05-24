package edu.ics.uci.regex.operators;

import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.Operator;

import java.io.Serializable;
import java.util.Objects;

public class PhysicalJoinOperator implements Operator, Serializable {

    private final JoinCondition joinCondition;


    public PhysicalJoinOperator(JoinCondition joinCondition) {
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
        PhysicalJoinOperator that = (PhysicalJoinOperator) o;
        return joinCondition.equals(that.joinCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDigest());
    }
    public String getDigest() {
        return "PhysicalJoinOperator(" + joinCondition + ")";
    }

}
