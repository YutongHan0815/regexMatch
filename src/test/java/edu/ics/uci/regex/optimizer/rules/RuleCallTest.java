package edu.ics.uci.regex.optimizer.rules;

import edu.ics.uci.optimizer.OptimizerPlanner;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.operator.schema.Field;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.optimizer.operator.schema.SpanType;
import edu.ics.uci.optimizer.rule.RuleSet;
import edu.ics.uci.regex.optimizer.expression.BooleanExpr;
import edu.ics.uci.regex.optimizer.expression.ComparisonExpr;
import edu.ics.uci.regex.optimizer.expression.SpanInputRef;
import edu.ics.uci.regex.optimizer.operators.LogicalJoinOperator;
import edu.ics.uci.regex.optimizer.operators.LogicalMatchOperator;
import edu.ics.uci.regex.optimizer.operators.LogicalProjectOperator;
import edu.ics.uci.regex.optimizer.rules.logical.JoinAssociativeRule;
import edu.ics.uci.regex.optimizer.rules.logical.JoinCommutativeRule;
import edu.ics.uci.regex.optimizer.rules.logical.MatchToJoinRule;
import edu.ics.uci.regex.optimizer.rules.logical.ProjectJoinTransposeRule;
import edu.ics.uci.regex.optimizer.rules.physical.*;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Map;


