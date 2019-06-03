package edu.ics.uci.optimizer;

import edu.ics.uci.optimizer.operator.MetaSet;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class TestRules {
    public TransformRule JoinCommutativeRule;

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
    @Test
    public void testJoinCommutativeRule() {

    }


}
