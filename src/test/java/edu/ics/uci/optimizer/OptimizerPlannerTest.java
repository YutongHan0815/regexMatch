package edu.ics.uci.optimizer;

import com.google.common.collect.ImmutableList;
import edu.ics.uci.optimizer.TestOperator.*;
import edu.ics.uci.optimizer.operator.MetaSet;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.ChildrenPolicy;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleSet;
import edu.ics.uci.regex.operators.*;
import edu.ics.uci.regex.rules.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.management.PlatformLoggingMXBean;
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
            OperatorNode opNode = OperatorNode.create(op, planner.defaultTraitSet(), inputs);
            inputs = singletonList(SubsetNode.create(opNode));
            topOperator = opNode;
        }

        if (topOperator == null) {
            throw new RuntimeException("operator list is empty");
        }

        return SubsetNode.create(topOperator);
    }

    public static SubsetNode createLeafSubset(OptimizerPlanner planner, Operator operator) {
        return SubsetNode.create(OperatorNode.create(operator, planner.defaultTraitSet(), emptyList()));
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
        assertEquals(planner.getAndOrTree().getSets().keySet().iterator().next(),
                planner.getAndOrTree().getOperatorSetID(planner.getAndOrTree().getOperators().values().iterator().next()));

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

        OperatorNode operatorJ = OperatorNode.create(new OperatorTwoInput("j"), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(operatorJ);
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

        OperatorNode operatorNode = OperatorNode.create(new OperatorB("b1"), planner.defaultTraitSet());
        planner.registerOperator(operatorNode, 0);

        assertEquals(2, planner.getAndOrTree().getOperators().size());
        assertTrue(planner.getAndOrTree().getSets().get(0).getOperators().contains(operatorNode));



    }

    /**
     * Test register two new operators into one Subset in a planner with existing root.
     */
    @Test
    public void testRegisterMultiInputNode() {

        SubsetNode subsetA = createLeafSubset(planner, new OperatorA("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new OperatorB("b0"));

        OperatorNode operatorRoot = OperatorNode.create(new OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeA = OperatorNode.create(new OperatorA("a1"),planner.defaultTraitSet());

        OperatorNode operatorNodeB = OperatorNode.create(new OperatorB("b1"),planner.defaultTraitSet());
        planner.registerOperator(operatorNodeA, 0);
        planner.registerOperator(operatorNodeB, 0);

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
        OperatorNode operatorRoot = OperatorNode.create(new OperatorB("b"), planner.defaultTraitSet(), subsetA);
        SubsetNode root = SubsetNode.create(operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeA = OperatorNode.create(new OperatorA("a0"),planner.defaultTraitSet());
        planner.registerOperator(operatorNodeA, 1);

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
        OperatorNode operatorRoot = OperatorNode.create(new OperatorB("b"), planner.defaultTraitSet(), subsetA);
        SubsetNode root = SubsetNode.create(operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeA = OperatorNode.create(new OperatorA("a0"),planner.defaultTraitSet());


        assertThrows(UnsupportedOperationException.class, ()->planner.registerOperator(operatorNodeA, 0),
                "TODO: set merge is not implemented yet");
    }

    /**
     *Test register a operator with a invalid SetID
     */
    @Test
    public void testRegisterWithInvalidSetID() {
        SubsetNode subsetA = createLeafSubset(planner, new OperatorA("a0"));
        OperatorNode operatorRoot = OperatorNode.create(new OperatorB("b"), planner.defaultTraitSet(), subsetA);
        SubsetNode root = SubsetNode.create(operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeB = OperatorNode.create(new OperatorB("b0"),planner.defaultTraitSet());

        assertThrows(Exception.class, ()->planner.registerOperator(operatorNodeB, 2), "");


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
        OperatorNode operatorRoot = OperatorNode.create(new OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subset1, subset2));
        SubsetNode root = SubsetNode.create(operatorRoot);
        planner.setRoot(root);

        OperatorNode operatorNodeB0 = OperatorNode.create(new OperatorB("b0"),planner.defaultTraitSet());
        planner.registerOperator(operatorNodeB0, 1);
        OperatorNode operatorNodeB1 = OperatorNode.create(new OperatorB("b1"),planner.defaultTraitSet());
        planner.registerOperator(operatorNodeB1, 2);


        MetaSet metaSet = MetaSet.create(Arrays.asList(operatorNodeB0,operatorNodeB1));
        SubsetNode subset3 = SubsetNode.create(metaSet, planner.defaultTraitSet());

        OperatorNode operatorNodeC = OperatorNode.create(new OperatorB("c"), planner.defaultTraitSet(), subset3);
        assertThrows(UnsupportedOperationException.class, ()->planner.registerOperator(operatorNodeC, 0),
                "TODO: a set equivalent to multiple other sets is not handled yet");
    }

    /**
     * Test register subset has one equivalent subSet
     */
    @Test
    public void testRegisterWithOneEquivalentSubSet() {
        SubsetNode subset1 = createLeafSubset(planner, new OperatorA("a0"));
        SubsetNode subset2 = createLeafSubset(planner, new OperatorB("b0"));
        OperatorNode operatorRoot = OperatorNode.create(new OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subset1, subset2));
        SubsetNode root = SubsetNode.create(operatorRoot);
        planner.setRoot(root);

        SubsetNode subset3 = createLeafSubset(planner, new OperatorA("a0"));

        OperatorNode operatorNodeC = OperatorNode.create(new OperatorB("c"), planner.defaultTraitSet(), subset3);
        planner.registerOperator(operatorNodeC, 0);

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

        OperatorNode operatorRoot = OperatorNode.create(new OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(operatorRoot);

        planner.addRule(dummyRule(exact(OperatorTwoInput.class, Arrays.asList(any(OperatorA.class), any(OperatorB.class)))));

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }

    @Test
    public void testJoinCommutativeRuleMatchMultiInputNode1() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));

        OperatorNode operatorRoot = OperatorNode.create(new LogicalJoinOperator(Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(operatorRoot);

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

        OperatorNode operatorRoot = OperatorNode.create(new LogicalJoinOperator(Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB, subsetC));

        SubsetNode root = SubsetNode.create(operatorRoot);
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
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(3, planner.getAndOrTree().getOperators().size());
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
        planner.addRule(VerifyToReverseVerifySplitRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());

    }

    /**
     * Test RuleCall Join Rules
     */
    @Test
    public void testRuleCallMultiJoinRule() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("[0-9]+PM"));

        planner.addRule(MatchToJoinRule.INSTANCE);
        planner.addRule(JoinCommutativeRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());
    }

    @Test
    public void testMatchVerifyToMatchVerifyRuleRuleMatch() {
        MetaSet meta = MetaSet.create(Arrays.asList(
                OperatorNode.create(new LogicalMatchOperator("a0"), planner.defaultTraitSet()),
                OperatorNode.create(new LogicalMatchOperator("b0"), planner.defaultTraitSet())
        ));
        SubsetNode matchA = SubsetNode.create(meta, planner.defaultTraitSet());

        OperatorNode operatorRoot = OperatorNode.create(
                new LogicalVerifyOperator("c1", Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(matchA));
        SubsetNode root = SubsetNode.create(operatorRoot);
        planner.addRule(MatchVerifyToMatchVerifyRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());
    }
    @Test
    public void testMatchVerifyToMatchVerifyRuleRuleMatch1() {

        MetaSet meta = MetaSet.create(
                OperatorNode.create(new LogicalMatchOperator("a0"), planner.defaultTraitSet())
        );
        SubsetNode matchA = SubsetNode.create(meta, planner.defaultTraitSet());

        OperatorNode operatorRoot = OperatorNode.create(
                new LogicalVerifyOperator("c1", Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(matchA));
        SubsetNode root = SubsetNode.create(operatorRoot);
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

        OperatorNode operatorRoot = OperatorNode.create(new LogicalJoinOperator(Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(operatorRoot);

        planner.addRule(JoinCommutativeRule.INSTANCE);
        planner.addRule(LogicalMatchToPhysicalMatchRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());

    }

    @Test
    public void testJoinCommutativeRuleMatch2() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("[0-9]+PM"));

        planner.addRule(JoinCommutativeRule.INSTANCE);
        planner.addRule(LogicalMatchToPhysicalMatchRule.INSTANCE);
        planner.addRule(MatchToJoinRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
    }
    @Test
    public void testMatchAscending() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("a1"), new LogicalVerifyOperator("b1", Condition.AFTER));

        planner.addRule(MatchVerifyToMatchVerifyRule.INSTANCE);
        planner.setRoot(root);

        OperatorNode operatorNodeA = OperatorNode.create(new LogicalMatchOperator("a2"), planner.defaultTraitSet());
        planner.registerOperator(operatorNodeA, 1);
        assertEquals(1, planner.getRuleCallQueue().size());

    }

    @Test
    public void testAllRuleMatch() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("[0-9]+PM"));

        RuleSet.DEFAULT_RULES.stream().forEach(transformRule -> planner.addRule(transformRule));
        planner.setRoot(root);
        planner.optimize();
        System.out.println(planner.getAndOrTree().getOperators().size());
        assertEquals(0, planner.getRuleCallQueue().size());
    }





}
