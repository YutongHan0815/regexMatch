package edu.ics.uci.regex.optimizer.operators;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.schema.Field;
import edu.ics.uci.optimizer.operator.schema.RowType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class ProjectOperator implements Operator, Serializable {

    final int leftIndex;
    final int rightIndex;
    final int resultIndex;

    ProjectOperator(int leftIndex, int rightIndex, int resultIndex) {
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
        this.resultIndex = resultIndex;
    }

    public int getLeftIndex() {
        return leftIndex;
    }

    public int getRightIndex() {
        return rightIndex;
    }

    public int getResultIndex() {
        return resultIndex;
    }

    @Override
    public RowType deriveRowType(List<RowType> inputRowTypeList) {
        Preconditions.checkArgument(inputRowTypeList.size() == 1);
        RowType inputRowType = inputRowTypeList.get(0);
        Preconditions.checkArgument(leftIndex < inputRowType.getFields().size());
        Preconditions.checkArgument(rightIndex < inputRowType.getFields().size());

        Builder<Field> builder = ImmutableList.builder();

//        for (int i = 0; i < inputRowType.getFields().size(); i++) {
//            if (i != leftIndex && i != rightIndex) {
//                builder.add(inputRowType.getFields().get(i));
//            }
//        }
//        builder.add(Field.of(inputRowType.getFields().get(leftIndex).getName() +
//                inputRowType.getFields().get(rightIndex).getName(), inputRowType.getFields().get(leftIndex).getType()));

        for (int i = 0; i < inputRowType.getFields().size(); i++) {
            if (i != leftIndex && i != rightIndex) {
                if (i == resultIndex) {
                    builder.add(Field.of(inputRowType.getFields().get(leftIndex).getName() +
                            inputRowType.getFields().get(rightIndex).getName(), inputRowType.getFields().get(leftIndex).getType()));
                }
                builder.add(inputRowType.getFields().get(i));
            } else
                builder.add(Field.of(inputRowType.getFields().get(leftIndex).getName() +
                        inputRowType.getFields().get(rightIndex).getName(), inputRowType.getFields().get(leftIndex).getType()));
        }

        return RowType.of(builder.build());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectOperator that = (ProjectOperator) o;
        return leftIndex == that.leftIndex &&
                rightIndex == that.rightIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftIndex, rightIndex);
    }
}
