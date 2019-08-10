package edu.ics.uci.regex.optimizer.operators;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.schema.Field;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.regex.optimizer.expression.Expression;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

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

       // System.out.println("deriveRowType" + inputRowTypeList);
        //left Fields are equal to right Fields
        if (inputRowTypeList.get(0).equals(inputRowTypeList.get(1)))
            return RowType.of(ImmutableList.<Field>builder()
                    .addAll(inputRowTypeList.get(0).getFields()).addAll(inputRowTypeList.get(1).getFields()).build()
            );

        ImmutableSet fieldsSet = ImmutableSet.<Field>builder()
                .addAll(inputRowTypeList.get(0).getFields()).addAll(inputRowTypeList.get(1).getFields()).build();
        ImmutableList fields = ImmutableList.copyOf(fieldsSet);
        return RowType.of(fields);
       // System.out.println("deriveRowType" + immutableSet);

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
