package edu.ics.uci.regex.runtime.regexMatcher.execution;

import edu.ics.uci.regex.optimizer.operators.PhysicalJoinOperator;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;

import java.io.Serializable;
import java.util.List;

public class JoinRelations implements ExecutionOperator, Serializable {
    private final PhysicalJoinOperator physicalJoinOperator;

    public JoinRelations(PhysicalJoinOperator physicalJoinOperator) {
        this.physicalJoinOperator = physicalJoinOperator;


    }


    @Override
    public <T extends ExecutionOperator> T setInputs(List<Relation> inputs) {
        JoinRelations joinRelations = new JoinRelations(physicalJoinOperator);
        return  (T) joinRelations;
    }
    @Override
    //TODO
    public Relation computeMatchingResult(List<Relation> inputs) {
        Relation matchingResults = Relation.create(inputs.get(0).getFieldValue());

        return  matchingResults;
    }


}
