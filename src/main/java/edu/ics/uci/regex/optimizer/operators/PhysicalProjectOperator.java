package edu.ics.uci.regex.optimizer.operators;

import edu.ics.uci.optimizer.operator.PhysicalOperator;
import edu.ics.uci.regex.runtime.regexMatcher.execution.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.execution.Project;

public class PhysicalProjectOperator extends ProjectOperator implements PhysicalOperator {

    public PhysicalProjectOperator(int leftIndex, int rightIndex, int resultIndex) {
        super(leftIndex, rightIndex, resultIndex);
    }

    @Override
    public ExecutionOperator getExecution() {
        Project matcher = new Project(this);
        return matcher;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "PhysicalProjectOperator{} " + super.toString();
    }

}
