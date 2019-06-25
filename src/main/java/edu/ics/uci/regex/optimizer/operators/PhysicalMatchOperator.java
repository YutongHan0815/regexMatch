package edu.ics.uci.regex.optimizer.operators;

import edu.ics.uci.optimizer.operator.PhysicalOperator;
import edu.ics.uci.regex.runtime.regexMatcher.execution.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.execution.MatchRegex;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;

import java.util.Objects;


public class PhysicalMatchOperator extends MatchOperator implements PhysicalOperator {

    // match left to right / match reverse regex
    final boolean reverseMatch;

    public PhysicalMatchOperator(SubRegex subRegex, boolean reverseMatch) {
        super(subRegex);

        this.reverseMatch = reverseMatch;
    }

    public boolean isReverseMatch() {
        return reverseMatch;
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
        if (!super.equals(o)) return false;
        PhysicalMatchOperator that = (PhysicalMatchOperator) o;
        return reverseMatch == that.reverseMatch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reverseMatch);
    }

    @Override
    public String toString() {
        return "PhysicalMatchOperator{" +
                "reverseMatch=" + reverseMatch +
                "} " + super.toString();
    }
}
