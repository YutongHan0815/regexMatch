package edu.ics.uci.regex.optimizer.operators;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.schema.Field;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.regex.optimizer.expression.Expression;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class JoinOperator implements Operator, Serializable {

    final Expression condition;

    JoinOperator(Expression condition) {
        Preconditions.checkNotNull(condition);
        this.condition = condition;
    }

    public Expression getCondition() {
        return condition;
    }

    @Override
    public RowType deriveRowType(List<RowType> inputRowTypeList) {
        Preconditions.checkArgument(inputRowTypeList.size() == 2);
        return RowType.of(ImmutableList.<Field>builder()
                .addAll(inputRowTypeList.get(0).getFields()).addAll(inputRowTypeList.get(1).getFields()).build()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinOperator that = (JoinOperator) o;
        return Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition);
    }

    @Override
    public String toString() {
        return "JoinOperator{" +
                "condition=" + condition +
                '}';
    }
}
