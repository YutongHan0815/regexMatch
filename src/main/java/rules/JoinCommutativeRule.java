package rules;

import javafx.util.Pair;
import operators.Operator;
import operators.PhysicalJoinOperator;
import operators.PhysicalMatchOperator;
import plan.*;

import java.io.Serializable;
import java.util.*;

public class JoinCommutativeRule implements TransformationRule, Serializable {
    private final String description;
    private final PatternNode mainPattern;


    public JoinCommutativeRule() {
        this.description = this.getClass().getName();
        this.mainPattern = PatternNode.exact(PhysicalJoinOperator.class,
                Arrays.asList(PatternNode.any(PhysicalMatchOperator.class),
                        PatternNode.any(PhysicalMatchOperator.class)));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinCommutativeRule that = (JoinCommutativeRule) o;
        return description.equals(that.description) &&
                mainPattern.equals(that.mainPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mainPattern);
    }
}
