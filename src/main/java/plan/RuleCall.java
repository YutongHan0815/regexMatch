package plan;

import operators.Operator;

public interface RuleCall {


    <T extends Operator> T getMatchedOperator(int ordinal);

    void transformTo( Operator equivalentOperator);

    void transformTo(SetNode setNode);


}
