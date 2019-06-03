package edu.ics.uci.regex.operators;

import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;
import edu.ics.uci.optimizer.operator.Operator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogicalVerifyOperator implements Operator, Serializable {

    private final String subRegex;
    private final Condition condition;

    public LogicalVerifyOperator(String subRegex, Condition condition) {
        this.subRegex = subRegex;
        this.condition = condition;
    }

    public String getSubRegex() {
        return subRegex;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalVerifyOperator that = (LogicalVerifyOperator) o;
        return Objects.equals(subRegex, that.subRegex) &&
                condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subRegex, condition);
    }

    public String getDigest() {
        return "LogicalVerifyOperator(" + subRegex + condition + ")";
    }

    public boolean isComposable() {

        final String regex = this.getSubRegex();
        PublicRegexp re = PublicParser.parse(regex, PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        return re.getOp() == PublicRegexp.PublicOp.CONCAT;
    }

    public List<String> decompose() {
        List<String> subRegexList = new ArrayList<>();

        PublicRegexp re = PublicParser.parse(this.getSubRegex(), PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        if(re.getOp() != PublicRegexp.PublicOp.CONCAT) {

        }
        else {
            PublicRegexp[] subs = re.getSubs();
            String subRegex1 = subs[0].toString();
            String subRegex2 = new String();
            subRegexList.add(subRegex1);
            for(int i=1; i<subs.length; i++) {
                subRegex2 += subs[i].toString();
            }
            subRegexList.add(subRegex2);
        }


        return subRegexList;
    }

}
