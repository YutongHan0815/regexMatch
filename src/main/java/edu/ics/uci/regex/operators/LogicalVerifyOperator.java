package edu.ics.uci.regex.operators;

import edu.ics.uci.optimizer.operator.Operator;

import java.io.Serializable;
import java.util.Objects;

public class LogicalVerifyOperator implements Operator, Serializable {

    private final String subRegex;
    private final Condition condition;

    public LogicalVerifyOperator(String subRegex, Condition condition) {
        this.subRegex = subRegex;
        this.condition = condition;
    }

    public String getSubRegex() {
        return subRegex;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalVerifyOperator that = (LogicalVerifyOperator) o;
        return Objects.equals(subRegex, that.subRegex) &&
                condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subRegex, condition);
    }

    public String getDigest() {
        return "LogicalVerifyOperator(" + subRegex + condition + ")";
    }


}
