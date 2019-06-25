package edu.ics.uci.regex.optimizer.expression;

import java.util.Objects;

public class SpanInputRef implements ExprOperand {

    public enum SpanAccess {
        START,
        END
    }

    private final int index;
    private final SpanAccess spanAccess;

    public static SpanInputRef of(int index, SpanAccess spanAccess) {
        return new SpanInputRef(index, spanAccess);
    }

    private SpanInputRef(int index, SpanAccess spanAccess) {
        this.index = index;
        this.spanAccess = spanAccess;
    }

    public int getIndex() {
        return index;
    }

    public SpanAccess getSpanAccess() {
        return spanAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpanInputRef spanInputRef = (SpanInputRef) o;
        return index == spanInputRef.index &&
                Objects.equals(spanAccess, spanInputRef.spanAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, spanAccess);
    }

    @Override
    public String toString() {
        return "SpanInputRef{" +
                "index=" + index +
                ", spanAccess=" + spanAccess +
                '}';
    }
}
