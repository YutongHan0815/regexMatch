package rules;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;
import operators.*;
import plan.MetaSet;
import plan.OperatorNode;
import plan.PatternNode;
import plan.rule.RuleCall;
import plan.SubsetNode;
import plan.triat.TraitSet;

import java.io.Serializable;
import java.util.*;

public class MatchToJoinRule implements TransformationRule, Serializable {
    public static final MatchToJoinRule INSTANCE = new MatchToJoinRule();
    private final String description;
    private final PatternNode mainPattern;

    public MatchToJoinRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.any(PhysicalMatchOperator.class, op->isComposable(op));
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
        final OperatorNode logicalMatchOpN = ruleCall.getMatchedOperator(0);
        final LogicalMatchOperator logicalMatchOperator = logicalMatchOpN.getOperator();

        LogicalJoinOperator newJoin = new LogicalJoinOperator(JoinCondition.JOIN_AFTER);
        List<String> subRegexList = decompose(logicalMatchOperator);
        LogicalMatchOperator newLeftMatch = new LogicalMatchOperator(subRegexList.get(0));
        LogicalMatchOperator newRightMatch = new LogicalMatchOperator(subRegexList.get(1));

        OperatorNode leftOperatorNode = OperatorNode.create(newLeftMatch, logicalMatchOpN.getTraitSet());
        OperatorNode rightOperatorNode = OperatorNode.create(newRightMatch, logicalMatchOpN.getTraitSet());
        MetaSet leftMetaSet = MetaSet.create(leftOperatorNode);
        MetaSet rightMetaSet = MetaSet.create(rightOperatorNode);

        SubsetNode leftMatchSubsetNode = SubsetNode.create(leftMetaSet, leftOperatorNode.getTraitSet());
        SubsetNode rightMatchSubsetNode = SubsetNode.create(rightMetaSet, rightOperatorNode.getTraitSet());

        OperatorNode joinOperatorNode = OperatorNode.create(newJoin, leftOperatorNode.getTraitSet(), Arrays.asList(leftMatchSubsetNode, rightMatchSubsetNode));

        ruleCall.transformTo(joinOperatorNode);


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
    public static boolean isComposable(PhysicalMatchOperator op) {

        final String regex = op.getSubRegex();
        PublicRegexp re = PublicParser.parse(regex, PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        return re.getOp() != PublicRegexp.PublicOp.CONCAT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchToJoinRule that = (MatchToJoinRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
