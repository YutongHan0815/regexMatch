package regexMatcher;




import java.util.ArrayList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plan.RegexParser;


public class RegexMatcher {

    private Pattern regexPattern;
    public String regex;
    private SubRegex mainRegex;
    public  String fieldValue = null;
    List<SubRegex> coreSubRegexes = new ArrayList<>();

    public RegexMatcher(String regex){
        this.regex = regex;
        this.regexPattern = Pattern.compile(regex);
    }
    public void setUp(){
        //System.out.println("4.1.setUp");
        // 1. break the regex into a number of sub-regexes that are called CoreSubRegexes or CSRs here.
        RegexParser parser = new RegexParser(regex);
        parser.breakIntoCoreSubRegexes();

        //2. initializeOffsetMatrix
        //initializeOffsetMatrix();
        // 2. maintain the subregexes in a graph.
        //generateExpandedSubRegexes();
        //generateSubRegexesCotainStar();

    }
    public boolean computeMatchingResult(String fieldValue){
        //printAllSubRegexes();

        if (fieldValue.isEmpty()) {
            return false;
        }
        else
            this.fieldValue = fieldValue;

        List<Span> matchingResults;

/*
        List<SubRegex> coreSubregexList = new ArrayList<>();
        for(SubRegex sub: coreSubRegexes){
            coreSubregexList.add(sub);
        }


 */

        if (coreSubRegexes.isEmpty()) {
            // 1. matching with JAVA regex
            matchingResults = computeMatchingResultsWithPattern(regexPattern);
        } else {

            //printAllSubRegexes();

                //if (mainRegex.complexity == SubRegex.ComplexityLevel.High)
           // matchingResults = computeMatchingResultsWithPattenAB(coreSubRegexes, -1);
            //matchingResults = computeMatchingResultsWithPatternApCpBv();
           // matchingResults = computeMatchingResultsWithPatternAlClBv();
            //matchingResults = computeMatchingResultsWithPattenABC(1);
            //matchingResults = compyteMatchingResultsWithABListC();
            matchingResults = compyteMatchingResultsWithABCList();




        }
        if(matchingResults.isEmpty())
            return false;
        else return  true;
            //System.out.println("matchingResultSize: " + matchingResults.size());
        }
    /**
     * Java Regex

     * @param pattern
     * @return
     */
    public List<Span> computeMatchingResultsWithPattern(Pattern pattern) {
        ///System.out.println("1 computeMatchingResultsWithPattern"+pattern.toString());
        //pattern = Pattern.compile(StringEscapeUtils.unescapeJava(pattern.toString()));
        //System.out.println("1 computeMatchingResultsWithPattern"+pattern.toString());
        List<Span> matchingResults = new ArrayList<>();

         //System.out.println("1 computeMatchingResultsWithPattern"+fieldValue);
        // Regex matching by JAVA regex matcher

        //int count = 0;
        int start;
        int end;
        //long startTime = System.nanoTime();

        Matcher javaMatcher = pattern.matcher(fieldValue);
        while (javaMatcher.find()) {
            start = javaMatcher.start();
            end = javaMatcher.end();
            //System.out.println(fieldValue.substring(start, end));
            matchingResults.add(
                    new Span( start, end, pattern.toString(), fieldValue.substring(start, end)));
        }
        //long endTime = System.nanoTime();
        //long time = (endTime - startTime)/1000;
        //System.out.print("  Time  " + time);



        return matchingResults;
    }

