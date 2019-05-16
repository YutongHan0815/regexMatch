package plan;


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
                    MatchVerifyToReverseMatchVerifyRule.INSTANCE,
                    VerifyToVerifySplitRule.INSTANCE,
                    VerifyToReverseVerifySplitRule.INSTANCE
            );

}
