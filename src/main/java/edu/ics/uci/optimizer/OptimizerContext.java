package edu.ics.uci.optimizer;

import java.io.Serializable;

/**
 * OptimizerContext keeps global identifier of {@link edu.ics.uci.optimizer.operator.Operator} and
 * identifier of {@link edu.ics.uci.optimizer.operator.EquivSet}
 * The identifier is incremental when new operators or EquivSets are created.
 */
public class OptimizerContext implements Serializable {

    private final OptimizerPlanner optimizerPlanner;
    //Identifier of Operator
    private int nextOperatorID = 0;

    //Identifier of EquivSet
    private int nextSetID = 0;

    public OptimizerContext(OptimizerPlanner optimizerPlanner) {
        this.optimizerPlanner = optimizerPlanner;
    }

    public int nextOperatorID() {
        int opID = nextOperatorID;
        this.nextOperatorID++;
        return opID;
    }

    public int nextSetID() {
        int setID = nextSetID;
        this.nextSetID++;
        return setID;
    }

}
