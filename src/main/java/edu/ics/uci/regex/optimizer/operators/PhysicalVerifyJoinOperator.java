package edu.ics.uci.regex.optimizer.operators;

import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.PhysicalOperator;
import edu.ics.uci.regex.optimizer.expression.Expression;
import edu.ics.uci.regex.runtime.regexMatcher.execution.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;
import edu.ics.uci.regex.runtime.regexMatcher.execution.VerifyRegex;

import java.util.Objects;

public class PhysicalVerifyJoinOperator extends JoinOperator implements PhysicalOperator {

    final boolean matchDirection;
    final SubRegex subRegex;

    public PhysicalVerifyJoinOperator(Expression condition, boolean matchDirection, SubRegex subRegex) {
        super(condition);

        Preconditions.checkNotNull(subRegex);
        this.matchDirection = matchDirection;
        this.subRegex = subRegex;
    }

    public SubRegex getSubRegex() {
        return subRegex;
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
        if (!super.equals(o)) return false;
        PhysicalVerifyJoinOperator that = (PhysicalVerifyJoinOperator) o;
        return matchDirection == that.matchDirection &&
                Objects.equals(subRegex, that.subRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), matchDirection, subRegex);
    }

    @Override
    public String toString() {
        return "PhysicalVerifyJoinOperator{" +
                "matchDirection=" + matchDirection +
                ", subRegex=" + subRegex +
                ", condition=" + condition +
                "} " + super.toString();
    }
}

