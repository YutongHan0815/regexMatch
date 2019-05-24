package edu.ics.uci.optimizer.rule;

import java.io.Serializable;

public interface TransformRule extends Serializable {

    PatternNode getMatchPattern();

    void onMatch(RuleCall ruleCall);

}
