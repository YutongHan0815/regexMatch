package edu.ics.uci.regex.optimizer.operators;

import edu.ics.uci.optimizer.operator.PhysicalOperator;
import edu.ics.uci.regex.runtime.regexMatcher.execution.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.execution.Project;

import java.io.Serializable;

public class PhysicalProjectOperator implements PhysicalOperator, Serializable {
    @Override
    public ExecutionOperator getExecution() {
        Project matcher = new Project(this);
        return matcher;
    }

}
