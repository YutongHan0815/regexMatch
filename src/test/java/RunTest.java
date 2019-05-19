
import fileScan.FileReader;
import regexMatcher.RegexMatcher;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RunTest {


    public static void main(String[] args) {

        try {
              String HEADER = "Date, ID, time";
              String delimiter = ",";

            //Path tempFolderPath = Paths.get("~/Documents/GitHub/data/sample_data_files");
            Path tempFolderPath = Paths.get("/Users/yutonghan/Documents/GitHub/data/sample_data_files/");
            Path queryResultPath = Paths.get("./src/test/preftest/results/test.csv");

            Path tempFilePath = tempFolderPath.resolve("sampleTwitters14M.json");
            // Path tempFilePath = tempFolderPath.resolve("sample_twitter_10K.json");
            //Path tempFilePath = tempFolderPath.resolve("sample_twitter_5.json")
             //Path tempFilePath = tempFolderPath.resolve("sample_twitter_5e.json");
            PerfTestUtils.createFile(queryResultPath, HEADER);
            BufferedWriter fileWriter = Files.newBufferedWriter(queryResultPath);

            FileReader fileReader = new FileReader();

            fileReader.openFile(tempFilePath);

            //List<String> regexQueries = Arrays.asList("[A-Z][aeiou|AEIOU][A-Za-z]*", "mosquitos?", "v[ir]{2}[us]{2}", "market(ing)?",
            //"medic(ine|al|ation|are|aid)?");
            // "(\d[4}](\-|\.)\d[1-12](\-|\.)\d[1-31])(.*)(Subject：PRO/AH/EDR.?Zika)\s(virus)(.?.?.?)(Brazil|Dominican\sRepublic)");
            //List<String> regexQueries = Arrays.asList("\\s(Feb|Jun|May|Sep)(\\s|,)[1-9]");
            //List<String> regexQueries = Arrays.asList("medic(ine|al|ation|are|aid)?");
            //List<String> regexQueries = Arrays.asList("(http|ftp)://(\\w+\\.)edu:\\d{1,5}");
            //List<String> regexQueries = Arrays.asList("www\\.(lib|isid|eurosurveillance)(\\.org\\/ViewArticle)*\\.(jpg|aspx)");

            //List<String> regexQueries = Arrays.asList("(\\.\\.)(Feb|Jun|May|Sep|Jul)\\s\\d[0-9]");
            //List<String> regexQueries = Arrays.asList("(http:|ftp:)(/)*twitter\\.com");
            //List<String> regexQueries = Arrays.asList("profile(_backgr|_sideba)(ound_im|r_borde)");
            //List<String> regexQueries = Arrays.asList("(_backgr|_sideba)(ound_im|r_borde)age_url");
            // List<String> regexQueries = Arrays.asList("(blood )(cells |sinus |pressu|serum )re eff");
            //List<String> regexQueries = Arrays.asList("\\b(?:[ABCEGHJKLMNPRSTVXY]\\d[ABCEGHJKLMNPRSTVWXYZ])\\s?(?:\\d[ABCEGHJKLMNPRSTVWXYZ]\\d)\\b");
            //List<String> regexQueries = Arrays.asList("(blood |these )(cells |sinus |pressu|serum )re eff");
            //List<String> regexQueries = Arrays.asList("Jordan (Levitan|Knight'|Jacobso|crying )*face is");
            //List<String> regexQueries = Arrays.asList("Jordan (Le|Knight'|Jacobso)vitan");
            //List<String> regexQueries = Arrays.asList("[0-9]{5}(-[0-9]{4})*[h-z]{5}");
            //List<String> regexQueries = Arrays.asList("re(ab|bc)*tweet(cd|ef)* Bar");
            //List<String> regexQueries = Arrays.asList("tweet");



            //List<String> regexQueries = Arrays.asList("www\\.(lib|isid|eurosurveillance)(.)*\\.(jpg|aspx)");

            //List<String> regexQueries = Arrays.asList("jpeg", "https?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.");


            // IP Address
            //List<String> regexQueries = Arrays.asList("(([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3}))(\\.[0-9]{1,3})");

            // Image links
            //List<String> regexQueries = Arrays.asList("http(s?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.)(bmp|gif)");
            //List<String> regexQueries = Arrays.asList("https?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.(bmp|png|jpg|gif)");
            //List<String> regexQueries = Arrays.asList("https?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.(bmp|gif)");
            //List<String> regexQueries = Arrays.asList("(https?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.)(bmp|gif)", "(https?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.)(bmp|gif)");

            //List<String> regexQueries = Arrays.asList("(https?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.)jpeg", "(https?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.)jpeg");
            //List<String> regexQueries = Arrays.asList("https?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.jpeg");
            //List<String> regexQueries = Arrays.asList("(https?:\\/\\/[\\w\\.\\/]+\\/[\\w\\.\\/]+\\.)jpeg");
            //Address
            //List<String> regexQueries = Arrays.asList("([0-9]+)((?:st|nd|rd|th)\\s?)(avenue|street|St|Precinct|Ave|Floor)");
            //List<String> regexQueries = Arrays.asList("([0-9]+)((?:st|nd|rd|th)\\s?)Floor");
           List<String> regexQueries = Arrays.asList("[0-9]+(?:st|nd|rd|th)\\s?Floor");
            // List<String> regexQueries = Arrays.asList("http(s?\\:\\/\\/[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,7}(?:\\/[\\w\\-]+)*\\.)jpeg");

            //Image files jpg and gif
            //List<String> regexQueries = Arrays.asList("[0-9A-Za-z_ ]+(\\.[jJ][pP][gG]|\\.[gG][iI][fF])");

            //Date
            //List<String> regexQueries = Arrays.asList("(Nov|Jun|Sep|April)(\\s(0[1-9]|[12][0-9]|30))");


            //Titles
            //List<String> regexQueries = Arrays.asList("Dr[.]?|Phd[.]?|MBA\\s");



            // List<String> regexQueries = Arrays.asList("((http|https|ftp)\\:\\/\\/)((((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])|([a-zA-Z0-9_\\-\\.])+\\.(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum|uk|me)))");

            //List<String> regexQueries = Arrays.asList("((19|20)[0-9]{2}-)(((01|03|05|07|08|10|12)-(0[1-9]|[12][0-9]|3[01]))|(02-(0[1-9]|[12][0-9]))|((04|06|09|11)-(0[1-9]|[12][0-9]|30)))");

            // List<String> regexQueries = Arrays.asList("([1-9]|1[0-2]|0[1-9]){1}(:[0-5][0-9][aApP][mM]){1}");


            //List<String> regexQueries = Arrays.asList("(\\w+([-+\\.']\\w+)*)@gmail\\.com");
            //List<String> regexQueries = Arrays.asList("(\\d{2}\\s{1})((Jan|Feb|Mar|Apr|May|Jun|Jul|Apr|Sep|Oct|Nov|Dec)\\s{1}\\d{4})");

            //List<String> regexQueries = Arrays.asList("[0-9]{10}GBR[0-9]{7}[U,M,F]{1}[0-9]{9}");
            //List<String> regexQueries = Arrays.asList("[a-z0-9!$'*+\\-_]+(\\.[a-z0-9!$'*+\\-_]+)*@([a-z0-9]+(-+[a-z0-9]+)*\\.)+([a-z]{2}|aero|arpa|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|travel)");
            //List<String> regexQueries = Arrays.asList("([a-z0-9!$'*+\\-_]+(\\.[a-z0-9!$'*+\\-_]+)*@([a-z0-9]+(-+[a-z0-9]+)*\\.)+)(cat|com|coop|edu|gov)");

            // List<String> regexQueries = Arrays.asList("([A-Z0-9<]{9}[0-9]{1}[A-Z]{3}[0-9]{7}[A-Z]{1})([0-9]{7}[A-Z0-9<]{14}[0-9]{2})");
            /**Regex from regexlib.txt
             * */
            //List<String> regexQueries = Arrays.asList("((FR)?\\s?)([A-Z0-9-[IO]]{2}[0-9]{9})");
            //List<String> regexQueries = Arrays.asList("\\d{4}[\\-\\/\\s]?((((0[13578])|(1[02]))[\\-\\/\\s]?(([0-2][0-9])|(3[01])))|(((0[469])|(11))[\\-\\/\\s]?(([0-2][0-9])|(30)))|(02[\\-\\/\\s]?[0-2][0-9]))");
            //List<String> regexQueries = Arrays.asList("(\\-?[1-9])((?:\\.\\d+)?[Ee][-+]?\\d+)");
            //List<String> regexQueries = Arrays.asList("([( ]?\\d{1,3})([ )]?[ -]?\\d{3}[ -]?\\d{4})");
            //List<String> regexQueries = Arrays.asList("(([1-9],)?([0-9]{3},){0,3}[0-9]{3}|[0-9]{0,16})(\\.[0-9]{0,3})");
            //List<String> regexQueries = Arrays.asList("(([0]?[1-9]|[1][0-2])[./-]([0]?[1-9]|[1|2][0-9]|[3][0|1]))([./-]([0-9]{4}|[0-9]{2}))");
            //List<String> regexQueries = Arrays.asList("(([0-9]{1})|([0-9]{1}[0-9]{1})|([1-3]{1}[0-6]{1}[0-5]{1}))(d(([0-9]{1})|(1[0-9]{1})|([1-2]{1}[0-3]{1}))h(([0-9]{1})|([1-5]{1}[0-9]{1}))m)");
            //List<String> regexQueries = Arrays.asList("(([0]?[1-9])|(1[0-2]))(\\/(([0]?[1-9])|([1,2]\\d{1})|([3][0,1]))\\/[12]\\d{3})");
            //List<String> regexQueries = Arrays.asList("(714|760|949|619|909|951|818|310|323|213|323|562|626)(-\\d{3}-\\d{4})");
            //List<String> regexQueries = Arrays.asList("(@\"^\\d[a-zA-Z]\\w{1})(\\d{2}[a-zA-Z]\\w{1}\\d{3})");
            //List<String> regexQueries = Arrays.asList("\\d{1,3}(((\\,\\d{3})*|(\\d+))(\\.\\d{0,2})?)");
            //List<String> regexQueries = Arrays.asList("(<[iI][mM][gG][a-zA-Z0-9\\s=\".]*)(((src)=\\s*(?:\"([^\"]*)\"|'[^']*'))[a-zA-Z0-9\\s=\".]*/*>(?:</[iI][mM][gG]>)*)");
            //List<String> regexQueries = Arrays.asList("([ABCEGHJKLMNPRSTVXYabceghjklmnprstvxy]{1})(\\d{1}[A-Za-z]{1}[ ]{0,1}\\d{1}[A-Za-z]{1}\\d{1})");
            //List<String> regexQueries = Arrays.asList("(href=|url|import).*[\\'\"]([^(http:)].*css)[\\'\"]");
            //List<String> regexQueries = Arrays.asList("([A-Z]{1}\\w{1,3})( (\\d{1}[A-Z]{2}))");
            //List<String> regexQueries = Arrays.asList("(49030)([2-9](\\d{10}$|\\d{12,13}))");
            //List<String> regexQueries = Arrays.asList("(\"(\\\\[\"\\\\]|[^\"])*)((\"|$)|'(\\\\['\\\\]|[^'])*('|$)|(\\\\[\"'\\\\]|[^\\s\"'])+/g)");
            //List<String> regexQueries = Arrays.asList("");

            /**
             * Regex from https://projects.lukehaas.me/regexhub/
             */
            //match dates with dashes, slashes or with spaces (e.g. dd-mm-yyyy dd/mm/yyyy dd mm yyyy), and optional time separated by a space or a dash (e.g. dd-mm-yyyy-hh:mm:ss or dd/mm/yyyy hh:mm:ss).
            //List<String> regexQueries = Arrays.asList("((0?[1-9]|[12][0-9]|3[01])([ \\/\\-])(0?[1-9]|1[012])(\\)2([0-9][0-9][0-9][0-9]))(([ -])([0-1]?[0-9]|2[0-3]):[0-5]?[0-9]:[0-5]?[0-9])?)");
            //times in 24 hour format
            //List<String> regexQueries = Arrays.asList("([01]?[0-9]|2[0-3]):[0-5][0-9]");

            //a valid date and times in the ISO-8601 format, excludes durations.
            //List<String> regexQueries = Arrays.asList("(([+-]?\\d{4,5}-?(?:\\d{2}|W\\d{2})T)(?:|(\\d{4}|[+-]\\d{5})-?)(?:|(0\\d|1[0-2])(?:|-?([0-2]\\d|3[0-1]))|([0-2]\\d{2}|3[0-5]\\d|36[0-6])|W([0-4]\\d|5[0-3])(?:\\d|-?([1-7])))(?:(\\d)|T(\\d)))(?:|([01]\\d|2[0-4])(?:|:?([0-5]\\d)(?:|:?([0-5]\\d)(?:\\d|\\.(\\d{3})))(?:|[zZ]|([+-](?:[01]\\d|2[0-4]))(?:|:?([0-5]\\d)))))");

            //RGB HEX color value
            //List<String> regexQueries = Arrays.asList("(#?)([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");

            //the src attribute of an HTML image tag
            //List<String> regexQueries = Arrays.asList("(<\\s*img[a>]+src\\s*)(=\\s*([\"'])(.*?)\\\\d[^>]*>)");
            //url
            //List<String> regexQueries = Arrays.asList("(((https?|ftp|file):\\/\\/)?)(([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?)");
            //ipv4 address
            //List<String> regexQueries = Arrays.asList("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
            //IP v6 addresses
            ///List<String> regexQueries = Arrays.asList("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))");
            //List<String> regexQueries = Arrays.asList("");
            //List<String> regexQueries = Arrays.asList("");
            //positive or negative number
            //List<String> regexQueries = Arrays.asList("(-?\\d*\\.?)(\\d+)");
            //phone number
            //List<String> regexQueries = Arrays.asList("(\\+?)(\\d.*){3,}");
            // id for youtube channel
            //List<String> regexQueries = Arrays.asList("(https?:\\/\\/(www\\.)?youtube.com\\/)(channel\\/UC([-_a-z0-9]{22}))");
            //css comment
            //List<String> regexQueries = Arrays.asList("(\\/\\\\/\\*[^*]*\\*+([^\\\\/*][^*]*\\*+)*)(\\\\/\\/)");

            //brazilian zip/postal code
            //List<String> regexQueries = Arrays.asList("([0-9]{5})(-[0-9]{3})");

            //IMAGE short code
            //List<String> regexQueries = Arrays.asList("(\\/\\[img\\](.*?))(\\[\\\\/img\\])");
            /**Regex from Regular Expression Lib*/
            //EMAIL
            //List<String> regexQueries = Arrays.asList("(.+@[^\\.].*)(\\.[a-z]{2,})");
            //List<String> regexQueries = Arrays.asList("(([\\w\\d\\-\\.]+)@{1}(([\\w\\d\\-]{1,67})|([\\w\\d\\-]+\\.[\\w\\d\\-]{1,67})))(\\.(([a-zA-Z\\d]{2,4})(\\.[a-zA-Z\\d]{2})?)\\t)");
            //List<String> regexQueries = Arrays.asList("(\\w[-._\\w]*)(\\w@\\w[-._\\w]*\\w\\.\\w{2,3}\\t)");
            //List<String> regexQueries = Arrays.asList("[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})");
            //URL
            //List<String> regexQueries = Arrays.asList("(((?:2[0-5]{2}|1\\d{2}|[1-9]\\d|[1-9])\\.(?:(?:2[0-5]{2}|1\\d{2}|[1-9]\\d|\\d)\\.){2}(?:2[0-5]{2}|1\\d{2}|[1-9]\\d|\\d)))(:(\\d|[1-9]\\d|[1-9]\\d{2,3}|[1-5]\\d{4}|6[0-4]\\d{3}|654\\d{2}|655[0-2]\\d|6553[0-5])\\t)");
            //List<String> regexQueries = Arrays.asList("([a-zA-Z]\\:|\\\\\\\\[^\\/\\\\:*?\"<>|]+\\\\[^\\/\\\\:*?\"<>|]+)(\\\\[^\\/\\\\:*?\"<>|]+)+(\\.[^\\/\\\\:*?\"<>|]+)\\t");
            //List<String> regexQueries = Arrays.asList("((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0))(\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])\\t)");
            //List<String> regexQueries = Arrays.asList("((((ht|f)tp(s?))\\://)?)(((([a-zA-Z0-9_\\-]{2,}\\.)+[a-zA-Z]{2,})|((?:(?:25[0-5]|2[0-4]\\d|[01]\\d\\d|\\d?\\d)(\\.?\\d)\\.){4}))(:[a-zA-Z0-9]+)?(/[a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~]*)?)");
            //String
            //List<String> regexQueries = Arrays.asList("(((4\\d{3})|(5[1-5]\\d{2})|(6011))-?\\d{4}-?\\d{4}-?)(\\d{4}|3[4,7]\\d{13}\\t)");
            //List<String> regexQueries = Arrays.asList("(ISBN\\x20(.{13}$)\\d{1,5}([- ]))(\\d{1,7}\\\\d\\d{1,6}\\\\d(\\d|X))");
            //List<String> regexQueries = Arrays.asList("([^(000)]([0-6]\\d{2}|7([0-6]\\d|7[012]))([ -]?)[^(00)])(\\d\\d\\d[^(0000)]\\d{4})");
            //List<String> regexQueries = Arrays.asList("([1-9]{1}(([0-9])?){2})+(,[0-9]{1}[0-9]{2})*");\

            //List<String> regexQueries = Arrays.asList("((?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\\\d|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\\\d))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\\\d(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8]))((\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\\\d(?:(?:1[6-9]|[2-9]\\d)?\\d{2}))");
            //List<String> regexQueries = Arrays.asList("\\d{1,2},\\/\\d{1,2}\\/\\d{4}");
            //List<String> regexQueries = Arrays.asList("([^\\d_].*?\\d)(\\w(\\w|[!@#$%]){7,20})");
            //List<String> regexQueries = Arrays.asList("(^|\\s)(00[1-9]|0[1-9]0|0[1-9][1-9]|[1-6]\\d{2}|7[0-6]\\d|77[0-2])(-?|[\\. ])([1-9]0|0[1-9]|[1-9][1-9])\\3(\\d{3}[1-9]|[1-9]\\d{3}|\\d[1-9]\\d{2}|\\d{2}[1-9]\\d)($|\\s|[;:,!\\.\\?])");

            //MISC
            //List<String> regexQueries = Arrays.asList("(\\+[1-9][0-9]*(\\([0-9]*\\)|-[0-9]*-))?[0]?[1-9][0-9\\- ]*");

            //List<String> regexQueries = Arrays.asList("(\\D?(\\d{3})\\D?\\D?)((\\d{3})\\D?(\\d{4}))");
            //List<String> regexQueries = Arrays.asList("(\\d{2}.?\\d{3}.?)(\\d{3}/?\\d{4}-?\\d{2})");
            //List<String> regexQueries = Arrays.asList("\\d{3}(\\s?\\d{3})");

            //Code
            //List<String> regexQueries = Arrays.asList("(&lt;[^&gt;]*\\n?.*=(&quot;|')?(.*\\.jpg)(&quot;|')?.*\\n?[^&lt;]*)(&gt;)");
            //Numbers
            //List<String> regexQueries = Arrays.asList("[1-9]+[0-9]*");
            //List<String> regexQueries = Arrays.asList("(\\D?)((\\d{3})\\D?\\D?(\\d{3})\\D?(\\d{4}))");
            //List<String> regexQueries = Arrays.asList("\\d{0,2}((\\.\\d{1,2})?)");



            List<String> resultsAB = new ArrayList<>();

            RegexMatcher regexMatcher = new RegexMatcher(regexQueries.get(0));
            regexMatcher.setUp();


            int idCounter = 0;
            while (idCounter < 1) {

                long regexMatchTimeS = System.currentTimeMillis();
                for (int i = 0; i < 100000; i++) {
                    String fieldValue  = fileReader.getNextTuple();

                    //long recordMatchTimeS = System.currentTimeMillis();

                    //int countR = 0;
                   // while(countR <10) {
                        //System.out.println("field :  "+ fieldValue);
                        if (regexMatcher.computeMatchingResult(fieldValue)) {
                            resultsAB.add(fieldValue);

                            //System.out.println("Tuple id ==========="+ i);
                        }
                       // countR++;
                   // }
                    /*
                    long recordMatchTimeE = System.currentTimeMillis();
                    double recordMatchTime = (recordMatchTimeE - recordMatchTimeS) / 1000.0;
                    fileWriter.append(recordMatchTime + delimiter);
                    fileWriter.append("\n");

                     */
                }

                long regexMatchTimeE = System.currentTimeMillis();
                double oneTupleMatchTime = (regexMatchTimeE - regexMatchTimeS) / 1000.0;


                fileWriter.append(idCounter + delimiter);
                fileWriter.append(oneTupleMatchTime + delimiter);
                fileWriter.append("\n");
                // fileWriter1.append(delimiter);
                idCounter++;

            }

            fileReader.close();
            System.out.println("Regex matching is done!");
            System.out.println("Regex matching Results: "+ resultsAB.size());
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

