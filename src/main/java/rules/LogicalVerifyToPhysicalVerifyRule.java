package rules;

import operators.LogicalVerifyOperator;

import operators.PhysicalVerifyOperator;
import plan.OperatorInput;
import plan.PatternNode;
import plan.RuleCall;
import plan.SetNode;

import java.io.Serializable;
import java.util.ArrayList;

public class LogicalVerifyToPhysicalVerifyRule implements TransformationRule, Serializable {

    private final String description;

    private final PatternNode matchPattern;

    public LogicalVerifyToPhysicalVerifyRule() {

        this.description = this.getClass().getName();
        this.matchPattern = PatternNode.any(LogicalVerifyOperator.class);
    }

    public String getDescription() {
        return description;
    }

    @Override
    public PatternNode getMatchPattern() {

        return matchPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final PhysicalVerifyOperator physicalVerifyOperator = ruleCall.getMatchedOperator(0);

        final SetNode verifySetNode = new SetNode();
        OperatorInput optInput = new OperatorInput(physicalVerifyOperator, new ArrayList<>());
        verifySetNode.operatorList.add(optInput);
        ruleCall.transformTo(verifySetNode);

    }


}
