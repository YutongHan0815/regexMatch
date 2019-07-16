package edu.ics.uci.optimizer.Operator;

import edu.ics.uci.optimizer.OptimizerPlanner;
import edu.ics.uci.optimizer.TestOperator;
import edu.ics.uci.optimizer.operator.EquivSet;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static edu.ics.uci.optimizer.OptimizerPlannerTest.constructSimpleChain;
import static edu.ics.uci.optimizer.OptimizerPlannerTest.createLeafSubset;
import static edu.ics.uci.optimizer.triat.ConventionDef.CONVENTION_TRAIT_DEF;
import static org.junit.jupiter.api.Assertions.*;

public class AndOrTreeTest {

    private OptimizerPlanner planner;

    @BeforeEach
    public void setUp() {
        this.planner = OptimizerPlanner.create();
        planner.addTraitDef(CONVENTION_TRAIT_DEF);
    }

    /**
     * Test set root as a single node in the planner.
     */
    @Test
    public void testRootSingleNode() {

        SubsetNode root = constructSimpleChain(planner, new TestOperator.OperatorA("a1"));

        planner.setRoot(root);

        assertEquals(1, planner.getAndOrTree().getSets().size());
        assertEquals(1, planner.getAndOrTree().getOperators().size());
    }

    /**
     * Test set root as multiple operators in the planner.
     */
    @Test
    public void testRootMultipleNodes() {

        SubsetNode root = constructSimpleChain(planner,
                new TestOperator.OperatorA("a1"), new TestOperator.OperatorB("b1"), new TestOperator.OperatorC("c1"));

        planner.setRoot(root);

        // assert the parent-children links are correct

        OperatorNode operatorNode1 = planner.getRoot().getOperators().iterator().next().getInputs().get(0).
                getOperators().iterator().next();
        assertEquals(new TestOperator.OperatorB("b1"), operatorNode1.getOperator());
        assertEquals(new TestOperator.OperatorA("a1"),
                operatorNode1.getInputs().get(0).getOperators().iterator().next().getOperator());
        assertEquals(3, planner.getAndOrTree().getSets().size());
        assertEquals(3, planner.getAndOrTree().getOperators().size());

    }


    /**
     * Test set root as operators with multiple inputs the planner.
     */
    @Test
    public void testRootMultiInputNodes() {

        SubsetNode subsetA = createLeafSubset(planner, new TestOperator.OperatorA("a1"));
        SubsetNode subsetB = createLeafSubset(planner, new TestOperator.OperatorB("b2"));

        OperatorNode operatorJ = OperatorNode.create(planner.getContext(), new TestOperator.OperatorTwoInput("j"), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorJ);
        planner.setRoot(root);

        // assert the parent-children links are correct
        assertEquals(new TestOperator.OperatorA("a1"),
                planner.getRoot().getOperators().iterator().next().getInputs().get(0).
                        getOperators().iterator().next().getOperator());
        assertEquals(new TestOperator.OperatorB("b2"),
                planner.getRoot().getOperators().iterator().next().getInputs().get(1).
                        getOperators().iterator().next().getOperator());

    }


