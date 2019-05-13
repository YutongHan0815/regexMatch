package operators;

import java.io.Serializable;
import java.util.Objects;

public class LogicalVerifyOperator implements Operator, Serializable {
    private final String subRegex;
    private final VerifyCondition verifyCondition;

    public LogicalVerifyOperator(String subRegex, VerifyCondition verifyCondition) {
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
        LogicalVerifyOperator that = (LogicalVerifyOperator) o;
        return subRegex.equals(that.subRegex) &&
                verifyCondition == that.verifyCondition;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getDigest());
    }

    public String getDigest() {
        return "LogicalVerifyOperator(" + subRegex + ")";
    }


}
