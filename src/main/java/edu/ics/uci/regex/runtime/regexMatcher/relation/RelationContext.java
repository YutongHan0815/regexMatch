package edu.ics.uci.regex.runtime.regexMatcher.relation;

import java.io.Serializable;

public class RelationContext implements Serializable {
    private int nextTupleID = 0;
    private int schemaNodeID = 0;
    private final Relation relation;

    public RelationContext(Relation relation) {

        this.relation = relation;
    }

    public int nextTupleID() {
        int opID = nextTupleID;
        this.nextTupleID++;
        return opID;
    }
    public int nextSchemaNodeID() {
        int opID = schemaNodeID;
        this.schemaNodeID++;
        return opID;
    }

}
