package edu.ics.uci.regex.optimizer.expression;

import java.util.Objects;

public class InputRef implements ExprOperand {

    private final int index;

    // TODO: not general, change later
    public enum SpanAccess {
        START,
        END
    }
    private final SpanAccess spanAccess;

    public static InputRef of(int index, SpanAccess spanAccess) {
        return new InputRef(index, spanAccess);
    }

    private InputRef(int index, SpanAccess spanAccess) {
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
        InputRef inputRef = (InputRef) o;
        return index == inputRef.index &&
                Objects.equals(spanAccess, inputRef.spanAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, spanAccess);
    }

    @Override
    public String toString() {
        return "InputRef{" +
                "index=" + index +
                ", spanAccess=" + spanAccess +
                '}';
    }
}
