package rules;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;

import operators.LogicalVerifyOperator;
import operators.PhysicalVerifyOperator;
import operators.VerifyCondition;
import plan.OperatorNode;
import plan.PatternNode;
import plan.rule.RuleCall;
import plan.SubsetNode;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VerifyToVerifySplitRule implements TransformationRule, Serializable {
    public static final VerifyToVerifySplitRule INSTANCE = new VerifyToVerifySplitRule();
    private final String description;
    private final PatternNode mainPattern;

    public VerifyToVerifySplitRule() {
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
        final LogicalVerifyOperator logicalVerifyOperator = ruleCall.getMatchedOperator(0);

        List<String> subRegexList = decompose(logicalVerifyOperator);

        LogicalVerifyOperator newVerify0 = new LogicalVerifyOperator(subRegexList.get(1), VerifyCondition.VERIFY_AFTER);
        LogicalVerifyOperator newVerify1 = new LogicalVerifyOperator(subRegexList.get(0), logicalVerifyOperator.getVerifyCondition());

        OperatorNode verifyOperatorNode1 = OperatorNode.create(newVerify1);
        SubsetNode verifySubsetNode = SubsetNode.create(verifyOperatorNode1);
        OperatorNode verifyOperatorNode0 = OperatorNode.create(newVerify0, Collections.singletonList(verifySubsetNode));
        SubsetNode verifySubsetNode0 = SubsetNode.create(verifyOperatorNode0);


        ruleCall.transformTo(verifySubsetNode0);
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
        VerifyToVerifySplitRule that = (VerifyToVerifySplitRule) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(mainPattern, that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
