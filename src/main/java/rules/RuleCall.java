package rules;

import plan.Operator;

public interface RuleCall {

    <T extends Operator> T getMatchedOperator(int ordinal);

    void transformTo( Operator equivalentOperator);

}
