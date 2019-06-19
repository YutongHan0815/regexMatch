package edu.ics.uci.regex.runtime.regexMatcher;


import edu.ics.uci.regex.optimizer.operators.PhysicalMatchOperator;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Span;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;


public class MatchRegex implements ExecutionOperator, Serializable {
    private final PhysicalMatchOperator physicalMatchOperator;


    public MatchRegex(PhysicalMatchOperator physicalMatchOperator) {
       this.physicalMatchOperator = physicalMatchOperator;
    }

    public PhysicalMatchOperator getPhysicalMatchOperator() {
        return physicalMatchOperator;
    }


    @Override
    // Match regex on a string from left to right
    public Relation computeMatchingResult(List<Relation> inputs) {
        String mainRegex = physicalMatchOperator.getSubRegex().regex;
        String fieldValue = inputs.get(0).getFieldValue();
        Relation matchingResults = Relation.create(fieldValue);
        inputs.get(0).tupleList.forEach(tuple-> {
            Span span = tuple.getRootNode().getSpan();
            Matcher javaMatcher = RegexMatchUtils.createMatcher(fieldValue.substring(span.getStart(),
                    span.getEnd()), mainRegex);
            while (javaMatcher.find()) {
                int start = javaMatcher.start();
                int end = javaMatcher.end();
                matchingResults.addTuple(new Span(start, end));
            }
        });


        return matchingResults;
    }

//    //Match reverse regex on a reverse string from right to left
//    public List<Span> computeReverseMatchingResult() {
//        List<Span> matchingResults = new ArrayList<>();
//        String reverseFieldValue = RegexMatchUtils.getReverseFieldValue(fieldValue);
//        String reverseRegex = RegexMatchUtils.getReverseRegex(subRegex.regex);
//        Matcher javaMatcher = RegexMatchUtils.createMatcher(reverseFieldValue, reverseRegex);
//
//        while (javaMatcher.find()) {
//            int start = javaMatcher.start();
//            int end = javaMatcher.end();
//
//            matchingResults.add(
//                    new Span( fieldValue.length()-end, fieldValue.length()-start));
//        }
//
//        return matchingResults;
//    }

    @Override
    public <T extends ExecutionOperator> T setInputs(List<Relation> relation) {
        MatchRegex matchRegex = new MatchRegex(physicalMatchOperator);
        return  (T) matchRegex;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchRegex that = (MatchRegex) o;
        return Objects.equals(physicalMatchOperator, that.physicalMatchOperator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(physicalMatchOperator);
    }

}
