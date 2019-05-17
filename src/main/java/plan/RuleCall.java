package plan;

import rules.TransformationRule;

public interface RuleCall {

    TransformationRule getMatchedRule();

    OperatorNode getMatchedOperator(int ordinal);

    void transformTo(OperatorNode equivalentOperator);

}
