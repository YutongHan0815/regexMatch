package edu.ics.uci.regex.runtime.regexMatcher.relation;

import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;

import java.io.Serializable;
import java.util.*;

public class Relation implements Serializable {
    public  List<Tuple> tupleList;
    private RelationContext relationContext;
    private final String fieldValue;

    public static Relation create(String fieldValue) {
        return new Relation(new ArrayList<>(), fieldValue);
    }

    public Relation(List<Tuple> tupleList, String fieldValue) {

        this.tupleList = tupleList;
        this.relationContext = new RelationContext(this);
        this.fieldValue = fieldValue;
    }

    public List<Tuple> getTupleList() {
        return tupleList;
    }

    public RelationContext getRelationContext() {
        return relationContext;
    }

    public String getFieldValue() {
        return fieldValue;
    }


    //For matchOperator
    public void addTuple(Span span) {
        TupleNode tupleNode = TupleNode.create(span);
        Tuple tuple = new Tuple(relationContext.nextTupleID(), tupleNode);
        tupleList.add(tuple);
    }
    public Relation getInitializeRelation() {
        List<Tuple> tupleList = new ArrayList<>();

        Span span = new Span(0, fieldValue.length()-1);
        TupleNode tupleNode = TupleNode.create(span);
        tupleList.add(new Tuple(relationContext.nextTupleID(), tupleNode));

        return new Relation(tupleList, fieldValue);
    }

}
