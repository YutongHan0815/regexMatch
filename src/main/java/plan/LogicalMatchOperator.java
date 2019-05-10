package plan;


import regexMatcher.SubRegex;

import java.io.Serializable;



public class LogicalMatchOperator implements Operator, Serializable {

    //Query regex
    private final String subRegex;

    //Cluster which the operator belongs to
    //public String opCluster;


    public LogicalMatchOperator(String mainRegex){
        this.subRegex = mainRegex;


    }




}
