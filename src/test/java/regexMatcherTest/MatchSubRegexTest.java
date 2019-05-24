package regexMatcherTest;


import edu.ics.uci.regex.regexMatcher.MatchSubRegex;
import edu.ics.uci.regex.regexMatcher.Span;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


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

        assertTrue(TestUtils.equals(exactedResult, expectedResults));

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

        assertTrue(TestUtils.equals(exactedResult, expectedResults));
    }
}