    /**For A(B*C) or C with reverse (AB*)
     *
     * @param flag
     * @return
     */
    public  List<Span> computeMatchingResultsWithPattenAB(List<SubRegex> coreSubRegexes, int flag) {
        //System.out.println("3 computeMatchingResultsWithPatterCoreSubregexes :"+ fieldValue);
        List<Span> matchingResults = new ArrayList<>();

        SubRegex sub = coreSubRegexes.get(0);
        Pattern subPattern = sub.regexPattern;
         Matcher javaMatcher = subPattern.matcher(fieldValue);
        //System.out.println("matchResult " + sub);
        SubRegex nextSub = coreSubRegexes.get(1);
        Pattern nextPattern = nextSub.regexPattern;
        //Matcher nextJavaMatcher = null;
        Matcher nextJavaMatcher = nextPattern.matcher(fieldValue);

        if(flag == -1){
            nextPattern = nextSub.getReverseSubRegex().regexPattern;

        }else{
            nextJavaMatcher = nextPattern.matcher(fieldValue);
        }
        /** find BC FROM EACH POSITION  of A */

        while (javaMatcher.find()) {
            int start = javaMatcher.start();
            int end = javaMatcher.end();



            if (flag != -1) {
                /**find BC from the end position of A*/
/*
                if (nextJavaMatcher.find(end) && nextJavaMatcher.start() == end) {
                    end = nextJavaMatcher.end();
                    //System.out.println("Matching: " + start + "--" + end + " " +  fieldValue.substring(start, end));
                    matchingResults.add(
                            new Span( start, end, regex, fieldValue.substring(start, end)));
                }
*/
                /**For from A to find reverse (BC) on reverse string*/
                nextPattern = nextSub.getReverseSubRegex().regexPattern;
                StringBuffer sb = new StringBuffer(fieldValue);
                String subFieldValue = sb.reverse().toString();
                //System.out.println(start+"--"+end+" "+fieldValue.substring(start, end));
                System.out.println("subFieldValue: " +" " + nextPattern.toString());
                Matcher subJavaMatcher = nextPattern.matcher(subFieldValue);
                if (subJavaMatcher.find()) {
                    int subStart = subJavaMatcher.start();
                    int subEnd = subJavaMatcher.end();
                    if (subEnd == 0 && start >= subEnd) {
                        start = start - subEnd-1;
                        //System.out.println("Matching: " + start + "--" + end + " " +  fieldValue.substring(start, end));
                        matchingResults.add(
                                new Span( start, end, regex, fieldValue.substring(start, end)));
                    }
                }

            } else {
                //System.out.println("Floor: " + start + "--" + end + " " +  fieldValue.substring(start, end));
                /**reverse string to match reverse sub-regex AB */
/*
                StringBuffer sb = new StringBuffer(fieldValue.substring(0, start));
                String subFieldValue = sb.reverse().toString();
                //System.out.println(start+"--"+end+" "+fieldValue.substring(start, end));
                System.out.println("subFieldValue: " +" " + nextPattern.toString());
                Matcher subJavaMatcher = nextPattern.matcher(subFieldValue);
                if (subJavaMatcher.find()) {
                    int subStart = subJavaMatcher.start();
                    int subEnd = subJavaMatcher.end();
                    if (subStart == 0 && start >= subEnd) {
                        start = start - subEnd-1;
                        //System.out.println("Matching: " + start + "--" + end + " " +  fieldValue.substring(start, end));
                        matchingResults.add(
                                new Span( start, end, regex, fieldValue.substring(start, end)));
                    }
                }


 */


                /**match sub-regex AB from left to right*/
                /*
                //Matcher subJavaMatcher = nextPattern.matcher(fieldValue);

                while(nextJavaMatcher.find()){

                    if(nextJavaMatcher.end() == start){
                        //System.out.println("Matching: " + nextJavaMatcher.start() + "--" + end + " " +  fieldValue.substring(nextJavaMatcher.start(), end));
                        matchingResults.add(
                                new Span( nextJavaMatcher.start(), end, regex, fieldValue.substring(nextJavaMatcher.start(), end)));
                    }
                }



                 */





            }
        }

        //System.out.println("matchResult " + matchingResults.size());
        return matchingResults;
    }

    /**
     * FOR A->B->C and C->B->A
     * @param flag
     * @return
     */

