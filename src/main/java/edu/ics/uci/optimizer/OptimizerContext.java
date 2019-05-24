package edu.ics.uci.optimizer;

import java.io.Serializable;

public class OptimizerContext implements Serializable {

    private final OptimizerPlanner optimizerPlanner;
    private int nextOperatorID = 0;
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
