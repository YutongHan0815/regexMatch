package plan;

import regexMatcher.SubRegex;

import java.io.Serializable;
import java.util.function.Predicate;

public class PhysicalVerifyOperator implements Operator, Serializable {




    private final String subRegex;
    private final VerifyCondition verifyCondition;

    public PhysicalVerifyOperator(String subRegex,VerifyCondition verifyCondition) {
        this.subRegex = subRegex;
        this.verifyCondition = verifyCondition;
    }

    public String getSubRegex() {
        return subRegex;
    }

    public VerifyCondition getVerifyCondition() {
        return verifyCondition;
    }
}
