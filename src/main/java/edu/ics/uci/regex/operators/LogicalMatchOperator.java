package edu.ics.uci.regex.operators;


import edu.ics.uci.optimizer.operator.Operator;

import java.io.Serializable;
import java.util.Objects;


public class LogicalMatchOperator implements Operator, Serializable {

    //Query regex
    private final String subRegex;

    //Cluster which the operator belongs to
    //public String opCluster;


    public LogicalMatchOperator(String mainRegex){
        this.subRegex = mainRegex;

    }

    public String getSubRegex() {
        return subRegex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalMatchOperator that = (LogicalMatchOperator) o;
        return Objects.equals(subRegex, that.subRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subRegex);
    }

    public String getDigest() {
        return "LogicalMatchOperator(" + subRegex + ")";
    }
}