    public  List<Span> computeMatchingResultsWithPattenABC(int flag) {
        //System.out.println("3 computeMatchingResultsWithPatterCoreSubregexes :"+ fieldValue);
        List<Span> matchingResults = new ArrayList<>();
        SubRegex sub = coreSubRegexes.get(0);
        Pattern patternA = sub.regexPattern;
        Matcher javaMatcherA = patternA.matcher(fieldValue);
        //List<Span> matchingResultsA = computeMatchingResultsWithPattern(fieldValue, sub.regex, sub.regexPattern);

        //System.out.println("matchResult " + sub);
        SubRegex subB = coreSubRegexes.get(1);
        Pattern patternB = subB.regexPattern;
        Matcher javaMatcherB = null;

        SubRegex subC = coreSubRegexes.get(2);
        Pattern patternC = subC.regexPattern;
        Matcher javaMatcherC = null;

        if(flag == -1){
            patternB = subB.getReverseSubRegex().regexPattern;
            patternC = subC.getReverseSubRegex().regexPattern;

        }else{
            javaMatcherB = patternB.matcher(fieldValue);
            javaMatcherC = patternC.matcher(fieldValue);
        }


        /** find next B with matchingList of A or C
         for(int i=0; i<matchingResultsA.size();i++){
         Span span = matchingResultsA.get(i);
         int start = span.getStart();
         int end = span.getEnd();
         */
        // find next B from each matching position A or C
        while (javaMatcherA.find()){
            int start = javaMatcherA.start();
            int end = javaMatcherA.end();

            if(flag != -1) {
                //find B from the end position of A
                if (javaMatcherB.find(end) && javaMatcherB.start() == end) {
                    int endB = javaMatcherB.end();
                    // find C from B
                    if (javaMatcherC.find(endB) && javaMatcherC.start() == endB) {
                        int endC = javaMatcherC.end();
                        //System.out.println("Matching: " + start + "--" + endC + " " + fieldValue.substring(start, endC));
                        matchingResults.add(
                                new Span( start, endC, regex, fieldValue.substring(start, endC)));

                    }
                }
            }
            else {
                //System.out.println("Floor: " + start + "--" + end + " " +  fieldValue.substring(start, end));
                //reverse string to match reverse sub-regex B
                StringBuffer sb = new StringBuffer(fieldValue.substring(0, start));
                String reverseFieldValue = sb.reverse().toString();

                //System.out.println(start+"--"+end+" "+fieldValue.substring(start, end));
                // System.out.println("subFieldValue: " +" " + nextPattern.toString());
                javaMatcherB = patternB.matcher(reverseFieldValue);
                javaMatcherC = patternC.matcher(reverseFieldValue);
                // find B
                if(javaMatcherB.find() && javaMatcherB.start() == 0){

                    int startB = javaMatcherB.start();
                    int endB = javaMatcherB.end();
                    //System.out.println(startB+"--"+endB+" "+fieldValue.substring(start-endB, start));
                    // find A
                    if(javaMatcherC.find(endB) && javaMatcherC.start() == endB) {
                        //System.out.println(javaMatcherC.start()+"--"+endB);

                        int startC = start-javaMatcherC.end();
                        //System.out.println("Matching: " + javaMatcherC.start() + "--" + javaMatcherC.end() + " " +  reverseFieldValue.substring(javaMatcherC.start(), javaMatcherC.end()));
                        matchingResults.add(
                                new Span( startC, end, regex, fieldValue.substring(startC, end)));
                    }

                }
            }
        }

        //System.out.println("matchResult " + matchingResults.size());
        return matchingResults;
    }

