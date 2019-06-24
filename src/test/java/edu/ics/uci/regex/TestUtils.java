package edu.ics.uci.regex;

public class TestUtils {
    public TestUtils() {

    }
    public static boolean equals(Relation expectedResults, Relation exactResults) {

        if (expectedResults != exactResults) {
            return false;
        } else return true;

    }


}
