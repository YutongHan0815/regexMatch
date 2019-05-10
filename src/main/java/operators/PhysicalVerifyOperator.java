package operators;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;


import java.io.Serializable;


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

    public boolean isComposable() {
        final String regex = this.getSubRegex();
        PublicRegexp re = PublicParser.parse(regex, PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        return re.getOp() != PublicRegexp.PublicOp.CONCAT;
    }
}

