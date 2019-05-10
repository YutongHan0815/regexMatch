package rules;

import plan.Operator;

import java.io.Serializable;


public class PatternRuleCall implements RuleCall, Serializable {

    @Override
    public <T extends Operator> T getMatchedOperator(int ordinal) {
        return null;
    }

    @Override
    public void transformTo(Operator equivalentOperator) {


    }
}
