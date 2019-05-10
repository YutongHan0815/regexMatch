package rules;

import plan.PhysicalVerifyOperator;

import java.io.Serializable;
import java.util.Objects;

public class VerifyToReverseVerifySplitRule implements TransformationRule, Serializable {

    private final String description;
    private final PatternNode mainPattern;

    public VerifyToReverseVerifySplitRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(PhysicalVerifyOperator.class);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerifyToReverseVerifySplitRule that = (VerifyToReverseVerifySplitRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