    /**
     * FOR Al + Bl + Cl or Cl + Bl + Al

     * @return
     */
    public  List<Span> compyteMatchingResultsWithABCList(){
        List<Span> matchingResults = new ArrayList<>();
        // 5. matching every subregex and merge the matchingResultsList

        //TreeMap<Pair<Double, Double>, Pair<SubRegex, Integer>> subMatchMap = new TreeMap<>();

        //int tupleSize = fieldValue.length();
        //List<Long> timeList = new ArrayList<>();
        //System.out.println("Tuplesize: " + tupleSize);
        //long startTime = 0;
        //long endTime = 0;
        //Pattern subRegexPattern;
        SubRegex sub = coreSubRegexes.get(0);
        matchingResults = computeMatchingResultsWithPattern(sub.regexPattern);


        //System.out.println("coreSubRegexes : " + coreSubRegexes.size());
        for (int i = 1; i < coreSubRegexes.size(); i++) {

            //subRegexPattern = coreSubPatterns.get(i);
            if(matchingResults.isEmpty()) break;

            SubRegex nextSub = coreSubRegexes.get(i);

            //System.out.println("coreSubRegexes : " + sub+ "size : " + matchingResults.size());
            List<Span> currentResults = computeMatchingResultsWithPattern(nextSub.regexPattern);
            if(currentResults.isEmpty()) {
                matchingResults.clear();
                break;
            }

            /* FOR Statistics
            endTime = System.currentTimeMillis();

            Double time = (endTime - startTime) * 1.0;

            if (currentResults.size() == 0) {
                sub.stats.addStatsSubRegexFailure(time, currentResults.size());
            } else
                sub.stats.addStatsSubRegexSuccess(time, currentResults.size());

            sub.selectivity = sub.stats.getSelectivity();
            sub.selectivity = currentResults.size() * 1.0 / tupleSize;




            //sub.stats.addStatsSubRegexSuccess(matchTime, currentResults.size());
            Pair<Double, Double> matchMap1 = new MutablePair<>(sub.selectivity, time);
            Pair<SubRegex, Integer> matchMap2 = new MutablePair<>(sub, currentResults.size());
            subMatchMap.put(matchMap1, matchMap2);

            //System.out.println("Warmup: "+ currentResults.size() + sub + " selectivity:  "+ sub.selectivity  + " time: " + time);

             */
            //if (i == 0) matchingResults = currentResults;
            //System.out.println("  occ  " + currentResults.size());
            //System.out.println(nextSub.originalSubId + "subregex : " + sub.originalSubId);
            if(sub.originalSubId > nextSub.originalSubId){
                matchingResults = computeSpanIntersection(currentResults, matchingResults);
            }else
                matchingResults = computeSpanIntersection(matchingResults, currentResults);

            sub = nextSub;
            //System.out.println("matchingResultsSize= " + matchingResults.size());

        }
        return  matchingResults;
    }

    /**
     * FOR Al + Bl + Cv
     * @return
     */
    public  List<Span> compyteMatchingResultsWithABListC(){
        boolean flag = true;
        SubRegex sub = coreSubRegexes.get(0);
        List<Span> matchingResults = new ArrayList<>();
        List<Span> intersectionResults = computeMatchingResultsWithPattern(sub.regexPattern);


        //System.out.println("coreSubRegexes : " + sub);

        if(intersectionResults.isEmpty())
            return matchingResults;

        SubRegex nextSub = coreSubRegexes.get(1);

        //System.out.println("coreSubRegexes : " + sub+ "size : " + matchingResults.size());
        List<Span> currentResults = computeMatchingResultsWithPattern(nextSub.regexPattern);
        if(currentResults.isEmpty()) {
            return  matchingResults;
        }

        //System.out.println("  occ  " + currentResults.size());
        //System.out.println(nextSub.originalSubId + "subregex : " + sub.originalSubId);

        if(sub.originalSubId > nextSub.originalSubId){
            flag = false;
            intersectionResults = computeSpanIntersection(currentResults, intersectionResults);

        }else {

            intersectionResults = computeSpanIntersection(intersectionResults, currentResults);

        }
        if(intersectionResults.isEmpty())
            return matchingResults;

        //System.out.println(flag+ "  occ  " + intersectionResults.size());
        Pattern pattern = pattern = coreSubRegexes.get(2).regexPattern;
        Matcher javaMatcher = javaMatcher =  pattern.matcher(fieldValue);

        if(!flag)
            pattern = coreSubRegexes.get(2).reverseSubRegex.regexPattern;


        //System.out.println(flag+ "  pattern " + pattern.toString());
        for(int i=0; i<intersectionResults.size();i++){
            Span span = intersectionResults.get(i);
            int start = span.getStart();
            int end = span.getEnd();
            if(flag){
                if(javaMatcher.find(end)){
                    if(javaMatcher.start() == end) {
                        //System.out.println("Matching: " + start+ "--" + javaMatcher.end() + " " +  fieldValue.substring(start, javaMatcher.end()));

                        matchingResults.add(
                                new Span(start, javaMatcher.end(), regex, fieldValue.substring(start, javaMatcher.end())));
                    }
                }
            }
            else{
                StringBuffer sb = new StringBuffer(fieldValue.substring(0,start));
                String revFieldValue = sb.reverse().toString();
                javaMatcher =  pattern.matcher(revFieldValue);
                if(javaMatcher.find()){
                    int subStart = javaMatcher.start();
                    int subEnd = javaMatcher.end();
                    if (subStart == 0 && start >= subEnd) {
                        start = start - subEnd-1;
                        //System.out.println("Matching: " + start + "--" + end + " " +  fieldValue.substring(start, end));
                        matchingResults.add(
                                new Span( start, end, regex, fieldValue.substring(start, end)));
                    }
                }

            }

        }


        //System.out.println("matchingResultsSize= " + matchingResults.size());

        return  matchingResults;
    }

