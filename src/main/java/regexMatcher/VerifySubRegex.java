package regexMatcher;

import operators.VerifyCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class VerifySubRegex implements MatchRegex, Serializable {
    private final VerifyCondition condition;
    private final boolean verifyOr;
    private final String fieldValue;
    private final String regex;

    public VerifySubRegex(VerifyCondition condition, boolean verifyOr, String fieldValue, String regex) {
        this.condition = condition;
        this.verifyOr = verifyOr;
        this.fieldValue = fieldValue;
        this.regex = regex;
    }

    public VerifyCondition getCondition() {
        return condition;
    }

    public boolean isVerifyOr() {
        return verifyOr;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public String getRegex() {
        return regex;
    }


    // VerifyCondition: AFTER verifyOr = true
    public List<Span> verifyResultsAFTER(List<Span> spanList) {

        List<Span> matchingResults = new ArrayList<>();
        Matcher javaMatcher = RegexMatchUtils.createMatcher(fieldValue, regex);

        for (Span span : spanList) {
            int end = span.getEnd();

            if (javaMatcher.find(end) && javaMatcher.start() == end) {
                end = javaMatcher.end();
                matchingResults.add(new Span(span.getStart(), end));
            }
        }
        return matchingResults;

    }

    // VerifyCondition: AFTER verifyOr = false
    public List<Span> verifyResultsAFTEROr(List<Span> spanList) {
        List<Span> matchingResults = new ArrayList<>();
        String reverseFieldValue = RegexMatchUtils.getReverseFieldValue(fieldValue);
        String reverseRegex = RegexMatchUtils.getReverseRegex(regex);
        Matcher javaMatcher = RegexMatchUtils.createMatcher(reverseFieldValue, reverseRegex);

        for(Span span : spanList) {
            while(javaMatcher.find()) {
                if(fieldValue.length() - javaMatcher.end() == span.getStart())
                    matchingResults.add(new Span(span.getStart(), fieldValue.length() - javaMatcher.start()));
            }
        }
        return matchingResults;
    }

    //VerifyCondition: BEFORE verifyOr = false
    public List<Span> verifyResultsBEFORE(List<Span> spanList) {
        List<Span> matchingResults = new ArrayList<>();
        String reverseFieldValue = RegexMatchUtils.getReverseFieldValue(fieldValue);
        String reverseRegex = RegexMatchUtils.getReverseRegex(regex);
        Matcher javaMatcher = RegexMatchUtils.createMatcher(reverseFieldValue, reverseRegex);

        for (Span span : spanList) {
            int start = span.getStart();
            if (javaMatcher.find(fieldValue.length()-start) && javaMatcher.start() == start) {
                start = javaMatcher.end();
                matchingResults.add(new Span(start, span.getEnd()));
            }
        }
        return matchingResults;

    }

    //VerifyCondition: BEFORE verifyOr = true
    public  List<Span> verifyResultsBEFOREOr(List<Span> spanList) {
        List<Span> matchingResults = new ArrayList<>();

        Matcher javaMatcher = RegexMatchUtils.createMatcher(fieldValue, regex);

        for (Span span : spanList) {
            int start = span.getStart();
            while (javaMatcher.find() && javaMatcher.end() == start) {
                matchingResults.add(new Span(javaMatcher.start(), span.getEnd()));
            }
        }
        return matchingResults;

    }
    //VerifyCondition: EQUAL verifyOr = true
    public List<Span> verifyResultsEQUAL(List<Span> spanList) {

        List<Span> matchingResults = new ArrayList<>();

        for (Span span : spanList) {
            String subfield = fieldValue.substring(span.getStart(), span.getEnd());
            Matcher javaMatcher = RegexMatchUtils.createMatcher(subfield, regex);
            if (javaMatcher.matches()) {
                matchingResults.add(span);
            }
        }
        return matchingResults;

    }
    //VerifyCondition: EQUAL verifyOr = false
    public List<Span> verifyResultsEQUALOr(List<Span> spanList) {
        List<Span> matchingResults = new ArrayList<>();
        String reverseFieldValue = RegexMatchUtils.getReverseFieldValue(fieldValue);
        String reverseRegex = RegexMatchUtils.getReverseRegex(regex);


        for (Span span : spanList) {
            String reverseSubfield = reverseFieldValue.substring(
                    reverseFieldValue.length() - span.getStart(), reverseFieldValue.length() - span.getEnd());
            Matcher javaMatcher = RegexMatchUtils.createMatcher(reverseSubfield, reverseRegex);
            if (javaMatcher.matches()) {
                matchingResults.add(span);
            }
        }
        return matchingResults;

    }

}


