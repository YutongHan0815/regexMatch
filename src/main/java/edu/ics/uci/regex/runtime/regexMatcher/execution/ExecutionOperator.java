package edu.ics.uci.regex.runtime.regexMatcher.execution;

import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;

import java.util.List;


public interface ExecutionOperator {

     Relation computeMatchingResult(List<Relation> inputs);
     <T extends ExecutionOperator> T setInputs(List<Relation> relation);

}
