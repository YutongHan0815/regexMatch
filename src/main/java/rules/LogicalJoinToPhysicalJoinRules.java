package rules;

import javafx.util.Pair;
import operators.LogicalJoinOperator;
import operators.Operator;
import operators.PhysicalJoinOperator;
import plan.OperatorInput;
import plan.PatternNode;
import plan.RuleCall;
import plan.SetNode;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class LogicalJoinToPhysicalJoinRules implements TransformationRule{

    private final String description;

    private final PatternNode matchPattern;

    public LogicalJoinToPhysicalJoinRules() {

        this.description = this.getClass().toString();

        this.matchPattern = PatternNode.any(LogicalJoinOperator.class);
    }

    @Override
    public PatternNode getMatchPattern() {

        return matchPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        final PhysicalJoinOperator physicalJoinOperator = ruleCall.getMatchedOperator(0);
        final SetNode joinSetNode = new SetNode();
        OperatorInput optInput = new OperatorInput(physicalJoinOperator, new ArrayList<>());
        joinSetNode.operatorList.add(optInput);

        ruleCall.transformTo(joinSetNode);

    }

    public String getDescription() {
        return description;
    }
}
