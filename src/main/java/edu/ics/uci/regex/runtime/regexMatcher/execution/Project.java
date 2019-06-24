package edu.ics.uci.regex.runtime.regexMatcher.execution;

import edu.ics.uci.regex.optimizer.operators.PhysicalProjectOperator;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;

import java.io.Serializable;
import java.util.List;

public class Project implements ExecutionOperator, Serializable {
    private final PhysicalProjectOperator physicalMatchOperator;

    public Project(PhysicalProjectOperator physicalMatchOperator) {
        this.physicalMatchOperator = physicalMatchOperator;
    }

    @Override
    public Relation computeMatchingResult(List<Relation> inputs) {
        return Relation.create(inputs.toString());
    }
    @Override
    public <T extends ExecutionOperator> T setInputs(List<Relation> relation) {
        Project project = new Project(physicalMatchOperator);
        return  (T) project;
    }

}
