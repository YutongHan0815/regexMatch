package edu.ics.uci.regex.runtime.regexMatcher.execution;

import edu.ics.uci.regex.optimizer.operators.PhysicalVerifyJoinOperator;
import edu.ics.uci.regex.runtime.regexMatcher.RegexMatchUtils;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Span;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class VerifyRegex implements ExecutionOperator, Serializable {
    private final PhysicalVerifyJoinOperator physicalVerifyJoinOperator;


    public VerifyRegex(PhysicalVerifyJoinOperator physicalVerifyJoinOperator) {
        this.physicalVerifyJoinOperator = physicalVerifyJoinOperator;
    }

    public PhysicalVerifyJoinOperator getPhysicalVerifyJoinOperator() {
        return physicalVerifyJoinOperator;
    }

    // Condition: AFTER verifyOr = true
    @Override
    public Relation computeMatchingResult(List<Relation> inputs) {
        Relation matchingResults = Relation.create(inputs.get(0).getFieldValue());
//        inputs.get(0).tupleList.forEach(tuple-> {
//            Span span = tuple.getRootNode().getSpan();
//            Matcher javaMatcher = RegexMatchUtils.createMatcher(inputs.get(0).getFieldValue().substring(span.getStart(), span.getEnd()),
//                    physicalVerifyJoinOperator.getSubRegex().getRegex());
//            int end = span.getEnd();
//            if (javaMatcher.find(end) && javaMatcher.start() == end) {
//                end = javaMatcher.end();
//                matchingResults.addTuple(new Span(span.getStart(), end));
//            }
//        });

        return matchingResults;
    }

    @Override
    public <T extends ExecutionOperator> T setInputs(List<Relation> relation) {
        VerifyRegex verifyRegex = new VerifyRegex(physicalVerifyJoinOperator);
        return  (T) verifyRegex;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerifyRegex that = (VerifyRegex) o;
        return Objects.equals(physicalVerifyJoinOperator, that.physicalVerifyJoinOperator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(physicalVerifyJoinOperator);
    }


    //
//    // Condition: AFTER verifyOr = false
//    public List<Span> verifyResultsAFTEROr(List<Span> spanList) {
//        List<Span> matchingResults = new ArrayList<>();
//        String reverseFieldValue = RegexMatchUtils.getReverseFieldValue(fieldValue);
//        String reverseRegex = RegexMatchUtils.getReverseRegex(regex);
//        Matcher javaMatcher = RegexMatchUtils.createMatcher(reverseFieldValue, reverseRegex);
//
//        for(Span span : spanList) {
//            while(javaMatcher.find()) {
//                if(fieldValue.length() - javaMatcher.end() == span.getStart())
//                    matchingResults.add(new Span(span.getStart(), fieldValue.length() - javaMatcher.start()));
//            }
//        }
//        return matchingResults;
//    }
//
//    //Condition: BEFORE verifyOr = false
//    public List<Span> verifyResultsBEFORE(List<Span> spanList) {
//        List<Span> matchingResults = new ArrayList<>();
//        String reverseFieldValue = RegexMatchUtils.getReverseFieldValue(fieldValue);
//        String reverseRegex = RegexMatchUtils.getReverseRegex(regex);
//        Matcher javaMatcher = RegexMatchUtils.createMatcher(reverseFieldValue, reverseRegex);
//
//        for (Span span : spanList) {
//            int start = span.getStart();
//            if (javaMatcher.find(fieldValue.length()-start) && javaMatcher.start() == start) {
//                start = javaMatcher.end();
//                matchingResults.add(new Span(start, span.getEnd()));
//            }
//        }
//        return matchingResults;
//
//    }
//
//    //Condition: BEFORE verifyOr = true
//    public  List<Span> verifyResultsBEFOREOr(List<Span> spanList) {
//        List<Span> matchingResults = new ArrayList<>();
//
//        Matcher javaMatcher = RegexMatchUtils.createMatcher(fieldValue, regex);
//
//        for (Span span : spanList) {
//            int start = span.getStart();
//            while (javaMatcher.find() && javaMatcher.end() == start) {
//                matchingResults.add(new Span(javaMatcher.start(), span.getEnd()));
//            }
//        }
//        return matchingResults;
//
//    }
//    //Condition: EQUAL verifyOr = true
//    public List<Span> verifyResultsEQUAL(List<Span> spanList) {
//
//        List<Span> matchingResults = new ArrayList<>();
//
//        for (Span span : spanList) {
//            String subfield = fieldValue.substring(span.getStart(), span.getEnd());
//            Matcher javaMatcher = RegexMatchUtils.createMatcher(subfield, regex);
//            if (javaMatcher.matches()) {
//                matchingResults.add(span);
//            }
//        }
//        return matchingResults;
//
//    }
//    //Condition: EQUAL verifyOr = false
//    public List<Span> verifyResultsEQUALOr(List<Span> spanList) {
//        List<Span> matchingResults = new ArrayList<>();
//        String reverseFieldValue = RegexMatchUtils.getReverseFieldValue(fieldValue);
//        String reverseRegex = RegexMatchUtils.getReverseRegex(regex);
//
//
//        for (Span span : spanList) {
//            String reverseSubfield = reverseFieldValue.substring(
//                    reverseFieldValue.length() - span.getStart(), reverseFieldValue.length() - span.getEnd());
//            Matcher javaMatcher = RegexMatchUtils.createMatcher(reverseSubfield, reverseRegex);
//            if (javaMatcher.matches()) {
//                matchingResults.add(span);
//            }
//        }
//        return matchingResults;
//
//    }

}