    /**
     * Test register a new operator into a planner with existing root. Node: the property of operator must be same.
     */
    @Test
    public void testRegisterSingleNode() {
        SubsetNode root = constructSimpleChain(planner, new TestOperator.OperatorA("a1"));
        planner.setRoot(root);

        OperatorNode operatorNode = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("a1"), planner.defaultTraitSet());
        planner.registerOperator(operatorNode, 2);
        assertEquals(2, planner.getAndOrTree().getOperators().size());
        assertTrue(planner.getAndOrTree().getSets().get(2).getOperators().contains(operatorNode));
        assertTrue(planner.getAndOrTree().getSet(2).getOperators(planner.defaultTraitSet()).contains(operatorNode));



    }

    /**
     * Test register two new operators into one Subset in a planner with existing root.
     */
    @Test
    public void testRegisterMultiInputNode() {

        SubsetNode subsetA = createLeafSubset(planner, new TestOperator.OperatorA("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new TestOperator.OperatorB("b0"));

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new TestOperator.OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeA = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("a0"),planner.defaultTraitSet());

        OperatorNode operatorNodeB = OperatorNode.create(planner.getContext(), new TestOperator.OperatorC("a0"),planner.defaultTraitSet());
        planner.registerOperator(operatorNodeA, 4);
        planner.registerOperator(operatorNodeB, 4);

        assertEquals(5, planner.getAndOrTree().getOperators().size());
        assertTrue(planner.getAndOrTree().getSets().get(4).getOperators().contains(operatorNodeA));
        // assertTrue(planner.getAndOrTree().getSets().get(4).getOperators().contains(operatorNodeB));

    }

    /**
     * Test register a existing operator into a planner with a existing subset.
     */
    @Test
    public void testRegisterSingleExistingNode() {
        SubsetNode subsetA = createLeafSubset(planner, new TestOperator.OperatorA("a0"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("b"), planner.defaultTraitSet(), subsetA);
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeA = OperatorNode.create(planner.getContext(), new TestOperator.OperatorA("a0"),planner.defaultTraitSet());
        planner.registerOperator(operatorNodeA, 3);
        assertEquals(2, planner.getAndOrTree().getOperators().size());
        assertEquals(1, subsetA.getOperators().size());
    }

    /**
     * Test register a existing operator into another existing subset
     * throws Exception TODO: set merge is not implemented yet
     */
    @Test
    public void testRegisterMultipleNodesWithExisting() {
        SubsetNode subsetA = createLeafSubset(planner, new TestOperator.OperatorA("a0"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("b"), planner.defaultTraitSet(), subsetA);
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);

        planner.setRoot(root);
        OperatorNode operatorNodeA = OperatorNode.create(planner.getContext(), new TestOperator.OperatorA("a0"),planner.defaultTraitSet());

        assertThrows(UnsupportedOperationException.class, ()->planner.registerOperator(operatorNodeA, 2),
                "TODO: set merge is not implemented yet");
    }

    /**
     *Test register a operator with a invalid SetID
     */
    @Test
    public void testRegisterWithInvalidSetID() {
        SubsetNode subsetA = createLeafSubset(planner, new TestOperator.OperatorA("a0"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("b"), planner.defaultTraitSet(), subsetA);
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeB = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("b0"),planner.defaultTraitSet());

        assertThrows(IllegalArgumentException.class, ()->planner.registerOperator(operatorNodeB, 6), "no set with ID: 6");


    }

    @Test
    public void testRegisterWithCycle() {


    }

    /**
     * Test register a operatorNode which the input set equivalent to multiple other sets
     * throws TODO: a set equivalent to multiple other sets is not handled yet
     */
    @Test
    public void testRegisterWithMultiEquivalentSubSet() {
        SubsetNode subset1 = createLeafSubset(planner, new TestOperator.OperatorA("a0"));
        SubsetNode subset2 = createLeafSubset(planner, new TestOperator.OperatorB("a0"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new TestOperator.OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subset1, subset2));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeB0 = OperatorNode.create(planner.getContext(), new TestOperator.OperatorA("a0"), planner.defaultTraitSet());
        planner.registerOperator(operatorNodeB0, 4);

        OperatorNode operatorNodeB1 = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("a0"), planner.defaultTraitSet());
        planner.registerOperator(operatorNodeB1, 5);


        EquivSet equivSet = EquivSet.create(planner.getContext(), Arrays.asList(operatorNodeB0, operatorNodeB1));
        SubsetNode subset3 = SubsetNode.create(equivSet, planner.defaultTraitSet());

        OperatorNode operatorNodeC = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("r"), planner.defaultTraitSet(), subset3);
        assertThrows(UnsupportedOperationException.class, ()->planner.registerOperator(operatorNodeC, 3),
                "TODO: a set equivalent to multiple other sets is not handled yet");
    }

    /**
     * Test register subset has one equivalent subSet
     */
    @Test
    public void testRegisterWithOneEquivalentSubSet() {
        SubsetNode subset1 = createLeafSubset(planner, new TestOperator.OperatorA("a0"));
        SubsetNode subset2 = createLeafSubset(planner, new TestOperator.OperatorB("b0"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new TestOperator.OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subset1, subset2));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        SubsetNode subset3 = createLeafSubset(planner, new TestOperator.OperatorA("a0"));
        OperatorNode operatorNodeC = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("r"), planner.defaultTraitSet(), subset3);
        planner.registerOperator(operatorNodeC, 3);

        assertEquals(3, planner.getAndOrTree().getSets().size());

    }
}
