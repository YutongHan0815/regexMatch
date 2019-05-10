package rules;

import plan.PatternNode;
import plan.RuleCall;

public interface TransformationRule {

    PatternNode getMatchPattern();

    void onMatch(RuleCall ruleCall);

}
