package edu.ics.uci.optimizer.rule;


import com.google.common.collect.ImmutableList;
import edu.ics.uci.regex.optimizer.rules.logical.JoinAssociativeRule;
import edu.ics.uci.regex.optimizer.rules.logical.JoinCommutativeRule;
import edu.ics.uci.regex.optimizer.rules.logical.MatchToJoinRule;
import edu.ics.uci.regex.optimizer.rules.logical.ProjectJoinTransposeRule;
import edu.ics.uci.regex.optimizer.rules.physical.*;

import java.io.Serializable;


public class RuleSet implements Serializable {

    public static final ImmutableList<TransformRule> DEFAULT_RULES=
            ImmutableList.of(
                    JoinCommutativeRule.INSTANCE,
                    MatchToJoinRule.INSTANCE,
                    LogicalJoinToPhysicalJoinRule.INSTANCE,
                    LogicalMatchToPhysicalMatchRule.INSTANCE,
                    JoinAssociativeRule.INSTANCE,
                    LogicalJoinToPhysicalVerifyRule.INSTANCE,
                    LogicalJoinToPhysicalVerifyReverseRule.INSTANCE,
                    LogicalMatchToPhysicalMatchReverseRule.INSTANCE,
                    LogicalProjectToPhysicalProjectRule.INSTANCE,
                    ProjectJoinTransposeRule.BOTH_PROJECT,
                    ProjectJoinTransposeRule.LEFT_PROJECT,
                    ProjectJoinTransposeRule.RIGHT_PROJECT

            );

}
