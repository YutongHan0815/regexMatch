package regexMatcher;




import java.util.ArrayList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plan.RegexParser;


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
