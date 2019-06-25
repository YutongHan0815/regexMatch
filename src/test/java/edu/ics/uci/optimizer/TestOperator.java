package edu.ics.uci.optimizer;

import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.schema.Field;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.optimizer.operator.schema.SpanType;

import java.util.List;
import java.util.Objects;

public abstract class TestOperator implements Operator {

    public final String property;

    public TestOperator(String property) {
        this.property = property;
    }
    public String getDigest() {
        return "TestOperator(" + property + ")";
    }

    @Override
    public RowType deriveRowType(List<RowType> inputRowTypeList) {
        return RowType.of(Field.of(property, SpanType.SPAN_TYPE));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestOperator that = (TestOperator) o;
        return Objects.equals(property, that.property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + property + "}";
    }

    public static class OperatorA extends TestOperator {
        public OperatorA(String property) {
            super(property);
        }

    }

    public static class OperatorB extends TestOperator {
        public OperatorB(String property) {
            super(property);
        }
    }

    public static class OperatorC extends TestOperator {
        public OperatorC(String property) {
            super(property);
        }
    }

    public static class OperatorTwoInput extends TestOperator {
        public OperatorTwoInput(String property) {
            super(property);
        }
    }

    public static class OperatorMultiInput extends TestOperator {
        public OperatorMultiInput(String property) {
            super(property);
        }
    }

}
