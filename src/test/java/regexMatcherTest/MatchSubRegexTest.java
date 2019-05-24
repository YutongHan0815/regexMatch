package regexMatcherTest;

import Utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import regexMatcher.MatchSubRegex;
import regexMatcher.Span;


import java.util.ArrayList;
import java.util.List;


public class MatchSubRegexTest {

    @Test
    public void testComputeMatchingResult() throws Exception{

        List<String> tuples = RegexTestConstantsText.getTuples();
        String fieldValue = tuples.get(0);
        String query = RegexTestConstantsText.getQueries().get(0);
        List<Span> expectedResults = new ArrayList<>();
        expectedResults.add(new Span(0,10));
        MatchSubRegex matchSubRegex = new MatchSubRegex(query, true, fieldValue);
        List<Span> exactedResult = matchSubRegex.computeMatchingResult();

        Assert.assertTrue(TestUtils.equals(exactedResult, expectedResults));

    }

    @Test
    public void testComputeReverseMatchingResult() throws Exception{
        List<String> tuples = RegexTestConstantsText.getTuples();
        String fieldValue = tuples.get(0);
        String query = RegexTestConstantsText.getQueries().get(0);
        List<Span> expectedResults = new ArrayList<>();
        expectedResults.add(new Span(0,10));
        MatchSubRegex matchSubRegex = new MatchSubRegex(query, true, fieldValue);
        List<Span> exactedResult = matchSubRegex.computeReverseMatchingResult();

        Assert.assertTrue(TestUtils.equals(exactedResult, expectedResults));
    }
}
