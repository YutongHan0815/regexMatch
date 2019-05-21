package rules;

import plan.PatternNode;
import plan.rule.RuleCall;

public interface TransformationRule {

    PatternNode getMatchPattern();

    void onMatch(RuleCall ruleCall);

}
