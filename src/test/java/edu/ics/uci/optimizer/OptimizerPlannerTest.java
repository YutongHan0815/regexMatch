package edu.ics.uci.optimizer;

import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import java.util.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class OptimizerPlannerTest {



    /**
     * Constructs a chain of operators, with the first operator being leaf and last operator being root.
     * Operator1 -> Operator2 -> Operator3
     */
    public static SubsetNode constructSimpleChain(OptimizerPlanner planner, Operator... operators) {
        List<SubsetNode> inputs = new ArrayList<>();
        OperatorNode topOperator = null;

        for (Operator op : operators) {
            OperatorNode opNode = OperatorNode.create(planner.getContext(), op, planner.defaultTraitSet(), inputs);
            inputs = singletonList(SubsetNode.create(planner.getContext(), opNode));
            topOperator = opNode;
        }

        if (topOperator == null) {
            throw new RuntimeException("operator list is empty");
        }

        return SubsetNode.create(planner.getContext(), topOperator);
    }

    public static SubsetNode createLeafSubset(OptimizerPlanner planner, Operator operator) {
        OperatorNode operatorNode = OperatorNode.create(planner.getContext(), operator, planner.defaultTraitSet(), emptyList());
        return SubsetNode.create(planner.getContext(),operatorNode);
    }


}
