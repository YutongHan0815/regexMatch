package edu.ics.uci.optimizer;

import edu.ics.uci.optimizer.TestOperator.*;
import edu.ics.uci.optimizer.operator.EquivSet;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.RuleSet;
import edu.ics.uci.regex.operators.*;
import edu.ics.uci.regex.rules.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static edu.ics.uci.optimizer.TestRules.dummyRule;
import static edu.ics.uci.optimizer.rule.PatternNode.*;
import static edu.ics.uci.optimizer.triat.ConventionDef.CONVENTION_TRAIT_DEF;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

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
        return SubsetNode.create(planner.getContext(),
                OperatorNode.create(planner.getContext(), operator, planner.defaultTraitSet(), emptyList()));
    }
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

        SubsetNode root = constructSimpleChain(planner, new OperatorA("a1"));

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
                new OperatorA("a1"), new OperatorB("b1"), new OperatorC("c1"));

        planner.setRoot(root);

        // TODO: assert the parent-children links are correct

        OperatorNode operatorNode1 = planner.getRoot().getOperators().iterator().next().getInputs().get(0).
                getOperators().iterator().next();
        assertEquals(new OperatorB("b1"), operatorNode1.getOperator());
        assertEquals(new OperatorA("a1"),
                operatorNode1.getInputs().get(0).getOperators().iterator().next().getOperator());
        assertEquals(3, planner.getAndOrTree().getSets().size());
        assertEquals(3, planner.getAndOrTree().getOperators().size());

    }


    /**
     * Test set root as operators with multiple inputs the planner.
     */
    @Test
    public void testRootMultiInputNodes() {

        SubsetNode subsetA = createLeafSubset(planner, new OperatorA("a1"));
        SubsetNode subsetB = createLeafSubset(planner, new OperatorB("b2"));

        OperatorNode operatorJ = OperatorNode.create(planner.getContext(), new OperatorTwoInput("j"), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorJ);
        planner.setRoot(root);

        // TODO: assert the parent-children links are correct
        assertEquals(new OperatorA("a1"),
                planner.getRoot().getOperators().iterator().next().getInputs().get(0).
                        getOperators().iterator().next().getOperator());
        assertEquals(new OperatorB("b2"),
                planner.getRoot().getOperators().iterator().next().getInputs().get(1).
                        getOperators().iterator().next().getOperator());

    }


    /**
     * Test register a new operator into a planner with existing root.
     */
    @Test
    public void testRegisterSingleNode() {
        SubsetNode root = constructSimpleChain(planner, new OperatorA("a1"));
        planner.setRoot(root);

        OperatorNode operatorNode = OperatorNode.create(planner.getContext(), new OperatorB("b1"), planner.defaultTraitSet());
        planner.registerOperator(operatorNode, 2);

        assertEquals(2, planner.getAndOrTree().getOperators().size());
        assertTrue(planner.getAndOrTree().getSets().get(1).getOperators().contains(operatorNode));



    }

    /**
     * Test register two new operators into one Subset in a planner with existing root.
     */
    @Test
    public void testRegisterMultiInputNode() {

        SubsetNode subsetA = createLeafSubset(planner, new OperatorA("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new OperatorB("b0"));

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeA = OperatorNode.create(planner.getContext(), new OperatorA("a1"),planner.defaultTraitSet());

        OperatorNode operatorNodeB = OperatorNode.create(planner.getContext(), new OperatorB("b1"),planner.defaultTraitSet());
        planner.registerOperator(operatorNodeA, 4);
        planner.registerOperator(operatorNodeB, 4);

        assertEquals(5, planner.getAndOrTree().getOperators().size());
        assertTrue(planner.getAndOrTree().getSets().get(0).getOperators().contains(operatorNodeA));
        assertTrue(planner.getAndOrTree().getSets().get(0).getOperators().contains(operatorNodeB));

    }

    /**
     * Test register a existing operator into a planner with a existing subset.
     */
    @Test
    public void testRegisterSingleExistingNode() {
        SubsetNode subsetA = createLeafSubset(planner, new OperatorA("a0"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new OperatorB("b"), planner.defaultTraitSet(), subsetA);
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeA = OperatorNode.create(planner.getContext(), new OperatorA("a0"),planner.defaultTraitSet());
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
        SubsetNode subsetA = createLeafSubset(planner, new OperatorA("a0"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new OperatorB("b"), planner.defaultTraitSet(), subsetA);
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);

        planner.setRoot(root);
        OperatorNode operatorNodeA = OperatorNode.create(planner.getContext(), new OperatorA("a0"),planner.defaultTraitSet());

        assertThrows(UnsupportedOperationException.class, ()->planner.registerOperator(operatorNodeA, 2),
                "TODO: set merge is not implemented yet");
    }

    /**
     *Test register a operator with a invalid SetID
     */
    @Test
    public void testRegisterWithInvalidSetID() {
        SubsetNode subsetA = createLeafSubset(planner, new OperatorA("a0"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new OperatorB("b"), planner.defaultTraitSet(), subsetA);
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeB = OperatorNode.create(planner.getContext(), new OperatorB("b0"),planner.defaultTraitSet());

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
        SubsetNode subset1 = createLeafSubset(planner, new OperatorA("a0"));
        SubsetNode subset2 = createLeafSubset(planner, new OperatorA("a1"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subset1, subset2));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeB0 = OperatorNode.create(planner.getContext(), new OperatorB("b0"), planner.defaultTraitSet());
        planner.registerOperator(operatorNodeB0, 4);

        OperatorNode operatorNodeB1 = OperatorNode.create(planner.getContext(), new OperatorB("b1"), planner.defaultTraitSet());
        planner.registerOperator(operatorNodeB1, 5);


        EquivSet equivSet = EquivSet.create(planner.getContext(), Arrays.asList(operatorNodeB0,operatorNodeB1));
        SubsetNode subset3 = SubsetNode.create(equivSet, planner.defaultTraitSet());

        OperatorNode operatorNodeC = OperatorNode.create(planner.getContext(), new OperatorB("c"), planner.defaultTraitSet(), subset3);
        //planner.registerOperator(operatorNodeC, 3);
        assertThrows(UnsupportedOperationException.class, ()->planner.registerOperator(operatorNodeC, 3),
                "TODO: a set equivalent to multiple other sets is not handled yet");
    }

    /**
     * Test register subset has one equivalent subSet
     */
    @Test
    public void testRegisterWithOneEquivalentSubSet() {
        SubsetNode subset1 = createLeafSubset(planner, new OperatorA("a0"));
        SubsetNode subset2 = createLeafSubset(planner, new OperatorB("b0"));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subset1, subset2));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        SubsetNode subset3 = createLeafSubset(planner, new OperatorA("a0"));

        OperatorNode operatorNodeC = OperatorNode.create(planner.getContext(), new OperatorB("c"), planner.defaultTraitSet(), subset3);
        planner.registerOperator(operatorNodeC, 3);

        assertEquals(1, subset1.getOperators().size());

    }

    @Test
    public void testRuleMatchSetRootOneOperator() {

        SubsetNode root = constructSimpleChain(planner, new OperatorA("a1"));

        planner.addRule(dummyRule(any(OperatorA.class)));

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }
    @Test
    public void testRuleMatchSetRootOneOperator1() {

        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("a1"));

        planner.addRule(LogicalMatchToPhysicalMatchRule.INSTANCE);

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }

    @Test
    public void testRuleMatchSetRootMultipleOperators() {

        SubsetNode root = constructSimpleChain(planner,
                new OperatorA("a1"), new OperatorB("b1"), new OperatorC("c1"));

        planner.addRule(dummyRule(exact(OperatorB.class, any(OperatorA.class))));

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }

    @Test
    public void testRuleMatchSetRootMultipleOperators1() {

        SubsetNode root = constructSimpleChain(planner,
                new LogicalMatchOperator("b1"),
                new LogicalVerifyOperator("a1", Condition.AFTER));

        planner.addRule(MatchVerifyToMatchVerifyRule.INSTANCE);

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());


    }
    @Test
    public void testJoinRuleMatchMultiInputNode() {
        SubsetNode subsetA = createLeafSubset(planner, new OperatorA("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new OperatorB("b0"));

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);

        planner.addRule(dummyRule(exact(OperatorTwoInput.class, Arrays.asList(any(OperatorA.class), any(OperatorB.class)))));

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }

    @Test
    public void testJoinCommutativeRuleMatchMultiInputNode1() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);

        planner.addRule(JoinCommutativeRule.INSTANCE);

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }
    @Test
    //TODO
    public void testNewChildrenPolicy() {
        SubsetNode root = constructSimpleChain(planner,
                new OperatorA("a1"), new OperatorB("b1"), new OperatorC("c1"));

        planner.addRule(dummyRule(leaf(OperatorB.class)));
        planner.setRoot(root);
        assertEquals(0, planner.getRuleCallQueue().size());
    }

    @Test
    public void testRuleCallLogicalToPhysicalRule() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalVerifyOperator("b1", Condition.AFTER));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));
        SubsetNode subsetC = createLeafSubset(planner,  new LogicalJoinOperator(Condition.AFTER));

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB, subsetC));

        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.addRule(LogicalMatchToPhysicalMatchRule.INSTANCE);
        planner.addRule(LogicalVerifyToPhysicalVerifyRule.INSTANCE);
        planner.addRule(LogicalJoinToPhysicalJoinRule.INSTANCE);

        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(8, planner.getAndOrTree().getOperators().size());
    }
    @Test
    public void testRuleCallSignalRule() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("[0-9]+PM"));

        planner.addRule(MatchToMatchVerifyRule.INSTANCE);
        planner.addRule(LogicalMatchToPhysicalMatchRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());
    }
    /**
     * Test RuleCall Match Rules
     */
    @Test
    public void testRuleCallMultiMatchRule() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("[0-9]+PM"));

        planner.addRule(MatchToMatchVerifyRule.INSTANCE);
        planner.addRule(MatchVerifyToMatchVerifyRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());

    }
    /**
     * Test RuleCall Verify Rules
     */
    @Test
    public void testRuleCallMultiVerifyRule() {
        SubsetNode root = constructSimpleChain(planner, new LogicalVerifyOperator("[0-9]+PM", Condition.AFTER));

        planner.addRule(VerifyToVerifySplitRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(3, planner.getAndOrTree().getOperators().size());

    }

    /**
     * Test RuleCall Join Rules
     */
    @Test
    public void testRuleCallMultiJoinRule() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("[0-9]+PM"));

        planner.addRule(MatchToJoinRule.INSTANCE);
        //planner.addRule(JoinCommutativeRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());
    }

    @Test
    public void testMatchVerifyToMatchVerifyRuleRuleMatch() {
        EquivSet meta = EquivSet.create(planner.getContext(), Arrays.asList(
                OperatorNode.create(planner.getContext(), new LogicalMatchOperator("a0"), planner.defaultTraitSet()),
                OperatorNode.create(planner.getContext(), new LogicalMatchOperator("b0"), planner.defaultTraitSet())
        ));
        SubsetNode matchA = SubsetNode.create(meta, planner.defaultTraitSet());

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), 
                new LogicalVerifyOperator("c1", Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(matchA));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.addRule(MatchVerifyToMatchVerifyRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());
    }
    @Test
    public void testMatchVerifyToMatchVerifyRuleRuleMatch1() {

        EquivSet meta = EquivSet.create(planner.getContext(), 
                OperatorNode.create(planner.getContext(), new LogicalMatchOperator("a0"), planner.defaultTraitSet())
        );
        SubsetNode matchA = SubsetNode.create(meta, planner.defaultTraitSet());

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), 
                new LogicalVerifyOperator("c1", Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(matchA));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.addRule(MatchVerifyToMatchVerifyRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(4, planner.getAndOrTree().getOperators().size());
    }
    @Test

    public void testJoinCommutativeRuleMatch() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalVerifyOperator("b1", Condition.AFTER));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);

        //planner.addRule(JoinCommutativeRule.INSTANCE);
        planner.addRule(LogicalMatchToPhysicalMatchRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());

    }

    @Test
    public void testVerifyToJoinRuleMatch() {
        SubsetNode root = constructSimpleChain(planner,
                new LogicalMatchOperator("a0"),
                new LogicalVerifyOperator("(b0)(c0)", Condition.AFTER));

        planner.addRule(VerifyToJoinRule.INSTANCE);
        //planner.addRule(JoinCommutativeRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());
    }

    @Test
    public void testMatchVerifyToReverseVerifyRuleMatch() {
        SubsetNode root = constructSimpleChain(planner,
                new LogicalMatchOperator("a0"),
                new LogicalVerifyOperator("[0-9]+PM", Condition.AFTER));

        planner.addRule(MatchVerifyToReverseVerifyRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(6, planner.getAndOrTree().getOperators().size());
    }
    @Test
    public void testMatchAscending() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("a1"),
                new LogicalVerifyOperator("b1", Condition.AFTER));

        planner.addRule(MatchVerifyToMatchVerifyRule.INSTANCE);
        planner.setRoot(root);

        OperatorNode operatorNodeA = OperatorNode.create(planner.getContext(), new LogicalMatchOperator("a2"), planner.defaultTraitSet());
        planner.registerOperator(operatorNodeA, 4);
        assertEquals(2, planner.getRuleCallQueue().size());

    }

    @Test
    public void testAllRuleMatch() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("[0-9]+PM(a|b)"));

        //RuleSet.DEFAULT_RULES.stream().forEach(transformRule -> planner.addRule(transformRule));
        planner.setRoot(root);
        planner.optimize();

        System.out.println(planner.getAndOrTree().getOperators().size());
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(3, planner.getAndOrTree().getSets().size());

        assertEquals(14, planner.getAndOrTree().getOperators().size());

    }





}
