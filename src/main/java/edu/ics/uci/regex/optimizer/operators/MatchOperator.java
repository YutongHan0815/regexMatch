package edu.ics.uci.regex.optimizer.operators;

import com.google.common.base.Preconditions;
import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.schema.Field;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static edu.ics.uci.optimizer.operator.schema.SpanType.SPAN_TYPE;

/**
 * Regex expression that match occurrences of a query sub-regex on tuples
 */
public abstract class MatchOperator implements Operator, Serializable {

    final SubRegex subRegex;

    MatchOperator(SubRegex subRegex) {
        Preconditions.checkNotNull(subRegex);
        this.subRegex = subRegex;
    }

    public SubRegex getSubRegex() {
        return subRegex;
    }

    @Override
    public RowType deriveRowType(List<RowType> inputRowTypeList) {
        Preconditions.checkArgument(inputRowTypeList.isEmpty());
        return RowType.of(Field.of(this.subRegex.getRegex(), SPAN_TYPE));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchOperator that = (MatchOperator) o;
        return Objects.equals(subRegex, that.subRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subRegex);
    }

    @Override
    public String toString() {
        return "MatchOperator{" +
                "subRegex=" + subRegex +
                '}';
    }

    public static boolean isComposable(SubRegex subRegex) {
        PublicRegexp re = PublicParser.parse(subRegex.getRegex(), PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        return re.getOp() == PublicRegexp.PublicOp.CONCAT;
    }

    public static List<SubRegex> decompose(SubRegex subRegex) {
        List<SubRegex> subRegexList = new ArrayList<>();

        PublicRegexp re = PublicParser.parse(subRegex.getRegex(), PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        if(re.getOp() != PublicRegexp.PublicOp.CONCAT) {

        }
        else {
            PublicRegexp[] subs = re.getSubs();
            SubRegex subRegex1 = new SubRegex(subs[0].toString());
            StringBuilder suffix = new StringBuilder();
            subRegexList.add(subRegex1);
            for(int i=1; i<subs.length; i++) {
                suffix.append(subs[i].toString());
            }
            SubRegex subRegex2 = new SubRegex(suffix.toString());
            subRegexList.add(subRegex2);
        }

        return subRegexList;
    }

}