import static edu.ics.uci.optimizer.OptimizerPlannerTest.constructSimpleChain;
import static edu.ics.uci.optimizer.OptimizerPlannerTest.createLeafSubset;
import static edu.ics.uci.optimizer.triat.ConventionDef.CONVENTION_TRAIT_DEF;
import static edu.ics.uci.regex.optimizer.expression.ComparisonExpr.ComparisionType.EQ;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuleCallTest {

    private OptimizerPlanner planner;

    @BeforeEach
    public void setUp() {
        this.planner = OptimizerPlanner.create();
        planner.addTraitDef(CONVENTION_TRAIT_DEF);
    }
    /**
     * Test RuleCall Logical to Physical Rules for all the Operators: Match, Join and Project
     */
    @Test
    public void testRuleCallLogicalToPhysicalRule() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));

        OperatorNode joinOperator1 = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.END), SpanInputRef.of(1, SpanInputRef.SpanAccess.START)
                )), planner.defaultTraitSet(),
                subsetA, subsetB);

        SubsetNode subsetD = SubsetNode.create(planner.getContext(), joinOperator1);

        OperatorNode projectOperator = OperatorNode.create(planner.getContext(),
                new LogicalProjectOperator(0, 1, 1), planner.defaultTraitSet(), subsetD);


        SubsetNode root = SubsetNode.create(planner.getContext(), projectOperator);

        planner.addRule(LogicalMatchToPhysicalMatchRule.INSTANCE);
        planner.addRule(LogicalJoinToPhysicalJoinRule.INSTANCE);
        planner.addRule(LogicalJoinToPhysicalVerifyRule.INSTANCE);
        planner.addRule(LogicalMatchToPhysicalMatchReverseRule.INSTANCE);
        planner.addRule(LogicalJoinToPhysicalVerifyReverseRule.INSTANCE);
        planner.addRule(LogicalProjectToPhysicalProjectRule.INSTANCE);

        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(12, planner.getAndOrTree().getOperators().size());
    }

    /**
     * Test RuleCall MatchToJoinRule Rules
     */
    @Test
    public void testMatchToJoinRule() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator(new SubRegex("[0-9]+PM")));

        planner.addRule(MatchToJoinRule.INSTANCE);

        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getOperators().size());

    }
    /**
     * Test RuleCall JoinCommutativeRule Rules
     */
    @Test
    public void testJoinCommutativeRule() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));

        OperatorNode operatorNode = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.END), SpanInputRef.of(1, SpanInputRef.SpanAccess.START)
                )), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorNode);

        planner.addRule(JoinCommutativeRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(4, planner.getAndOrTree().getOperators().size());

    }

    /**
     * Test RuleCall JoinCommutativeRule Rules WITH Boolean Expression in condition
     */
    @Test
    public void testJoinCommutativeRule2() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));
        SubsetNode subsetC = createLeafSubset(planner, new LogicalMatchOperator("c0"));

        OperatorNode logicalJoinACOpN = OperatorNode.create(planner.getContext(),
                new LogicalJoinOperator(
                        ComparisonExpr.of(ComparisonExpr.ComparisionType.GE,
                                SpanInputRef.of(0, SpanInputRef.SpanAccess.END), SpanInputRef.of(1, SpanInputRef.SpanAccess.START)
                        )), planner.defaultTraitSet(), Arrays.asList(subsetA, subsetC));
        SubsetNode subsetNodeJoin = SubsetNode.create(planner.getContext(), logicalJoinACOpN);


        OperatorNode logicalJoinEqualOpN = OperatorNode.create(planner.getContext(),
                new LogicalJoinOperator(
                        BooleanExpr.of(BooleanExpr.BooleanType.AND,
                                Arrays.asList(
                                        ComparisonExpr.of(ComparisonExpr.ComparisionType.EQ, SpanInputRef.of(0, SpanInputRef.SpanAccess.END), SpanInputRef.of(2, SpanInputRef.SpanAccess.START)),
                                        ComparisonExpr.of(ComparisonExpr.ComparisionType.EQ, SpanInputRef.of(2, SpanInputRef.SpanAccess.END), SpanInputRef.of(1, SpanInputRef.SpanAccess.START))
                                ))),
                planner.defaultTraitSet(), Arrays.asList(subsetNodeJoin, subsetB));

        SubsetNode root = SubsetNode.create(planner.getContext(), logicalJoinEqualOpN);

        planner.addRule(JoinCommutativeRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(8, planner.getAndOrTree().getOperators().size());

    }


    @Test
    public void testJoinAssociativeRuleMatch() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));
        SubsetNode subsetC = createLeafSubset(planner, new LogicalMatchOperator("c0"));

        OperatorNode operatorNode = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.START), SpanInputRef.of(1, SpanInputRef.SpanAccess.END)
                )), planner.defaultTraitSet(),
                Arrays.asList(subsetC, subsetB));
        SubsetNode subsetNode = SubsetNode.create(planner.getContext(), operatorNode);

        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.END), SpanInputRef.of(1, SpanInputRef.SpanAccess.START)
                )), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetNode));

        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.addRule(JoinAssociativeRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(7, planner.getAndOrTree().getOperators().size());

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
        assertEquals(6, planner.getAndOrTree().getOperators().size());
    }

    @Test
    public void testLogicalJoinToPhysicalVerifyRuleMatching() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));

        OperatorNode joinOperator = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.START), SpanInputRef.of(1, SpanInputRef.SpanAccess.END)
                )), planner.defaultTraitSet(),
                subsetA, subsetB);
        SubsetNode root = SubsetNode.create(planner.getContext(), joinOperator);
        planner.addRule(LogicalJoinToPhysicalVerifyRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(4, planner.getAndOrTree().getOperators().size());

    }

    @Test
    public void testLeftInputMapping() {
        int oldStartIndex = 0;
        int newStartIndex = 0;

        LogicalProjectOperator project = new LogicalProjectOperator(1, 3, 3);
        RowType projectInputRowType = RowType.of(
                Field.of("A", SpanType.SPAN_TYPE),
                Field.of("B", SpanType.SPAN_TYPE),
                Field.of("C", SpanType.SPAN_TYPE),
                Field.of("D", SpanType.SPAN_TYPE),
                Field.of("E", SpanType.SPAN_TYPE)
        );
        RowType projectOutputRowType = RowType.of(
                Field.of("A", SpanType.SPAN_TYPE),
                Field.of("C", SpanType.SPAN_TYPE),
                Field.of("E", SpanType.SPAN_TYPE),
                Field.of("BD", SpanType.SPAN_TYPE)

        );

        Map<SpanInputRef, SpanInputRef> inputMapping = ProjectJoinTransposeRule.getInputMapping(
                oldStartIndex, newStartIndex, project, projectInputRowType, projectOutputRowType);

        assertEquals(inputMapping.get(SpanInputRef.of(3, SpanInputRef.SpanAccess.START)), SpanInputRef.of(1, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(3, SpanInputRef.SpanAccess.END)), SpanInputRef.of(3, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(0, SpanInputRef.SpanAccess.START)), SpanInputRef.of(0, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(0, SpanInputRef.SpanAccess.END)), SpanInputRef.of(0, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(1, SpanInputRef.SpanAccess.START)), SpanInputRef.of(2, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(1, SpanInputRef.SpanAccess.END)), SpanInputRef.of(2, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(2, SpanInputRef.SpanAccess.START)), SpanInputRef.of(4, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(2, SpanInputRef.SpanAccess.END)), SpanInputRef.of(4, SpanInputRef.SpanAccess.END));

    }

    @Test
    public void testRightInputMapping() {
        int oldStartIndex = 4;
        int newStartIndex = 5;

        LogicalProjectOperator project = new LogicalProjectOperator(1, 3, 3);
        RowType projectInputRowType = RowType.of(
                Field.of("A", SpanType.SPAN_TYPE),
                Field.of("B", SpanType.SPAN_TYPE),
                Field.of("C", SpanType.SPAN_TYPE),
                Field.of("D", SpanType.SPAN_TYPE),
                Field.of("E", SpanType.SPAN_TYPE)
        );
        RowType projectOutputRowType = RowType.of(
                Field.of("A", SpanType.SPAN_TYPE),
                Field.of("C", SpanType.SPAN_TYPE),
                Field.of("E", SpanType.SPAN_TYPE),
                Field.of("BD", SpanType.SPAN_TYPE)

        );

        Map<SpanInputRef, SpanInputRef> inputMapping = ProjectJoinTransposeRule.getInputMapping(
                oldStartIndex, newStartIndex, project, projectInputRowType, projectOutputRowType);

        assertEquals(inputMapping.get(SpanInputRef.of(4, SpanInputRef.SpanAccess.START)), SpanInputRef.of(5, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(4, SpanInputRef.SpanAccess.END)), SpanInputRef.of(5, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(5, SpanInputRef.SpanAccess.START)), SpanInputRef.of(7, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(5, SpanInputRef.SpanAccess.END)), SpanInputRef.of(7, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(6, SpanInputRef.SpanAccess.START)), SpanInputRef.of(9, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(6, SpanInputRef.SpanAccess.END)), SpanInputRef.of(9, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(7, SpanInputRef.SpanAccess.START)), SpanInputRef.of(6, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(7, SpanInputRef.SpanAccess.END)), SpanInputRef.of(8, SpanInputRef.SpanAccess.END));

    }
    /**
     * Test case resultIndex < leftIndex
     */
    @Test
    public void testResultInputMapping1() {
        int oldStartIndex = 0;
        int newStartIndex = 0;

        LogicalProjectOperator project = new LogicalProjectOperator(2, 4, 1);
        RowType projectInputRowType = RowType.of(
                Field.of("A", SpanType.SPAN_TYPE),
                Field.of("B", SpanType.SPAN_TYPE),
                Field.of("C", SpanType.SPAN_TYPE),
                Field.of("D", SpanType.SPAN_TYPE),
                Field.of("E", SpanType.SPAN_TYPE)
        );
        RowType projectOutputRowType = RowType.of(
                Field.of("A", SpanType.SPAN_TYPE),
                Field.of("C", SpanType.SPAN_TYPE),
                Field.of("BE", SpanType.SPAN_TYPE),
                Field.of("D", SpanType.SPAN_TYPE)

        );

        Map<SpanInputRef, SpanInputRef> inputMapping = ProjectJoinTransposeRule.getInputMapping(
                oldStartIndex, newStartIndex, project, projectInputRowType, projectOutputRowType);


        assertEquals(inputMapping.get(SpanInputRef.of(0, SpanInputRef.SpanAccess.START)), SpanInputRef.of(0, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(0, SpanInputRef.SpanAccess.END)), SpanInputRef.of(0, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(1, SpanInputRef.SpanAccess.START)), SpanInputRef.of(2, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(1, SpanInputRef.SpanAccess.END)), SpanInputRef.of(4, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(2, SpanInputRef.SpanAccess.START)), SpanInputRef.of(1, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(2, SpanInputRef.SpanAccess.END)), SpanInputRef.of(1, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(3, SpanInputRef.SpanAccess.START)), SpanInputRef.of(3, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(3, SpanInputRef.SpanAccess.END)), SpanInputRef.of(3, SpanInputRef.SpanAccess.END));

    }

    /**
     * Test case leftIndex < resultIndex < rightIndex
     */
    @Test
    public void testResultInputMapping2() {
        int oldStartIndex = 0;
        int newStartIndex = 0;

        LogicalProjectOperator project = new LogicalProjectOperator(2, 4, 3);
        RowType projectInputRowType = RowType.of(
                Field.of("A", SpanType.SPAN_TYPE),
                Field.of("B", SpanType.SPAN_TYPE),
                Field.of("C", SpanType.SPAN_TYPE),
                Field.of("D", SpanType.SPAN_TYPE),
                Field.of("E", SpanType.SPAN_TYPE)
        );
        RowType projectOutputRowType = RowType.of(
                Field.of("A", SpanType.SPAN_TYPE),
                Field.of("B", SpanType.SPAN_TYPE),
                Field.of("D", SpanType.SPAN_TYPE),
                Field.of("BE", SpanType.SPAN_TYPE)

        );

        Map<SpanInputRef, SpanInputRef> inputMapping = ProjectJoinTransposeRule.getInputMapping(
                oldStartIndex, newStartIndex, project, projectInputRowType, projectOutputRowType);


        assertEquals(inputMapping.get(SpanInputRef.of(0, SpanInputRef.SpanAccess.START)), SpanInputRef.of(0, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(0, SpanInputRef.SpanAccess.END)), SpanInputRef.of(0, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(1, SpanInputRef.SpanAccess.START)), SpanInputRef.of(1, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(1, SpanInputRef.SpanAccess.END)), SpanInputRef.of(1, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(2, SpanInputRef.SpanAccess.START)), SpanInputRef.of(3, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(2, SpanInputRef.SpanAccess.END)), SpanInputRef.of(3, SpanInputRef.SpanAccess.END));
        assertEquals(inputMapping.get(SpanInputRef.of(3, SpanInputRef.SpanAccess.START)), SpanInputRef.of(2, SpanInputRef.SpanAccess.START));
        assertEquals(inputMapping.get(SpanInputRef.of(3, SpanInputRef.SpanAccess.END)), SpanInputRef.of(4, SpanInputRef.SpanAccess.END));

    }

    @Test
    public void testProjectJoinTransposeRuleMatching() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));
        SubsetNode subsetC = createLeafSubset(planner, new LogicalMatchOperator("c0"));

        OperatorNode joinOperator1 = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.END), SpanInputRef.of(1, SpanInputRef.SpanAccess.START)
                )), planner.defaultTraitSet(),
                subsetA, subsetB);

        SubsetNode subsetD = SubsetNode.create(planner.getContext(), joinOperator1);

        OperatorNode projectOperator = OperatorNode.create(planner.getContext(),
                new LogicalProjectOperator(0, 1, 0), planner.defaultTraitSet(), subsetD);
        SubsetNode subsetPro = SubsetNode.create(planner.getContext(), projectOperator);

        OperatorNode joinOperator2 = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.END), SpanInputRef.of(1, SpanInputRef.SpanAccess.START)
        )), planner.defaultTraitSet(), subsetPro, subsetC);

        SubsetNode root = SubsetNode.create(planner.getContext(), joinOperator2);
        planner.addRule(ProjectJoinTransposeRule.LEFT_PROJECT);
        //planner.addRule(ProjectJoinTransposeRule.BOTH_PROJECT);
        // planner.addRule(ProjectJoinTransposeRule.RIGHT_PROJECT);

        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());

        //assertEquals(11, planner.getAndOrTree().getSet(2).getOperators().size());

    }

    @Test
    public void testJoinAssociativeRuleMatching() {
        SubsetNode subsetA = createLeafSubset(planner, new LogicalMatchOperator("a0"));
        SubsetNode subsetB = createLeafSubset(planner, new LogicalMatchOperator("b0"));
        SubsetNode subsetC = createLeafSubset(planner, new LogicalMatchOperator("c0"));

        OperatorNode joinOperator1 = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.START), SpanInputRef.of(1, SpanInputRef.SpanAccess.END)
                )), planner.defaultTraitSet(),
                subsetC, subsetB);

        SubsetNode subsetD = SubsetNode.create(planner.getContext(), joinOperator1);

        OperatorNode projectOperator1 = OperatorNode.create(planner.getContext(),
                new LogicalProjectOperator(0, 1, 0), planner.defaultTraitSet(), subsetD);
        SubsetNode subsetPro1 = SubsetNode.create(planner.getContext(), projectOperator1);

        OperatorNode joinOperator2 = OperatorNode.create(planner.getContext(), new LogicalJoinOperator(ComparisonExpr.of(EQ,
                SpanInputRef.of(0, SpanInputRef.SpanAccess.END), SpanInputRef.of(1, SpanInputRef.SpanAccess.START)
        )), planner.defaultTraitSet(), subsetA, subsetPro1);

        OperatorNode projectOperator2 = OperatorNode.create(planner.getContext(),
                new LogicalProjectOperator(0, 1, 0), planner.defaultTraitSet(),
                SubsetNode.create(planner.getContext(), joinOperator2));

        SubsetNode root = SubsetNode.create(planner.getContext(), projectOperator2);
        planner.addRule(ProjectJoinTransposeRule.LEFT_PROJECT);
        planner.addRule(ProjectJoinTransposeRule.BOTH_PROJECT);
        planner.addRule(ProjectJoinTransposeRule.RIGHT_PROJECT);
        planner.addRule(JoinAssociativeRule.INSTANCE);

        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());

        assertEquals(11, planner.getAndOrTree().getOperators().size());

    }

    @Test
    public void testRuleCallMatchProjectJoinRule() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("[0-9]+PM(a|b)"));

        planner.addRule(MatchToJoinRule.INSTANCE);
        planner.addRule(ProjectJoinTransposeRule.RIGHT_PROJECT);
        planner.addRule(ProjectJoinTransposeRule.BOTH_PROJECT);
        planner.addRule(ProjectJoinTransposeRule.LEFT_PROJECT);
        planner.addRule(JoinAssociativeRule.INSTANCE);
        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(11, planner.getAndOrTree().getOperators().size());

    }


    @Test
    public void testAllRuleMatch() {
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("(a)(b)"));

        RuleSet.DEFAULT_RULES.stream().forEach(transformRule -> planner.addRule(transformRule));

        planner.setRoot(root);
        planner.optimize();
        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(5, planner.getAndOrTree().getSet(2).getOperators().size());
        assertEquals(4, planner.getAndOrTree().getSets().size());
        assertEquals(19, planner.getAndOrTree().getOperators().size());

    }


    @Test
    public void testAllRuleMatch1() {

       // SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("(a)(a)(a)"));
       // SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("(a)(b)(c)"));
        SubsetNode root = constructSimpleChain(planner, new LogicalMatchOperator("(a)(a)(b)"));

        RuleSet.LOGICAL_RULES.stream().forEach(transformRule -> planner.addRule(transformRule));


        planner.setRoot(root);
        planner.optimize();

        assertEquals(0, planner.getRuleCallQueue().size());
        assertEquals(2, planner.getAndOrTree().getSet(2).getOperators().size());
        assertEquals(9, planner.getAndOrTree().getSets().size());
        assertEquals(19, planner.getAndOrTree().getOperators().size());

    }


}
