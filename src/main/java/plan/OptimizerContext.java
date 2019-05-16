package plan;

import java.io.Serializable;

public class OptimizerContext implements Serializable {

    private final RegexPlanner regexPlanner;
    private int nextOperatorID = 0;
    private int nextSetID = 0;

    public OptimizerContext(RegexPlanner regexPlanner) {
        this.regexPlanner = regexPlanner;
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
