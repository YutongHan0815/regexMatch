package rules;

public interface TransformationRule {

    PatternNode getMatchPattern();

    void onMatch(RuleCall ruleCall);

}
