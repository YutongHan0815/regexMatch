package rules;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;
import plan.PhysicalVerifyOperator;
import regexMatcher.SubRegex;

import java.io.Serializable;
import java.util.Objects;

public class VerifyToVerifySplitRule implements TransformationRule, Serializable {
    private final String description;
    private final PatternNode mainPattern;

    public VerifyToVerifySplitRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(PhysicalVerifyOperator.class, VerifyToVerifySplitRule::isComposable());
    }

    public String getDescription() {
        return description;
    }

    public PatternNode getMainPattern() {
        return mainPattern;
    }

    @Override
    public PatternNode getMatchPattern() {
        return mainPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final PhysicalVerifyOperator physicalVerifyOperator = ruleCall.getMatchedOperator(0);
        final PhysicalVerifyOperator childVerifyOperator = ruleCall.getMatchedOperator(1);

        ruleCall.transformTo(physicalVerifyOperator);

    }

    public static boolean isComposable(PhysicalVerifyOperator verifyOperator) {
        final String regex = verifyOperator.getSubRegex();
        PublicRegexp re = PublicParser.parse(regex, PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        return re.getOp() != PublicRegexp.PublicOp.CONCAT;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerifyToVerifySplitRule that = (VerifyToVerifySplitRule) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(mainPattern, that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
