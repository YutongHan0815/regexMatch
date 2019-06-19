package edu.ics.uci.optimizer;

import edu.ics.uci.optimizer.operator.*;
import edu.ics.uci.regex.RegexTestConstantsText;
import edu.ics.uci.regex.optimizer.operators.Condition;

import edu.ics.uci.regex.optimizer.operators.PhysicalJoinOperator;
import edu.ics.uci.regex.optimizer.operators.PhysicalMatchOperator;
import edu.ics.uci.regex.runtime.regexMatcher.*;

import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Span;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static edu.ics.uci.optimizer.OptimizerPlannerTest.createLeafSubset;
import static edu.ics.uci.optimizer.triat.ConventionDef.CONVENTION_TRAIT_DEF;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryEngineTest {

    private OptimizerPlanner planner;

    @BeforeEach
    public void setUp() {
        this.planner = OptimizerPlanner.create();
        planner.addTraitDef(CONVENTION_TRAIT_DEF);
    }
    @Test
    public void searchWithMatchSinglePlan() {
        List<String> fieldValueList = RegexTestConstantsText.getTuples();
        String fieldValue = fieldValueList.get(1);

        SubsetNode root = createLeafSubset(planner, new PhysicalMatchOperator(new SubRegex("[0-9]+(?:st|nd|rd|th)\\s?Floor"), true));
        planner.setRoot(root);

        OperatorNode rootNode = planner.getAndOrTree().getOperator(1);
        RegexQueryEngine queryEngine = new RegexQueryEngine(rootNode);
        queryEngine.compile();
        Relation matchingResult = queryEngine.executeQuery(fieldValue);

        Relation expectedResults = Relation.create(fieldValue);
        expectedResults.addTuple(new Span(22, 32));

        assertEquals(expectedResults.tupleList.get(0).getRootNode().getSpan(), matchingResult.tupleList.get(0).getRootNode().getSpan());


    }

    @Test
    public void searchWithJoinSinglePlan() {
        List<String> fieldValueList = RegexTestConstantsText.getTuples();
        String fieldValue = fieldValueList.get(1);

        SubsetNode subsetA = createLeafSubset(planner, new PhysicalMatchOperator(new SubRegex("[0-9]+(?:st|nd|rd|th)"), true));
        SubsetNode subsetB = createLeafSubset(planner, new PhysicalMatchOperator(new SubRegex("\\s?Floor"), true));
        OperatorNode operatorRoot = OperatorNode.create(planner.getContext(), new PhysicalJoinOperator(Condition.AFTER), planner.defaultTraitSet(),
                Arrays.asList(subsetA, subsetB));
        SubsetNode root = SubsetNode.create(planner.getContext(), operatorRoot);
        planner.setRoot(root);

        OperatorNode rootNode = planner.getAndOrTree().getOperator(5);
        RegexQueryEngine queryEngine = new RegexQueryEngine(rootNode);
        queryEngine.compile();
        Relation matchingResult = queryEngine.executeQuery(fieldValue);

        Relation expectedResults = Relation.create(fieldValue);
        expectedResults.addTuple(new Span(22, 32));

        assertEquals(expectedResults.tupleList.get(0).getRootNode().getSpan(), matchingResult.tupleList.get(0).getRootNode().getSpan());


    }

}
