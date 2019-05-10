package operators;




import regexMatcher.SubRegex;

import java.io.Serializable;



public class PhysicalMatchOperator implements Operator, Serializable {

    private final SubRegex subRegex;
    // match left to right / match reverse regex
    private boolean physicalMatchOptOr;

    //the properties of the regex expression
    private String regexProperties;

    //the cost of this operator
    public int costOpt;

   public PhysicalMatchOperator(SubRegex mainRegex) {
       this.subRegex = mainRegex;
   }

/*
    //matching all the spans on input strings
    public List<Span> match(Operator operator, List<Span> spanList, boolean matchOr){


        return spanList;

    }

 */

}
