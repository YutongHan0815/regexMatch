package edu.ics.uci.regex.optimizer.operators;



import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.PhysicalOperator;
import edu.ics.uci.regex.optimizer.expression.Expression;
import edu.ics.uci.regex.runtime.regexMatcher.execution.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;
import edu.ics.uci.regex.runtime.regexMatcher.execution.VerifyRegex;

import java.io.Serializable;
import java.util.Objects;


public class PhysicalVerifyJoinOperator implements PhysicalOperator, Serializable {

    private final Expression condition;
    private final boolean matchDirection;
    private final SubRegex subRegex;


    public static PhysicalVerifyJoinOperator create(Expression condition, SubRegex subRegex) {
        return new PhysicalVerifyJoinOperator(condition, true, subRegex);
    }

    public PhysicalVerifyJoinOperator(Expression condition, boolean matchDirection, SubRegex subRegex) {
        Preconditions.checkNotNull(condition);
        Preconditions.checkNotNull(subRegex);

        this.condition = condition;
        this.matchDirection = matchDirection;
        this.subRegex = subRegex;
    }

    public SubRegex getSubRegex() {
        return subRegex;
    }

    public Expression getCondition() {
        return condition;
    }

    public boolean isMatchDirection() {
        return matchDirection;
    }

    @Override
    public ExecutionOperator getExecution() {
        VerifyRegex matcher = new VerifyRegex(this);
        return matcher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalVerifyJoinOperator that = (PhysicalVerifyJoinOperator) o;
        return matchDirection == that.matchDirection &&
                condition == that.condition &&
                Objects.equals(subRegex, that.subRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, matchDirection, subRegex);
    }

    @Override
    public String toString() {
        return "PhysicalVerifyJoinOperator{" +
                "condition=" + condition +
                ", matchDirection=" + matchDirection +
                ", subRegex=" + subRegex +
                '}';
    }
}

