package edu.ics.uci.optimizer.rule;


import com.google.common.collect.ImmutableList;
import edu.ics.uci.regex.rules.*;

import java.io.Serializable;


public class RuleSet implements Serializable {

    public static final ImmutableList<TransformRule> DEFAULT_RULES=
            ImmutableList.of(
                    LogicalJoinToPhysicalJoinRule.INSTANCE,
                    LogicalVerifyToPhysicalVerifyRule.INSTANCE,
                    LogicalMatchToPhysicalMatchRule.INSTANCE,
                    JoinCommutativeRule.INSTANCE,
                    MatchToMatchVerifyRule.INSTANCE,
                    MatchToJoinRule.INSTANCE,
                    MatchVerifyToMatchVerifyRule.INSTANCE,
                    VerifyToVerifySplitRule.INSTANCE,
                    VerifyToReverseVerifySplitRule.INSTANCE
            );

}
