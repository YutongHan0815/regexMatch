package edu.ics.uci.regex.runtime.regexMatcher;

import edu.ics.uci.regex.runtime.regexMatcher.relation.Tuple;

public interface QueryEngine {
    Tuple executeQuery(Tuple tuple);

}
