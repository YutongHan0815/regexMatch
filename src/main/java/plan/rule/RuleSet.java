package plan.rule;


import com.google.common.collect.ImmutableList;
import rules.*;

import java.io.Serializable;


public class RuleSet implements Serializable {

    private static final ImmutableList<TransformationRule> DEFAULT_RULES=
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
