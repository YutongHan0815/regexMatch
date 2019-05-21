package plan;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;
import operators.LogicalMatchOperator;

import java.util.ArrayList;
import java.util.List;


public class RegexParser {

    private String regex;



    public RegexParser(String regex){
        this.regex = regex;

    }
    // transform a query regex Q into logicalMatch(Q)
    public void parser(){

        LogicalMatchOperator mainLogMatchOpt = new LogicalMatchOperator(regex);

    }


    public void breakIntoCoreSubRegexes() {
        // System.out.println(" breakIntoCoreSubregexes ");
        //int labelsIndex = 0;
        List<SubRegex> coreSubRegexes = new ArrayList<>();
        PublicRegexp re = PublicParser.parse(regex, PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        //System.out.println("1 breakIntoCoreSubregexes ");
        //List<String> subRegexSequences = new ArrayList<>();

        int numberOfCoreSubRegexes = 0;
        int minMainRegexLength = PublicRegexp.computeMinLength(re);
        int maxMainRegexLength = PublicRegexp.computeMaxLength(re);
        SubRegex.ComplexityLevel mainRegexComplexity = SubRegex.ComplexityLevel.Low;
        if (re.getOp() != PublicRegexp.PublicOp.CONCAT) {
            //String mainReverseRegex = PublicRegexp.reverseDeepCopy(re);
            //mainRegex = new SubRegex(regex);
            //coreSubRegexes.add(mainRegex);
            numberOfCoreSubRegexes = 1;
        } else {

            int subIndex = 0;
            int subCount = 0;
            for (PublicRegexp sub : re.getSubs()) {
                //System.out.println("sub " + sub+ " ");

                SubRegex.ComplexityLevel level = getRegexComplexity(sub);

                int minSubLength = PublicRegexp.computeMinLength(sub);
                int maxSubLength = PublicRegexp.computeMaxLength(sub);
                // System.out.println("sub " + sub+ " "+ minSubLength);

                // Keep also calculating the complexity of the full regex
                if (subIndex == 0) {
                    mainRegexComplexity = level;
                } else {
                    if (level == SubRegex.ComplexityLevel.High) {
                        mainRegexComplexity = SubRegex.ComplexityLevel.High;
                        // skip star and plus
                        // subCount ++;

                        //System.out.println("starsubCount " + subCount);
                        //continue;
                    } else if (level == SubRegex.ComplexityLevel.Medium &&
                            mainRegexComplexity != SubRegex.ComplexityLevel.High) {
                        mainRegexComplexity = SubRegex.ComplexityLevel.Medium;
                    }
                }


                /*
                String subString = null;
                if(sub.toString().startsWith("("))
                    subString = sub.toString().substring(1, sub.toString().length()-1);
                else subString = sub.toString();
                */
                //System.out.println("subtoString " + sub.toString());
                SubRegex coreSubRegex = new SubRegex(sub.toString(), subIndex, 1, level, subCount, minSubLength, maxSubLength);
                coreSubRegex.setOriginalSubCount(coreSubRegexes.size());
                coreSubRegexes.add(coreSubRegex);
                //System.out.println(coreSubRegex + "coreSubRegex " + coreSubRegex.originalSubId+" "+subIndex );


                // compute reverseSubRegex

                //String reverseSubString = "\\.([\\-0-9A-Z_a-z]+/)*[Convention-Za-z]?[Convention-Za-z]?[Convention-Za-z]?[Convention-Za-z]?[Convention-Za-z]?[Convention-Za-z][Convention-Za-z]\\.[\\--\\.0-9A-Za-z]+//:s?";

                // String reverseSub= "s?\\:\\/\\/[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,7}(?:\\/[\\w\\-]+)*\\.)";
                String reverseSubString = PublicRegexp.reverseDeepCopy(sub);
                // System.out.println("ReverseString============== " + reverseSubString);

                SubRegex reverseSubRegex = new SubRegex(reverseSubString, subIndex, 1, level, subCount, minSubLength, maxSubLength);
                coreSubRegex.setReverseSubRegex(reverseSubRegex);

                // System.out.println(sub + " coreSubRegex== ");
                //System.out.println("reverse " + reverseSubRegex);
                subIndex++;
                subCount++;
            }

            numberOfCoreSubRegexes = subIndex;
        }


        // for Convention(B*C)
/*
        SubRegex nextSub = null;
        for (int i = 1; i < coreSubRegexes.size(); i++) {
            if(nextSub == null) {
                nextSub = coreSubRegexes.get(i);
            }
            else
                nextSub = combineSubs(nextSub, coreSubRegexes.get(i));
            //System.out.println("nextSub "+nextSub.regexPatern.toString());
        }
        coreSubRegexes.set(1, nextSub);

 */

/**
 * * test different order of subregex matching
 */
/*
        coreSubRegexes.get(0).selectivity = 0.1;
        coreSubRegexes.get(1).selectivity = 0.2;
        coreSubRegexes.get(2).selectivity = 0.3;
        coreSubRegexes.sort(Comparator.comparing(SubRegex::getSelectivity));


 */


//        System.out.println("---------------------------------------");
//        for(SubSequence sub: coreSubSequences){
//        	System.out.println(sub.toString());
//        }
//        System.out.println("---------------------------------------");

    }
    private SubRegex.ComplexityLevel getRegexComplexity(PublicRegexp re){
        SubRegex.ComplexityLevel level = SubRegex.ComplexityLevel.Low;
        if(PublicRegexp.hasOp(re, PublicRegexp.PublicOp.STAR) ||
                PublicRegexp.hasOp(re, PublicRegexp.PublicOp.PLUS) ||
                PublicRegexp.hasOp(re, PublicRegexp.PublicOp.QUEST)){
            level = SubRegex.ComplexityLevel.High;
            return level;
        }
        if(PublicRegexp.hasOp(re, PublicRegexp.PublicOp.ALTERNATE) ||
                PublicRegexp.hasOp(re, PublicRegexp.PublicOp.REPEAT) ||
                //PublicRegexp.hasOp(re, PublicRegexp.PublicOp.QUEST) ||
                PublicRegexp.hasOp(re, PublicRegexp.PublicOp.CAPTURE)){
            for(PublicRegexp sub : re.getSubs()){
                SubRegex.ComplexityLevel subLevel = getRegexComplexity(sub);
                //System.out.println(sub + " "+ subLevel);
                if(subLevel == SubRegex.ComplexityLevel.High) {

                    level = subLevel;
                    break;
                }
                else
                    level = SubRegex.ComplexityLevel.Medium;
            }

        }
        return level;
    }
}