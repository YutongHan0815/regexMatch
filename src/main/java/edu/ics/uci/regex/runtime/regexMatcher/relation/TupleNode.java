package edu.ics.uci.regex.runtime.regexMatcher.relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TupleNode implements Serializable {
    private Span span;
    private List<TupleNode> childrenNode;

    public static TupleNode create(Span span) {
        return new TupleNode(span, new ArrayList<>());
    }
    public TupleNode(Span span, List<TupleNode> childrenNode) {
        this.span = span;
        this.childrenNode = childrenNode;
    }

    public Span getSpan() {
        return span;
    }

    public List<TupleNode> getChildrenNode() {
        return childrenNode;
    }
}
