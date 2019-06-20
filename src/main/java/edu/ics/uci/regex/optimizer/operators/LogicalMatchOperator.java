package edu.ics.uci.regex.optimizer.operators;


import com.google.re2j.PublicParser;
import com.google.re2j.PublicRE2;
import com.google.re2j.PublicRegexp;
import com.google.re2j.PublicSimplify;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.regex.runtime.regexMatcher.ExecutionOperator;
import edu.ics.uci.regex.runtime.regexMatcher.SubRegex;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class LogicalMatchOperator implements Operator, Serializable {

    //Query regex
    private final SubRegex subRegex;

    //Cluster which the operator belongs to
    //public String opCluster;

    public LogicalMatchOperator(String mainRegexString){
        this.subRegex = new SubRegex(mainRegexString);

    }
    public LogicalMatchOperator(SubRegex mainRegex){
        this.subRegex = mainRegex;

    }


    public SubRegex getSubRegex() {
        return subRegex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalMatchOperator that = (LogicalMatchOperator) o;
        return Objects.equals(subRegex, that.subRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subRegex);
    }
    @Override
    public String getDigest() {
        return "LogicalMatchOperator(" + subRegex.getRegex() + ")";
    }
    @Override
    public ExecutionOperator getExecution() {
        throw new UnsupportedOperationException("a logical match operator is not supported in a query plan!");
    }

    public boolean isComposable() {

        final SubRegex regex = this.getSubRegex();
        PublicRegexp re = PublicParser.parse(regex.getRegex(), PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        return re.getOp() == PublicRegexp.PublicOp.CONCAT;
    }

    public List<SubRegex> decompose() {
        List<SubRegex> subRegexList = new ArrayList<>();

        PublicRegexp re = PublicParser.parse(this.getSubRegex().getRegex(), PublicRE2.PERL);
        re = PublicSimplify.simplify(re);
        if(re.getOp() != PublicRegexp.PublicOp.CONCAT) {

        }
        else {
            PublicRegexp[] subs = re.getSubs();
            SubRegex subRegex1 = new SubRegex(subs[0].toString());
            String suffix = new String();
            subRegexList.add(subRegex1);
            for(int i=1; i<subs.length; i++) {
                suffix += subs[i].toString();
            }
            SubRegex subRegex2 = new SubRegex(suffix);
            subRegexList.add(subRegex2);
        }

        return subRegexList;
    }

}
