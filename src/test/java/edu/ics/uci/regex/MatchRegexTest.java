package edu.ics.uci.regex;


import edu.ics.uci.regex.optimizer.operators.PhysicalMatchOperator;
import edu.ics.uci.regex.runtime.regexMatcher.MatchRegex;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Span;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MatchRegexTest {

    @Test
    public void testComputeMatchingResult() {

        List<String> fieldValueList = RegexTestConstantsText.getTuples();
        String fieldValue = fieldValueList.get(0);
        String query = RegexTestConstantsText.getQueries().get(0);
        Relation relation = Relation.create(fieldValue);


        MatchRegex matchRegex = new MatchRegex(PhysicalMatchOperator.create(query));

        Relation matchingResult = matchRegex.computeMatchingResult(Arrays.asList(relation));

        Relation expectedResults = Relation.create(fieldValue);
        expectedResults.addTuple(new Span(0, 10));

        assertEquals(expectedResults.tupleList.size(), matchingResult.tupleList.size());


    }
}