    /**
     * Matching regex with  AC OR CA to verify the occurrences of B*.

     * @return
     */

    public List<Span> computeMatchingResultsWithPatternApCpBv(){
        List<Span> matchingResults = new ArrayList<>();
        // Subregex A
        SubRegex startSubRegex = coreSubRegexes.get(0);
        // Subregex C
        SubRegex endSubRegex = coreSubRegexes.get(1);
        //// Subregex B
        List<SubRegex> subStars = new ArrayList<>();
        subStars.add(coreSubRegexes.get(2));

        boolean flag = true;
        if(startSubRegex.originalSubId > endSubRegex.originalSubId) {
            flag = false;
            //SubRegex curr = startSubRegex;
            //startSubRegex = endSubRegex;
            //endSubRegex = curr;

        }
        StringBuffer sb = new StringBuffer(fieldValue);
        String reverseFieldValue = sb.reverse().toString();


        String starstring = combineStars(subStars, true);
        //System.out.println("starstring : "+ starstring);
        Pattern pattern = Pattern.compile(starstring);
        Matcher starMatcher = pattern.matcher(fieldValue);

        //List<Span> startSubResults = new ArrayList<>();

        Pattern startPattern = startSubRegex.regexPattern;
        Matcher startJavaMatcher = startPattern.matcher(fieldValue);

        Pattern endPattern = endSubRegex.regexPattern;
        Matcher endJavaMatcher = endPattern.matcher(fieldValue);

        //C OR A
        //int updateStart = 0;
        while (startJavaMatcher.find()) {
            int start = startJavaMatcher.start();
            int end = startJavaMatcher.end();
            //System.out.println("resultcC：" + start + " " + fieldValue.substring(start, end));
            if(!flag){
                //matching reverse A

                endPattern = endSubRegex.reverseSubRegex.regexPattern;
                //System.out.println("endPattern : "+ endPattern.toString());
                endJavaMatcher = endPattern.matcher(reverseFieldValue);

                if(endJavaMatcher.find(fieldValue.length()-start)) {
                    int nextStart = endJavaMatcher.start();
                    int nextEnd = endJavaMatcher.end();
                    //matchingResults.add(
                    //new Span("content", fieldValue.length()-nextStart, end, regex.getRegex(), fieldValue.substring(fieldValue.length()-nextStart, end)));

                    //System.out.println("resultA：" +  nextEnd + " "  + fieldValue.substring(fieldValue.length()-nextStart, start));
                    starMatcher.reset(fieldValue.substring(fieldValue.length()-nextStart, start));
                    //verify B
                    if(starMatcher.matches()){
                        matchingResults.add(
                                new Span(fieldValue.length()-nextEnd, end, regex, fieldValue.substring(fieldValue.length()-nextEnd, end)));

                    }


                // matching A from left to right
                /*
                endJavaMatcher.reset(fieldValue.substring(0,start));
                while(endJavaMatcher.find()) {
                    int endSubS = endJavaMatcher.start();
                    int endSubE = endJavaMatcher.end();
                    //System.out.println(endSubS+"--" + endSubE +" resultA=：" +  fieldValue.substring(endSubS, endSubE+2));


                    starMatcher.reset(fieldValue.substring(endSubE, start));
                    //System.out.println(" resultB=：" +  fieldValue.substring(endSubS, endSubE+2));

                    //verify B
                    if(starMatcher.matches()){
                        //System.out.println(" result=：" +  fieldValue.substring(endSubS, end));
                        matchingResults.add(
                                new Span(endSubS, end, regex, fieldValue.substring(endSubS, end)));

                    }

                }

                 */
                    /*
                    // reverse B*
                    starMatcher.reset(reverseField);
                    if (starMatcher.find(fieldValue.length()-end)) {
                        //System.out.println("resultC：" +(starMatcher.start()-1) + " " + starMatcher.end());
                        //System.out.println("resultC：" +(fieldValue.length()-start) + " " + nextStart);
                        int finalStart = reverseFieldValue.length() - nextEnd;
                        if (starMatcher.end() == nextStart && (fieldValue.length() - start) == starMatcher.start()) {
                            //System.out.println("result：" + fieldValue.substring(finalStart, end));
                            matchingResults.add(
                                    new Span("content", finalStart, end, regex.getRegex(), fieldValue.substring(finalStart, end)));
                        } else if (starMatcher.end() == nextStart && (fieldValue.length() - start) == (starMatcher.start() - 1)) {
                            matchingResults.add(
                                    new Span("content", finalStart, end, regex.getRegex(), fieldValue.substring(finalStart, end)));
                        }

                    }


                     */


                }

            }else {

                int updateEnd = end;
                while(endJavaMatcher.find(updateEnd)){
                    int nextStart = endJavaMatcher.start();
                    int nextEnd = endJavaMatcher.end();
                    //System.out.println("resultA：" +  nextEnd + " "  + fieldValue.substring(nextStart, nextEnd));
                    //B*
                    if (starMatcher.find(end)) {
                        //System.out.println("resultB：" + fieldValue.substring(starMatcher.start(), starMatcher.end()));
                        //System.out.println("start B：" + starMatcher.start() + " == " + end + " end " + starMatcher.end() + " == " + nextStart);
                        if (starMatcher.start() == end) {
                            if (starMatcher.end() == nextStart) {
                                //System.out.println("result：" + fieldValue.substring(start, nextEnd));
                                matchingResults.add(
                                        new Span( start, nextEnd, regex, fieldValue.substring(start, nextEnd)));
                                //Next C
                            }
                            updateEnd = nextEnd;


                        } else break;
                    } else break;
                }



            }


        }
        return  matchingResults;
    }
    /**
     * position List A and position List C to verfy B or find positions of B
     * CAB/ ACB
     * @return
     */
    public List<Span> computeMatchingResultsWithPatternAlClBv(){

        SubRegex sub = coreSubRegexes.get(0);
        List<Span> matchingResults = computeMatchingResultsWithPattern(sub.regexPattern);
        new ArrayList<>();

        SubRegex nextSub = coreSubRegexes.get(1);
        Pattern nextSubRegexPattern = nextSub.regexPattern;
        List<Span> currentResults = computeMatchingResultsWithPattern(nextSubRegexPattern);

        SubRegex starSub = coreSubRegexes.get(2);
        if(Math.abs(sub.originalSubId-nextSub.originalSubId) == 1){
            // AB
            matchingResults = computeSpanIntersection(currentResults, matchingResults);
            //BA
        }
        // ACB*
        else if(sub.originalSubId > nextSub.originalSubId+1 ){
            matchingResults = computeSpanIntersection(currentResults, matchingResults, starSub);
            //System.out.println("matchingResultsSize= " + matchingResults.size());
        }
        //CAB*
        else  matchingResults = computeSpanIntersection(matchingResults, currentResults, starSub);


        return matchingResults;
    }

