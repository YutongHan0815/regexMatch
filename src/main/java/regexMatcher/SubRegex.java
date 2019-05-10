package regexMatcher;


//import com.google.re2j.Pattern;

import java.util.List;
import java.util.regex.Pattern;

public class SubRegex extends AbstractSubSequence{
    public enum ComplexityLevel{
        High,
        Medium,
        Low
    }
    Pattern regexPattern;
    Pattern startWithRegexPattern;
    Pattern startToEndRegexPattern;
    Pattern endWithRegexPattern;
    String regex;
    ComplexityLevel complexity;
    SubRegex reverseSubRegex;
    Double selectivity;
    int minLength;
    int maxlength;
    int originalSubId;

    public Double getSelectivity() {
        return selectivity;
    }

    public SubRegex(String regex){
        super(-1, -1);
        super.setOriginalSubCount(-1);
        this.regex = regex;
        originalSubId = 0;
        regexPattern = null;
        startWithRegexPattern = null;
        startToEndRegexPattern = null;
        endWithRegexPattern = null;
        this.complexity = ComplexityLevel.High;
        reverseSubRegex = null;
        minLength = 0;
        maxlength = 0;
        selectivity = 0d;

    }


    public String getsubRegexPredicate() {
        return regex;
    }

    public void setRegex(String predicate) {
        this.regex = predicate;
    }

    public void setSelectivity(Double selectivity) {
        this.selectivity = selectivity;
    }

    public SubRegex(String regex, int startingCSRIndex, int numberOfCSRs, ComplexityLevel complexity, int  originalSubId, int minLength, int maxlength){
        super(startingCSRIndex, numberOfCSRs);
        super.setOriginalSubCount(0);
        regexPattern = regexPattern.compile(regex);
        startWithRegexPattern = startWithRegexPattern.compile("^" + regex);
        startToEndRegexPattern = startToEndRegexPattern.compile("^" + regex + "$");
        endWithRegexPattern = endWithRegexPattern.compile(regex + "$");
        this.regex = regex;
        this.complexity = complexity;
        this.reverseSubRegex = reverseSubRegex;
        this.minLength = minLength;
        this.maxlength = maxlength;

        this.originalSubId = originalSubId;
        selectivity = 0d;

    }

    public void setOriginalSubCount(int count){
        super.setOriginalSubCount(count);
        if(reverseSubRegex != null){
            reverseSubRegex.setOriginalSubCount(count);
        }
    }

    public void setReverseSubRegex(SubRegex reverse){
        reverseSubRegex = reverse;
    }

    public SubRegex getReverseSubRegex(){
        return reverseSubRegex;
    }

    public String toString(){
        return toStringShort() + "///" + complexity + "///" + regex;
    }

    public static String getPlanSignature(List<SubRegex> plan){
        String signature = "";
        for(SubRegex s: plan){
            signature += s.toStringShort();
        }
        return signature;
    }


    @Override
    public boolean isSubRegex() {
        return true;
    }


}
