package rules;

import operators.PhysicalJoinOperator;
import operators.PhysicalMatchOperator;
import plan.PatternNode;
import plan.RuleCall;

import java.io.Serializable;
import java.util.Objects;

public class MatchToJoinRule implements TransformationRule, Serializable {
    private final String description;
    private final PatternNode mainPattern;

    public MatchToJoinRule() {
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
        final PhysicalJoinOperator physicalJoinOperator = ruleCall.getMatchedOperator(0);
        final PhysicalMatchOperator leftMatchOpt = ruleCall.getMatchedOperator(1);
        final PhysicalMatchOperator rightMatchOpt = ruleCall.getMatchedOperator(2);

        ruleCall.transformTo(physicalJoinOperator);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchToJoinRule that = (MatchToJoinRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
