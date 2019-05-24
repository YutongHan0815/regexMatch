package regexMatcherTest;

import edu.ics.uci.regex.regexMatcher.Span;

import java.util.List;

public class TestUtils {
    public TestUtils() {

    }
    public static boolean equals(List<Span> expectedResults, List<Span> exactResults) {

        if (expectedResults.size() != exactResults.size()) {
            return false;
        } else {
            return expectedResults.containsAll(exactResults) && exactResults.containsAll(expectedResults);
        }

    }

}