    private String combineStars(List<SubRegex> subSTARs, boolean flag) {
        String starString = "";
        //Pattern subSTARPattern ;
        for (SubRegex sub : subSTARs) {
            if (flag) {
                starString += sub.regexPattern.toString();
            } else {
                starString += sub.reverseSubRegex.regexPattern.toString();
            }
        }
        //System.out.println("starString: " + starString);
        // subSTARPattern = Pattern.compile(starString);

        return starString;


    }



    /**
     * by Yutong Han
     * intersection of two matching results for two sub-regexes, which R=AB A.end = B.start
     * @param matchingResults
     * @param currentResults
     * @return finalResults
     */
    private List<Span> computeSpanIntersectionSortJoin(List<Span> matchingResults, List<Span> currentResults){
        List<Span> mergeResults = new ArrayList<>();
        for (int i  = 0; i < matchingResults.size();) {
            for (int j = 0; j < currentResults.size();) {
                Span span = matchingResults.get(i);
                Span curSpan = currentResults.get(j);

                if(span.getEnd() >  curSpan.getStart()){
                    j++;

                }else if(span.getEnd() < curSpan.getStart()){
                    j++;
                }else {
                    i++;j++;
                }
            }
        }

                return mergeResults;
    }

    private List<Span> computeSpanIntersection(List<Span> matchingResults, List<Span> currentResults){
        List<Span> finalResults = new ArrayList<>();

        System.out.println("2 computeSpanIntersection: "+ currentResults.size()+ matchingResults.size());
        for (int i  = 0; i < matchingResults.size();i++) {
            for (int j  = 0; j < currentResults.size();j++) {
                Span span = matchingResults.get(i);
                Span curSpan = currentResults.get(j);

                if (span.getEnd() == curSpan.getStart()) {
                    String connectRegexPredicate = span.getKey() + curSpan.getKey();
                    int start = span.getStart();
                    int end = curSpan.getEnd();
                    String value = span.getValue()+curSpan.getValue();
                    finalResults.add( new Span( start, end, connectRegexPredicate, value));
                    // System.out.println("Results: "+start+"--"+end+" "+value);
                    break;
                }

            }
        }
        return finalResults;


    }

