package edu.ics.uci.optimizer.rule;

import edu.ics.uci.optimizer.OptimizerPlanner;
import edu.ics.uci.optimizer.TestOperator;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.regex.optimizer.expression.ComparisonExpr;
import edu.ics.uci.regex.optimizer.expression.SpanInputRef;
import edu.ics.uci.regex.optimizer.operators.LogicalJoinOperator;
import edu.ics.uci.regex.optimizer.operators.LogicalMatchOperator;
import edu.ics.uci.regex.optimizer.rules.logical.JoinCommutativeRule;
import edu.ics.uci.regex.optimizer.rules.logical.MatchToJoinRule;
import edu.ics.uci.regex.optimizer.rules.physical.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static edu.ics.uci.optimizer.OptimizerPlannerTest.constructSimpleChain;
import static edu.ics.uci.optimizer.OptimizerPlannerTest.createLeafSubset;
import static edu.ics.uci.optimizer.TestRules.dummyRule;
import static edu.ics.uci.optimizer.rule.PatternNode.*;
import static edu.ics.uci.optimizer.triat.ConventionDef.CONVENTION_TRAIT_DEF;
import static edu.ics.uci.regex.optimizer.expression.ComparisonExpr.ComparisionType.EQ;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuleMatchTest {

    private OptimizerPlanner planner;

    @BeforeEach
    public void setUp() {
        this.planner = OptimizerPlanner.create();
        planner.addTraitDef(CONVENTION_TRAIT_DEF);
    }

    @Test
    public void testRuleMatchSetRootOneOperator() {

        SubsetNode root = constructSimpleChain(planner, new TestOperator.OperatorA("a1"));

        planner.addRule(dummyRule(operand(TestOperator.OperatorA.class).children(any()).build()));

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
                new TestOperator.OperatorA("a1"), new TestOperator.OperatorB("b1"), new TestOperator.OperatorC("c1"));

        planner.addRule(dummyRule(operand(TestOperator.OperatorB.class)
                .children(exact(Arrays.asList(operand(TestOperator.OperatorA.class).children(any())))).
                        build()));
        //exact(OperatorB.class, any(OperatorA.class))));

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }

    @Test
    public void testRuleMatchSetRootMultipleOperators1() {

        SubsetNode root = constructSimpleChain(planner,
                new LogicalMatchOperator("ab(c|d)"));
        planner.addRule(MatchToJoinRule.INSTANCE);
        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());


    }
    @Test
    public void testJoinRuleMatchMultiInputNode() {
        SubsetNode subsetA = createLeafSubset(planner, new TestOperator.OperatorA("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new TestOperator.OperatorB("b0"));

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new TestOperator.OperatorTwoInput("r"), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);

        planner.addRule(dummyRule(operand(TestOperator.OperatorTwoInput.class)
                .children(exact(Arrays.asList(operand(TestOperator.OperatorA.class).children(any()),
                        operand(TestOperator.OperatorB.class).children(any()))))
                .build()));
        //exact(OperatorTwoInput.class, Arrays.asList(any(OperatorA.class), any(OperatorB.class)))));

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }

    @Test
    public void testJoinCommutativeRuleMatchMultiInputNode1() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.END), SpanInputRef.of(1, SpanInputRef.SpanAccess.START))),
                planner.defaultTraitSet(), Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);

        planner.addRule(JoinCommutativeRule.INSTANCE);

        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());

    }
    @Test
    //TODO
    public void testNewChildrenPolicy() {
        SubsetNode root = constructSimpleChain(planner,
                new TestOperator.OperatorA("a1"), new TestOperator.OperatorB("b1"), new TestOperator.OperatorC("c1"));

        planner.addRule(dummyRule(operand(TestOperator.OperatorB.class).children(any()).build()));
        planner.setRoot(root);
        assertEquals(1, planner.getRuleCallQueue().size());
    }

    @Test

    public void testMatchAscending() {
        SubsetNode root = constructSimpleChain(planner, new TestOperator.OperatorB("b0"), new TestOperator.OperatorA("a0"));
        planner.addRule(dummyRule(operand(TestOperator.OperatorA.class)
                .children(exact(Arrays.asList(operand(TestOperator.OperatorB.class).children(none()))))
                .build()));
        planner.setRoot(root);

        OperatorNode operatorNodeB = OperatorNode.create(planner.getContext(), new TestOperator.OperatorB("b0"), planner.defaultTraitSet());
        planner.registerOperator(operatorNodeB, 4);
        assertEquals(1, planner.getRuleCallQueue().size());

    }

    @Test
    public void testMatchAnyOperatorClass() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("[0-9]+PM(a|b)"));

        TransformRule matchAnyRule = dummyRule(operand(Operator.class).children(any()).build());

        planner.addRule(matchAnyRule);
        planner.setRoot(root);

        assertEquals(1, planner.getRuleCallQueue().size());
    }


}
