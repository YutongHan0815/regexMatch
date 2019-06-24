package edu.ics.uci.regex.optimizer.operators;

import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.PhysicalOperator;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.regex.optimizer.expression.Expression;
import edu.ics.uci.regex.runtime.regexMatcher.execution.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.execution.JoinRelations;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PhysicalJoinOperator implements PhysicalOperator, Serializable {

    private final Expression condition;

    public static PhysicalJoinOperator create(Expression condition) {
        return new PhysicalJoinOperator(condition);
    }
    public PhysicalJoinOperator(Expression condition) {
        Preconditions.checkNotNull(condition);
        this.condition = condition;

    }

    public Expression getCondition() {
        return condition;
    }

    public RowType deriveRowType(List<RowType> inputRowTypeList) {
        Preconditions.checkArgument(inputRowTypeList.size() == 2);
        throw new UnsupportedOperationException();
//        return RowType.of(Field.of(this.subRegex.getRegex(), SPAN_TYPE));
    }

    @Override
    public ExecutionOperator getExecution() {
        JoinRelations matcher = new JoinRelations(this);
        return matcher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalJoinOperator that = (PhysicalJoinOperator) o;
        return Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition);
    }

    @Override
    public String toString() {
        return "PhysicalJoinOperator{" +
                "condition=" + condition +
                '}';
    }
}
