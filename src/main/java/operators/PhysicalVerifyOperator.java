package operators;



import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;


public class PhysicalVerifyOperator implements Operator, Serializable {

    private final String subRegex;
    private final VerifyCondition verifyCondition;




    public PhysicalVerifyOperator(String subRegex, VerifyCondition verifyCondition) {
        Preconditions.checkNotNull(subRegex);
        Preconditions.checkNotNull(verifyCondition);


        this.subRegex = subRegex;
        this.verifyCondition = verifyCondition;
    }

    public String getSubRegex() {
        return subRegex;
    }

    public VerifyCondition getVerifyCondition() {
        return verifyCondition;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalVerifyOperator that = (PhysicalVerifyOperator) o;
        return subRegex.equals(that.subRegex) &&
                verifyCondition == that.verifyCondition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDigest());
    }
    public String getDigest() {
        return "PhysicalVerifyOperator(subRegex=" + subRegex + verifyCondition + ")";
    }
}

