package edu.ics.uci.regex.optimizer.operators;


public class LogicalProjectOperator extends ProjectOperator {

    public LogicalProjectOperator(int leftIndex, int rightIndex, int resultIndex) {
        super(leftIndex, rightIndex, resultIndex);
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
