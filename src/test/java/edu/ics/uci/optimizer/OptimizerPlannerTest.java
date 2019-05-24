package edu.ics.uci.optimizer;

import com.google.common.collect.Lists;
import edu.ics.uci.optimizer.TestOperator.*;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.ics.uci.optimizer.TestRules.dummyRule;
import static edu.ics.uci.optimizer.rule.PatternNode.*;
import static edu.ics.uci.optimizer.triat.ConventionDef.CONVENTION_TRAIT_DEF;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizerPlannerTest {

    /**
     * Constructs a chain of operators, with the first operator being leaf and last operator being root.
     * Operator1 -> Operator2 -> Operator3
     */
    public static SubsetNode constructSimpleChain(OptimizerPlanner planner, Operator... operators) {
        List<SubsetNode> inputs = new ArrayList<>();
        OperatorNode topOperator = null;

        for (Operator op : operators) {
            OperatorNode opNode = OperatorNode.create(op, planner.defaultTraitSet(), inputs);
            inputs = singletonList(SubsetNode.create(opNode));
            topOperator = opNode;
        }

        if (topOperator == null) {
            throw new RuntimeException("operator list is empty");
        }

        return SubsetNode.create(topOperator);
    }


    @Test
    public void testRegisterSingleNode() {

        OptimizerPlanner planner = OptimizerPlanner.create();
        planner.addTraitDef(CONVENTION_TRAIT_DEF);

        SubsetNode root = constructSimpleChain(planner, new OperatorA("a1"));

        planner.setRoot(root);

        assertEquals(1, planner.getSets().size());
        assertEquals(1, planner.getOperators().size());
        assertEquals(planner.getSets().keySet().iterator().next(),
                planner.getOperatorToSet().get(planner.getOperators().keySet().iterator().next()));

    }

    @Test
    public void testRegisterMultiNodes() {

        OptimizerPlanner planner = OptimizerPlanner.create();
        planner.addTraitDef(CONVENTION_TRAIT_DEF);

        SubsetNode root = constructSimpleChain(planner,
                new OperatorA("a1"), new OperatorB("b1"), new OperatorC("c1"));

        planner.setRoot(root);

        assertEquals(3, planner.getSets().size());
        assertEquals(3, planner.getOperators().size());


    }

    @Test
    public void testFireRuleOneOperator() {

        OptimizerPlanner planner = OptimizerPlanner.create();
        planner.addTraitDef(CONVENTION_TRAIT_DEF);

        SubsetNode root = constructSimpleChain(planner, new OperatorA("a1"));

        planner.addRule(dummyRule(any(OperatorA.class)));

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }

    @Test
    public void testFireRuleMultipleOperator() {

        OptimizerPlanner planner = OptimizerPlanner.create();
        planner.addTraitDef(CONVENTION_TRAIT_DEF);

        SubsetNode root = constructSimpleChain(planner,
                new OperatorA("a1"), new OperatorB("b1"), new OperatorC("c1"));

        planner.addRule(dummyRule(exact(OperatorB.class, any(OperatorA.class))));

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }


}
