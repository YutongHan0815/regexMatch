package rules;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;
import operators.Operator;
import operators.PhysicalJoinOperator;
import operators.PhysicalMatchOperator;
import operators.PhysicalVerifyOperator;
import plan.OperatorInput;
import plan.PatternNode;
import plan.RuleCall;
import plan.SetNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatchToJoinRule implements TransformationRule, Serializable {
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
        final PhysicalJoinOperator physicalJoinOperator = ruleCall.getMatchedOperator(0);
        final PhysicalMatchOperator leftMatchOpt = ruleCall.getMatchedOperator(1);
        final PhysicalMatchOperator rightMatchOpt = ruleCall.getMatchedOperator(2);

        SetNode joinSetNode = new SetNode();
        List<Operator> inputOperatorList = new ArrayList<>();
        inputOperatorList.add(leftMatchOpt);
        inputOperatorList.add(rightMatchOpt);
        OperatorInput optInput = new OperatorInput(physicalJoinOperator, inputOperatorList);

        joinSetNode.operatorList.add(optInput);

        SetNode leftMatchNode = new SetNode(joinSetNode);
        SetNode rightMatchNode = new SetNode(joinSetNode);

        joinSetNode.addNode(leftMatchNode);
        joinSetNode.addNode(rightMatchNode);

        ruleCall.transformTo(joinSetNode);


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
