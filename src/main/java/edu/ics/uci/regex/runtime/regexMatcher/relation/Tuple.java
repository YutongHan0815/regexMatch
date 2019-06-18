package edu.ics.uci.regex.runtime.regexMatcher.relation;

import java.io.Serializable;
import java.util.Objects;


public class Tuple implements Serializable {
    private final int tupleId;
    private final TupleNode rootNode;

    public Tuple(int tupleId, TupleNode rootNode) {
        this.tupleId = tupleId;
        this.rootNode = rootNode;
    }

    public TupleNode getRootNode() {
        return rootNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return tupleId == tuple.tupleId &&
                Objects.equals(rootNode, tuple.rootNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tupleId, rootNode);
    }
}
