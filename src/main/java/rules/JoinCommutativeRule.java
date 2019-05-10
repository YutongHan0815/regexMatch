package rules;

import operators.PhysicalJoinOperator;
import operators.PhysicalMatchOperator;
import plan.PatternNode;
import plan.RuleCall;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class JoinCommutativeRule implements TransformationRule, Serializable {
    private final String description;
    private final PatternNode mainPattern;


    public JoinCommutativeRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.exact(PhysicalJoinOperator.class,
                Arrays.asList(PatternNode.any(PhysicalMatchOperator.class),
                        PatternNode.any(PhysicalMatchOperator.class)));
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
        JoinCommutativeRule that = (JoinCommutativeRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
