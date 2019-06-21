package edu.ics.uci.regex.optimizer.operators;


import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.PhysicalOperator;
import edu.ics.uci.regex.runtime.regexMatcher.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.MatchRegex;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;

import java.io.Serializable;
import java.util.Objects;


public class PhysicalMatchOperator implements PhysicalOperator, Serializable {
    private final SubRegex subRegex;
    // match left to right / match reverse regex
    private final boolean matchDirection;

    public static PhysicalMatchOperator create(SubRegex subRegex) {
        return new PhysicalMatchOperator(subRegex, true);
    }

    public static PhysicalMatchOperator create(String mainRegex) {
        return new PhysicalMatchOperator(new SubRegex(mainRegex), true);
    }

    public PhysicalMatchOperator(SubRegex subRegex, boolean matchDirection) {
        Preconditions.checkNotNull(subRegex);

        this.subRegex = subRegex;
        this.matchDirection = matchDirection;
    }

    public SubRegex getSubRegex() {
        return subRegex;
    }

    public boolean isMatchDirection() {
        return matchDirection;
    }

    @Override
    public ExecutionOperator getExecution() {
        MatchRegex matcher = new MatchRegex(this);
        return matcher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalMatchOperator that = (PhysicalMatchOperator) o;
        return matchDirection == that.matchDirection &&
                Objects.equals(subRegex, that.subRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subRegex, matchDirection);
    }

    @Override
    public String toString() {
        return "PhysicalMatchOperator{" +
                "subRegex=" + subRegex +
                ", matchDirection=" + matchDirection +
                '}';
    }
}
