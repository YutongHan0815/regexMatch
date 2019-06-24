package edu.ics.uci.optimizer.operator;

import edu.ics.uci.regex.runtime.regexMatcher.execution.ExecutionOperator;

public interface PhysicalOperator extends Operator {

    ExecutionOperator getExecution();

}
