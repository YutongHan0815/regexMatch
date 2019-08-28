package edu.ics.uci.optimizer.rule;


import edu.ics.uci.optimizer.OptimizerContext;
import edu.ics.uci.optimizer.OptimizerPlanner;
import edu.ics.uci.optimizer.operator.OperatorNode;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * A RuleCall is an invocation of a {@link TransformRule} with a set of {@link OperatorNode}s as arguments
 */
public class RuleCall implements Serializable {

    private final OptimizerPlanner planner;
    private final TransformRule rule;
    private final Map<Integer, Integer> matchedOperators;

    public RuleCall(OptimizerPlanner planner, TransformRule rule, Map<Integer, Integer> matchedOperators) {
        this.planner = planner;
        this.rule = rule;
        this.matchedOperators = matchedOperators;
    }

    public OptimizerContext getContext() {
        return this.planner.getContext();
    }

    /**
     * Returns the invoked rule
     * @return rule
     */
    public TransformRule getRule() {
        return rule;
    }

    /**
     * Return the matched {@link OperatorNode} at {@ordinal}
     * @param ordinal
     * @return the matched {@link OperatorNode}
     */
    public OperatorNode getOperator(int ordinal) {
        return this.planner.getAndOrTree().getOperator(matchedOperators.get(ordinal));
    }

    /**
     * Registers that a rule has produced an equivalent relational expression.
     * Called by the rule whenever it finds a match.
     *
     * @param equivalentOperator
     */
    public void transformTo(OperatorNode equivalentOperator) {
        int equivSetID = planner.getAndOrTree().getOperatorSet(matchedOperators.get(0)).getSetID();
        this.planner.registerOperator(equivalentOperator, equivSetID);
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
