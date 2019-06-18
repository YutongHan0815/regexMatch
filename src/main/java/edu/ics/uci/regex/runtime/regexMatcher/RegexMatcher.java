package edu.ics.uci.regex.runtime.regexMatcher;


public class RegexMatcher {

    private String regex;

    public RegexMatcher(String regex) {
        this.regex = regex;
    }

    public void setUp() {
        //System.out.println("4.1.setUp");
        RegexParser parser = new RegexParser(regex);


    }

    public boolean computeMatchingResult(String fieldValue) {
        return false;
    }




}
