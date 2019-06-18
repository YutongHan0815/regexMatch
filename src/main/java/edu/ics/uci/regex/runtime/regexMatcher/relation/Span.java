package edu.ics.uci.regex.runtime.regexMatcher.relation;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;

public class Span implements Serializable {
    // The start position of the span, which is the offset of the gap before the
    // first character of the span.
    private int start;
    // The end position of the span, which is the offset of the gap after the
    // last character of the span.
    private int end;

    public Span(int start, int end) {
        Preconditions.checkNotNull(start);
        Preconditions.checkNotNull(end);

        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Span span = (Span) o;
        return start == span.start &&
                end == span.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
