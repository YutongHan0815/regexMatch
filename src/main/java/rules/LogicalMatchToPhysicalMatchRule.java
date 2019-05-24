package rules;


import operators.LogicalMatchOperator;
import operators.PhysicalMatchOperator;
import plan.OperatorNode;
import plan.PatternNode;
import plan.rule.RuleCall;
import plan.SubsetNode;
import plan.triat.Convention;

import java.io.Serializable;


public class LogicalMatchToPhysicalMatchRule implements TransformationRule, Serializable {

    public static final LogicalMatchToPhysicalMatchRule INSTANCE = new LogicalMatchToPhysicalMatchRule();
    private final String description;
    private final PatternNode matchPattern;

    public LogicalMatchToPhysicalMatchRule() {

        this.description = this.getClass().getName();
        this.matchPattern = PatternNode.any(LogicalMatchOperator.class);
    }

    public String getDescription() {
        return description;
    }

    @Override
    public PatternNode getMatchPattern() {
        return matchPattern;
    }


    @Override
    public void onMatch(RuleCall ruleCall) {
        final OperatorNode logicalMatchOpN = ruleCall.getMatchedOperator(0);
        final LogicalMatchOperator logicalMatchOperator = logicalMatchOpN.getOperator();

        PhysicalMatchOperator physicalMatchOperator = new PhysicalMatchOperator(logicalMatchOperator.getSubRegex());
        OperatorNode matchOperatorNode = OperatorNode.create(physicalMatchOperator, logicalMatchOpN.getTraitSet().replace(Convention.PHYSICAL));

        ruleCall.transformTo(matchOperatorNode);

    }
}