    private List<Span> computeSpanIntersection(List<Span> matchingResults, List<Span> currentResults, SubRegex starSubRegex){
        List<Span> finalResults = new ArrayList<>();
        Pattern starPattern = starSubRegex.regexPattern;


        // System.out.println("2 computeSpanIntersection: "+ currentResults.size()+matchingResults.size());
        for (int i  = 0; i < matchingResults.size(); i++) {
            for (int j  = 0; j < currentResults.size(); j++) {
                Span span = matchingResults.get(i);
                Span curSpan = currentResults.get(j);
                if (span.getEnd() <= curSpan.getStart()) {
                    Matcher starMatcher = starPattern.matcher(fieldValue.substring(span.getEnd(), curSpan.getStart()));
                    if(starMatcher.matches()) {
                        String connectRegexPredicate = span.getKey() + starSubRegex.regex + curSpan.getKey();
                        int start = span.getStart();
                        int end = curSpan.getEnd();
                        //String value = span.getValue() + curSpan.getValue();
                        finalResults.add(new Span( start, end, connectRegexPredicate, fieldValue.substring(start, end)));
                    }
                    //System.out.println("Results: "+start+"--"+end+" "+value);
                    else break;
                }

            }
        }
        return finalResults;


    }
    private void printAllSubRegexes() {
        System.out.println("Core SubRegexes:");
        for (SubRegex core : coreSubRegexes) {
            System.out.println(core.toString()+" "+ core.getSelectivity() + "  " + core.originalSubId );
            if (core.isSubRegex()) {
                // System.out.println(((SubRegex) core).getReverseSubRegex().toString());
            }
        }
    }

}
