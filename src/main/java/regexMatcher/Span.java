package regexMatcher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class JsonConstants {

    private JsonConstants() { }


    public static final String SPAN_START = "start";
    public static final String SPAN_END = "end";

}

public class Span implements Substring{
    // The start position of the span, which is the offset of the gap before the
    // first character of the span.
    private int start;
    // The end position of the span, which is the offset of the gap after the
    // last character of the span.
    private int end;


    /*
     * Example: Value = "The quick brown fox jumps over the lazy dog" Now the
     * Span for brown should be start = 10 : index Of character 'b' end = 15 :
     * index of character 'n'+ 1 OR start+length Both of then result in same
     * values. tokenOffset = 2 position of word 'brown'
     */
    public static int INVALID_TOKEN_OFFSET = -1;

    @JsonCreator
    public Span(
            @JsonProperty(value = JsonConstants.SPAN_START, required = true)
            int start, 
            @JsonProperty(value = JsonConstants.SPAN_END, required = true)
            int end) {

        this.start = start;
        this.end = end;

    }

    public Span( int start, int end, String key, String value) {
        this(start, end);
    }


    @JsonProperty(value = JsonConstants.SPAN_START)
    public int getStart() {
        return start;
    }

    @JsonProperty(value = JsonConstants.SPAN_END)
    public int getEnd() {
        return end;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + end;
        result = prime * result + start;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Span other = (Span) obj;


        if (start != other.start)
            return false;

        if (end != other.end)
            return false;


        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("start: " + this.getStart() + "\n");
        sb.append("end:   " + this.getEnd() + "\n");

        return sb.toString();
    }
}
