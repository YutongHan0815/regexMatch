package edu.ics.uci.optimizer.rule;


import edu.ics.uci.optimizer.OptimizerPlanner;
import edu.ics.uci.optimizer.operator.OperatorNode;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class RuleCall implements Serializable {

    private final OptimizerPlanner planner;
    private final TransformRule rule;
    private final Map<Integer, OperatorNode> matchedOperators;

    public RuleCall(OptimizerPlanner planner, TransformRule rule, Map<Integer, OperatorNode> matchedOperators) {
        this.planner = planner;
        this.rule = rule;
        this.matchedOperators = matchedOperators;
    }

    public TransformRule getRule() {
        return rule;
    }

    public OperatorNode getOperator(int ordinal) {
        return matchedOperators.get(ordinal);
    }

    public void transformTo(OperatorNode equivalentOperator) {
        this.planner.registerOperator(equivalentOperator, planner.getOperatorToSet().get(planner.getOperators().inverse().get(matchedOperators.get(0))));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleCall ruleCall = (RuleCall) o;
        return Objects.equals(planner, ruleCall.planner) &&
                Objects.equals(rule, ruleCall.rule) &&
                Objects.equals(matchedOperators, ruleCall.matchedOperators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planner, rule, matchedOperators);
    }

    @Override
    public String toString() {
        return "RuleCall{" +
                "rule=" + rule +
                ", matchedOperators=" + matchedOperators +
                '}';
    }
}
