package regexMatcherTest;

import java.util.ArrayList;
import java.util.List;

public class RegexTestConstantsText {

    public static List<String> getTuples() {
        List<String> tuples = new ArrayList<>();
        tuples.add("32nd Floor internationalCenter@gmail.comprofile");
        tuples.add("Bar just asked Jordan 32nd Floor internationalCenter@gmail.comprofile");
        tuples.add("\"profile_image_url\": \"http://pbs.twimg.com/profile_images/862781605018046464/HTg7Kr1y_normal.jpg\", \"lang\": \"en\", \"location\": \"1776 Broadway, 19th Floor\",");
        return tuples;
    }
    public static List<String> getQueries() {
        List<String> queries = new ArrayList<>();
        queries.add("[0-9]+(?:st|nd|rd|th)\\s?Floor");
        queries.add("https?:\\\\/\\\\/[\\\\w\\\\.\\\\/]+\\\\/[\\\\w\\\\.\\\\/]+\\\\.(bmp|png|jpg|gif)");
        return queries;
    }


}
