package edu.ics.uci.regex.regexMatcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Relation implements Serializable {
    public List<Span> spanList;

    public Relation() {
        this.spanList = new ArrayList<>();
    }
}
