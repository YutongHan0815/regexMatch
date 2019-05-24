package edu.ics.uci.regex.rules;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.operators.*;
import edu.ics.uci.optimizer.operator.MetaSet;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.operator.SubsetNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VerifyToReverseVerifySplitRule implements TransformRule, Serializable {

    public static final VerifyToReverseVerifySplitRule INSTANCE = new VerifyToReverseVerifySplitRule();
    private final String description;
    private final PatternNode mainPattern;

    public VerifyToReverseVerifySplitRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(PhysicalVerifyOperator.class, op -> isComposable(op));
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

        List<String> subRegexList = decompose(logicalVerifyOperator);

        LogicalVerifyOperator newVerify0 = new LogicalVerifyOperator(subRegexList.get(0), VerifyCondition.VERIFY_BEFORE);
        LogicalVerifyOperator newVerify1 = new LogicalVerifyOperator(subRegexList.get(1), logicalVerifyOperator.getVerifyCondition());

        OperatorNode verifyOperatorNode1 = OperatorNode.create(newVerify1, logicalVerifyOpN.getTraitSet());
        MetaSet  verifyMetaSet = MetaSet.create(verifyOperatorNode1);
        SubsetNode verifySubsetNode = SubsetNode.create(verifyMetaSet, verifyOperatorNode1.getTraitSet());
        OperatorNode verifyOperatorNode0 = OperatorNode.create(newVerify0, verifyOperatorNode1.getTraitSet(), Collections.singletonList(verifySubsetNode));

        ruleCall.transformTo(verifyOperatorNode0);
    }

    public static List<String> decompose(LogicalVerifyOperator op) {
        List<String> subRegexList = new ArrayList<>();

        PublicRegexp re = PublicParser.parse(op.getSubRegex(), PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        if(re.getOp() != PublicRegexp.PublicOp.CONCAT) {

        }
        else {
            PublicRegexp[] subs = re.getSubs();
            String subRegex1 = subs[0].toString();
            String subRegex2 = new String();
            subRegexList.add(subRegex1);
            for(int i=1; i<subs.length; i++) {
                subRegex2 += subs[i].toString();
            }
            subRegexList.add(subRegex2);
        }


        return subRegexList;
    }
    public static boolean isComposable(PhysicalVerifyOperator op) {

        final String regex = op.getSubRegex();
        PublicRegexp re = PublicParser.parse(regex, PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        return re.getOp() != PublicRegexp.PublicOp.CONCAT;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerifyToReverseVerifySplitRule that = (VerifyToReverseVerifySplitRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}