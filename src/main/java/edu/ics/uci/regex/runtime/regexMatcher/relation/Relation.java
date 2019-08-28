package edu.ics.uci.regex.runtime.regexMatcher.relation;


import java.io.Serializable;
import java.util.*;

public class Relation implements Serializable {
    public  List<Tuple> tupleList;
    private final String fieldValue;

    public static Relation create(String fieldValue) {
        return new Relation(new ArrayList<>(), fieldValue);
    }

    public Relation(List<Tuple> tupleList, String fieldValue) {

        this.tupleList = tupleList;

        this.fieldValue = fieldValue;
    }

    public List<Tuple> getTupleList() {
        return tupleList;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void addTuple(Span span) {

    }


}
