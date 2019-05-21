package regexMatcher;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


public class MatchSubRegex implements MatchRegex, Serializable {
    private boolean matchOr;
    private final String fieldValue;
    private final String regex;


    public MatchSubRegex(String subRegex, boolean matchOr, String fieldValue) {
        this.regex = subRegex;
        this.matchOr = matchOr;
        this.fieldValue = fieldValue;
    }

    // Match regex on a string from left to right
    public List<Span> computeMatchingResult() {
        List<Span> matchingResults = new ArrayList<>();

        Matcher javaMatcher = RegexMatchUtils.createMatcher(fieldValue, regex);
        while (javaMatcher.find()) {
            int start = javaMatcher.start();
            int end = javaMatcher.end();
            matchingResults.add(
                    new Span(start, end));
        }

        return matchingResults;
    }

    //Match reverse regex on a reverse string from right to left
    public List<Span> computeReverseMatchingResult() {
        List<Span> matchingResults = new ArrayList<>();

        String reverseFieldValue = RegexMatchUtils.getReverseFieldValue(fieldValue);
        String reverseRegex = RegexMatchUtils.getReverseRegex(regex);
        Matcher javaMatcher = RegexMatchUtils.createMatcher(reverseFieldValue, reverseRegex);

        while (javaMatcher.find()) {
            int start = javaMatcher.start();
            int end = javaMatcher.end();

            matchingResults.add(
                    new Span( fieldValue.length()-end, fieldValue.length()-start));
        }

        return matchingResults;
    }


}
