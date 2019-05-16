package rules;


import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;
import operators.*;
import plan.OperatorNode;
import plan.PatternNode;
import plan.RuleCall;
import plan.SetNode;

import java.io.Serializable;

import java.util.*;

public class MatchToMatchVerifyRule implements TransformationRule, Serializable {

    public static final MatchToMatchVerifyRule INSTANCE = new MatchToMatchVerifyRule();

    private final String description;
    private final PatternNode mainPattern;

    public MatchToMatchVerifyRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(LogicalMatchOperator.class, op->isComposable(op));
    }

    public String getDescription() {
        return description;
    }

    @Override
    public PatternNode getMatchPattern() {
        return mainPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final LogicalMatchOperator logicalMatchOperator = ruleCall.getMatchedOperator(0);

        List<String> subRegexList = decompose(logicalMatchOperator);

        LogicalVerifyOperator newVerify = new LogicalVerifyOperator(subRegexList.get(0), VerifyCondition.VERIFY_AFTER);
        LogicalMatchOperator newMatch = new LogicalMatchOperator(subRegexList.get(1));
        OperatorNode matchOperatorNode = OperatorNode.create(newMatch);

        SetNode matchSetNode = SetNode.create(matchOperatorNode);
        OperatorNode verifyOperatorNode = OperatorNode.create(newVerify, Collections.singletonList(matchSetNode));
        SetNode verifySetNode = SetNode.create(verifyOperatorNode);

        ruleCall.transformTo(verifySetNode);

    }

    public static List<String> decompose(LogicalMatchOperator op) {
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

    public static boolean isComposable(LogicalMatchOperator op) {

        final String regex = op.getSubRegex();
        PublicRegexp re = PublicParser.parse(regex, PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        return re.getOp() != PublicRegexp.PublicOp.CONCAT;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchToMatchVerifyRule that = (MatchToMatchVerifyRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
