package edu.ics.uci.regex.runtime.regexMatcher;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.regex.Pattern;

public class SubRegex extends AbstractSubSequence{
    public enum ComplexityLevel{
        High,
        Medium,
        Low
    }
    Pattern regexPattern;
    String regex;
    ComplexityLevel complexity;
    SubRegex reverseSubRegex;
    Double selectivity;
    int minLength;
    int maxlength;
    int originalSubId;

    public SubRegex(String regex){
        super(-1, -1);
        Preconditions.checkNotNull(regex);
        super.setOriginalSubCount(-1);
        this.regex = regex;
        originalSubId = 0;
        regexPattern = null;
        this.complexity = ComplexityLevel.High;
        reverseSubRegex = null;
        minLength = 0;
        maxlength = 0;
        selectivity = 0d;

    }


    public SubRegex(String regex, int startingCSRIndex, int numberOfCSRs, ComplexityLevel complexity, int  originalSubId, int minLength, int maxlength){
        super(startingCSRIndex, numberOfCSRs);
        super.setOriginalSubCount(0);
        regexPattern = regexPattern.compile(regex);
        this.regex = regex;
        this.complexity = complexity;
        this.reverseSubRegex = reverseSubRegex;
        this.minLength = minLength;
        this.maxlength = maxlength;

        this.originalSubId = originalSubId;
        selectivity = 0d;

    }

    public Pattern getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(Pattern regexPattern) {
        this.regexPattern = regexPattern;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public ComplexityLevel getComplexity() {
        return complexity;
    }

    public void setComplexity(ComplexityLevel complexity) {
        this.complexity = complexity;
    }

    public Double getSelectivity() {
        return selectivity;
    }

    public void setSelectivity(Double selectivity) {
        this.selectivity = selectivity;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    public int getOriginalSubId() {
        return originalSubId;
    }

    public void setOriginalSubId(int originalSubId) {
        this.originalSubId = originalSubId;
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
