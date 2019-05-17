package regexMatcher;

import java.io.Serializable;

public class VerifySubRegex implements MatchRegex, Serializable {
    public String subRegex;
    public boolean matchOr;
    public String fieldValue;

}
