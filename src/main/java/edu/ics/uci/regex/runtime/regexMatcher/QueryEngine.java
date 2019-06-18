package edu.ics.uci.regex.runtime.regexMatcher;

import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;

public interface QueryEngine {
    Relation executeQuery(String fieldValue);

}
