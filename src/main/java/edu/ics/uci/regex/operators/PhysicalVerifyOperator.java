package edu.ics.uci.regex.operators;



import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.Operator;

import java.io.Serializable;
import java.util.Objects;


public class PhysicalVerifyOperator implements Operator, Serializable {

    private final String subRegex;
    private final Condition condition;




    public PhysicalVerifyOperator(String subRegex, Condition condition) {
        Preconditions.checkNotNull(subRegex);
        Preconditions.checkNotNull(condition);


        this.subRegex = subRegex;
        this.condition = condition;
    }

    public String getSubRegex() {
        return subRegex;
    }

    public Condition getCondition() {
        return condition;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalVerifyOperator that = (PhysicalVerifyOperator) o;
        return subRegex.equals(that.subRegex) &&
                condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDigest());
    }
    public String getDigest() {
        return "PhysicalVerifyOperator(subRegex=" + subRegex + condition + ")";
    }
}

