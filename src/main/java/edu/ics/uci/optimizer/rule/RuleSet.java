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
                    MatchToJoinRule.INSTANCE,
                    JoinAssociativeRule.INSTANCE,
                    JoinCommutativeRule.INSTANCE,
                    ProjectJoinTransposeRule.BOTH_PROJECT,
                    ProjectJoinTransposeRule.LEFT_PROJECT,
                    ProjectJoinTransposeRule.RIGHT_PROJECT,
                    LogicalJoinToPhysicalJoinRule.INSTANCE,
                    LogicalMatchToPhysicalMatchRule.INSTANCE,
                    LogicalMatchToPhysicalMatchReverseRule.INSTANCE,
                    LogicalJoinToPhysicalVerifyRule.INSTANCE,
                    LogicalJoinToPhysicalVerifyReverseRule.INSTANCE,
                    LogicalProjectToPhysicalProjectRule.INSTANCE

            );

    public static final ImmutableList<TransformRule> LOGICAL_RULES=
            ImmutableList.of(
                    MatchToJoinRule.INSTANCE,
                    JoinAssociativeRule.INSTANCE,
                    JoinCommutativeRule.INSTANCE,
                    ProjectJoinTransposeRule.BOTH_PROJECT,
                    ProjectJoinTransposeRule.LEFT_PROJECT,
                    ProjectJoinTransposeRule.RIGHT_PROJECT

            );

    public static final ImmutableList<TransformRule> LOGICALTOPHYSICAL_RULES=
            ImmutableList.of(
                    LogicalJoinToPhysicalJoinRule.INSTANCE,
                    LogicalMatchToPhysicalMatchRule.INSTANCE,
                    LogicalMatchToPhysicalMatchReverseRule.INSTANCE,
                    LogicalJoinToPhysicalVerifyRule.INSTANCE,
                    LogicalJoinToPhysicalVerifyReverseRule.INSTANCE,
                    LogicalProjectToPhysicalProjectRule.INSTANCE

            );

}
