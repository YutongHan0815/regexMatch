package rules;


import operators.PhysicalMatchOperator;
import operators.PhysicalVerifyOperator;
import plan.PatternNode;
import plan.RuleCall;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class MatchToMatchVerifyRule implements TransformationRule, Serializable {

    private final String description;
    private final PatternNode mainPattern;

    public MatchToMatchVerifyRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(PhysicalMatchOperator.class);

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
        final PhysicalMatchOperator physicalMatchOperator = ruleCall.getMatchedOperator(1);

        PatternNode newPattern =PatternNode.exact(physicalVerifyOperator.getClass(),
                Arrays.asList(PatternNode.any(physicalMatchOperator.getClass())));

        //ruleCall.transformTo(newPattern);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchToMatchVerifyRule that = (MatchToMatchVerifyRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
