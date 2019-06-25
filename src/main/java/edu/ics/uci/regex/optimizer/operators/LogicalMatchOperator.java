package edu.ics.uci.regex.optimizer.operators;

import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;

import java.io.Serializable;


public class LogicalMatchOperator extends MatchOperator {

    public LogicalMatchOperator(String mainRegexString){
        this(new SubRegex(mainRegexString));

    }
    public LogicalMatchOperator(SubRegex mainRegex){
        super(mainRegex);

    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "LogicalMatchOperator{} " + super.toString();
    }
}
