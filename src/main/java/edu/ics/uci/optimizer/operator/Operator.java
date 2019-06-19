package edu.ics.uci.optimizer.operator;


import edu.ics.uci.regex.runtime.regexMatcher.ExecutionOperator;

public interface Operator {
    String getDigest();
    ExecutionOperator getExecution();
}
