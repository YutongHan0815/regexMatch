package edu.ics.uci.regex.optimizer.operators;


public class LogicalProjectOperator extends ProjectOperator {

    public LogicalProjectOperator(int leftIndex, int rightIndex) {
        super(leftIndex, rightIndex);
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
        return "LogicalProjectOperator{} " + super.toString();
    }
}
