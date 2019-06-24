package edu.ics.uci.regex.optimizer.operators;

import com.google.common.base.Preconditions;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.regex.optimizer.expression.Expression;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class LogicalJoinOperator implements Operator, Serializable {

//    private final Condition condition;
    private final Expression condition;

    public LogicalJoinOperator(Expression condition) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalJoinOperator that = (LogicalJoinOperator) o;
        return Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition);
    }

    @Override
    public String toString() {
        return "LogicalJoinOperator{" +
                "condition=" + condition +
                '}';
    }
}
