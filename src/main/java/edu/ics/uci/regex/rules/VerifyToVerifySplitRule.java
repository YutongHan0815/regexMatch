package edu.ics.uci.regex.rules;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;

import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.Condition;
import edu.ics.uci.regex.operators.LogicalVerifyOperator;
import edu.ics.uci.optimizer.operator.MetaSet;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VerifyToVerifySplitRule implements TransformRule, Serializable {
    public static final VerifyToVerifySplitRule INSTANCE = new VerifyToVerifySplitRule();
    private final String description;
    private final PatternNode mainPattern;

    public VerifyToVerifySplitRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(LogicalVerifyOperator.class,
                op -> op.isComposable() && !op.getCondition().equals(Condition.EQUAL));
    }

    public String getDescription() {
        return description;
    }

    public PatternNode getMainPattern() {
        return mainPattern;
    }

    @Override
    public PatternNode getMatchPattern() {
        return mainPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final OperatorNode logicalVerifyOpN = ruleCall.getOperator(0);
        final LogicalVerifyOperator logicalVerifyOperator = logicalVerifyOpN.getOperator();

        List<String> subRegexList = logicalVerifyOperator.decompose();

        LogicalVerifyOperator newVerify0 = new LogicalVerifyOperator(subRegexList.get(1), Condition.AFTER);
        LogicalVerifyOperator newVerify1 = new LogicalVerifyOperator(subRegexList.get(0), logicalVerifyOperator.getCondition());

        OperatorNode verifyOperatorNode1 = OperatorNode.create(newVerify1, logicalVerifyOpN.getTraitSet());
        MetaSet verifyMetaSet = MetaSet.create(verifyOperatorNode1);
        SubsetNode verifySubsetNode = SubsetNode.create(verifyMetaSet, logicalVerifyOpN.getTraitSet());
        OperatorNode verifyOperatorNode0 = OperatorNode.create(newVerify0, verifyOperatorNode1.getTraitSet(), Collections.singletonList(verifySubsetNode));


        ruleCall.transformTo(verifyOperatorNode0);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerifyToVerifySplitRule that = (VerifyToVerifySplitRule) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(mainPattern, that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
