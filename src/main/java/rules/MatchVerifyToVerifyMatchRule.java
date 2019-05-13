package rules;

import operators.Operator;
import operators.PhysicalMatchOperator;
import operators.PhysicalVerifyOperator;
import plan.OperatorInput;
import plan.PatternNode;
import plan.RuleCall;
import plan.SetNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchVerifyToVerifyMatchRule implements TransformationRule, Serializable {

    private final String description;
    private final PatternNode mainPattern;

    public MatchVerifyToVerifyMatchRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.exact(PhysicalVerifyOperator.class,
                Arrays.asList(PatternNode.any(PhysicalMatchOperator.class)));
    }
    @Override
    public PatternNode getMatchPattern() {
        return mainPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final PhysicalMatchOperator physicalMatchOperator = ruleCall.getMatchedOperator(0);
        final PhysicalVerifyOperator physicalVerifyOperator = ruleCall.getMatchedOperator(1);

        SetNode matchSetNode = new SetNode();
        List<Operator> inputOperatorList = new ArrayList<>();
        inputOperatorList.add(physicalVerifyOperator);

        OperatorInput optInput = new OperatorInput(physicalMatchOperator, inputOperatorList);

        matchSetNode.operatorList.add(optInput);

        SetNode verifyNode = new SetNode(matchSetNode);

        matchSetNode.addNode(verifyNode);
        ruleCall.transformTo(matchSetNode);

    }
}
