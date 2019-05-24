package edu.ics.uci.optimizer;

import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;

public class TestRules {

    public static TransformRule dummyRule(PatternNode pattern) {
        return new TransformRule() {
            @Override
            public PatternNode getMatchPattern() {
                return pattern;
            }

            @Override
            public void onMatch(RuleCall ruleCall) {
            }
        };
    }

}
