package regexMatcherTest;

import java.util.ArrayList;
import java.util.List;

public class RegexTestConstantsText {

    public static List<String> getTuples() {
        List<String> tuples = new ArrayList<>();
        tuples.add(" ");
        tuples.add(" ");
        return tuples;
    }
    public static List<String> getQueries() {
        List<String> queries = new ArrayList<>();
        queries.add("[0-9]+(?:st|nd|rd|th)\\s?Floor");
        queries.add("https?:\\\\/\\\\/[\\\\w\\\\.\\\\/]+\\\\/[\\\\w\\\\.\\\\/]+\\\\.(bmp|png|jpg|gif)");
        return queries;
    }


}
