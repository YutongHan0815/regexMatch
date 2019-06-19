package edu.ics.uci.regex;

import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;

public class TestUtils {
    public TestUtils() {

    }
    public static boolean equals(Relation expectedResults, Relation exactResults) {

        if (expectedResults != exactResults) {
            return false;
        } else return true;

    }


}
