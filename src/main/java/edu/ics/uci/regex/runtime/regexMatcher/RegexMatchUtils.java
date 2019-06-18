package edu.ics.uci.regex.runtime.regexMatcher;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatchUtils implements Serializable {


    public static String getReverseFieldValue(String fieldValue) {
        StringBuilder sb = new StringBuilder(fieldValue);
        return  sb.reverse().toString();
    }
    public static String getReverseRegex(String regex) {
        PublicRegexp re = PublicParser.parse(regex, PublicRE2.PERL);
        re = PublicSimplify.simplify(re);

        return PublicRegexp.reverseDeepCopy(re);
    }
    public static Matcher createMatcher(String fieldValue, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher javaMatcher = pattern.matcher(fieldValue);
        return javaMatcher;
    }

}
