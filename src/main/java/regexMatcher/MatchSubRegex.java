package regexMatcher;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchSubRegex implements MatchRegex, Serializable {
    public String subRegex;
    public boolean matchOr;
    public String fieldValue;

    public MatchSubRegex(String subRegex, boolean matchOr, String fieldValue) {
        this.subRegex = subRegex;
        this.matchOr = matchOr;
        this.fieldValue = fieldValue;
    }

    public List<Span> computeMatchingResult() {
        Pattern pattern = Pattern.compile(subRegex);
        List<Span> matchingResults = new ArrayList<>();

        Matcher javaMatcher = pattern.matcher(fieldValue);
        while (javaMatcher.find()) {
            int start = javaMatcher.start();
            int end = javaMatcher.end();

            matchingResults.add(
                    new Span( start, end));
        }

        return matchingResults;
    }
    public List<Span> computeReverseMatchingResult() {
        String reverseFieldValue = reverseString(fieldValue);
        String reverseRegex = reverseRegex(subRegex);

        Pattern pattern = Pattern.compile(reverseRegex);
        List<Span> matchingResults = new ArrayList<>();

        Matcher javaMatcher = pattern.matcher(reverseFieldValue);
        while (javaMatcher.find()) {
            int start = javaMatcher.start();
            int end = javaMatcher.end();

            matchingResults.add(
                    new Span( fieldValue.length()-end, fieldValue.length()-start));
        }

        return matchingResults;
    }

    public List<Span> computeMatchingResult(Relation relation) {
        List<Span> spanList = relation.spanList;
        Pattern pattern = Pattern.compile(subRegex);
        Matcher javaMatcher = pattern.matcher(fieldValue);

        List<Span> matchingResults = new ArrayList<>();
        for(Span span : spanList) {
            if(javaMatcher.find(span.getEnd()) && javaMatcher.start() == span.getEnd()){
                int start = javaMatcher.start();
                int end = javaMatcher.end();
                matchingResults.add(new Span(start, end));
            }
        }
        return matchingResults;
    }

    public List<Span> computeReverseMatchingResult(Relation relation) {
        String reverseFieldValue = reverseString(fieldValue);
        String reverseRegex = reverseRegex(subRegex);
        List<Span> spanList = relation.spanList;

        Pattern pattern = Pattern.compile(reverseRegex);

        List<Span> matchingResults = new ArrayList<>();
        Matcher javaMatcher = pattern.matcher(reverseFieldValue);

        //Todo
        for(Span span : spanList) {
            if(javaMatcher.find(span.getEnd()) && javaMatcher.start() == span.getEnd()){
                int start = javaMatcher.start();
                int end = javaMatcher.end();
                matchingResults.add(new Span(start, end));
            }
        }
        return matchingResults;
    }


    private String reverseString(String fieldValue) {
        StringBuilder sb = new StringBuilder(fieldValue);
        return  sb.reverse().toString();
    }
    private String reverseRegex(String regex) {
        PublicRegexp re = PublicParser.parse(regex, PublicRE2.PERL);
        re = PublicSimplify.simplify(re);

        return PublicRegexp.reverseDeepCopy(re);
    }


    public String getSubRegex() {
        return subRegex;
    }

    public void setSubRegex(String subRegex) {
        this.subRegex = subRegex;
    }


    public boolean isMatchOr() {
        return matchOr;
    }

    public void setMatchOr(boolean matchOr) {
        this.matchOr = matchOr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchSubRegex that = (MatchSubRegex) o;
        return matchOr == that.matchOr &&
                Objects.equals(subRegex, that.subRegex) &&
                Objects.equals(fieldValue, that.fieldValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subRegex, matchOr, fieldValue);
    }
}
