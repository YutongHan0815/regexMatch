package plan;

import operators.Operator;
import rules.TransformationRule;

public interface RuleCall {

    TransformationRule getMatchedRule();

    <T extends Operator> T getMatchedOperator(int ordinal);

    void transformTo(Operator equivalentOperator);

    void transformTo(SetNode setNode);


}
