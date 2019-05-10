package rules;

import plan.PhysicalMatchOperator;
import plan.PhysicalVerifyOperator;

import java.io.Serializable;
import java.util.Arrays;

public class MatchVerifyToVerifyMatchRule implements TransformationRule, Serializable {

    private final String description;
    private final PatternNode mainPattern;

    public MatchVerifyToVerifyMatchRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.exact(PhysicalVerifyOperator.class,
                Arrays.asList(PatternNode.any(PhysicalMatchOperator.class)));
    }
    @Override
    public PatternNode getMatchPattern() {
        return mainPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final PhysicalMatchOperator physicalMatchOperator = ruleCall.getMatchedOperator(0);
        final PhysicalVerifyOperator physicalVerifyOperator = ruleCall.getMatchedOperator(1);

        //ruleCall.transformTo();

    }
}
