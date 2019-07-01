package edu.ics.uci.regex.optimizer.rules.logical;

import com.google.common.base.Verify;
import edu.ics.uci.optimizer.OptimizerContext;
import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.operator.schema.Field;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.optimizer.operator.schema.SpanType;
import edu.ics.uci.optimizer.rule.PatternNode;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.regex.optimizer.expression.ExprOperand;
import edu.ics.uci.regex.optimizer.expression.Expression;
import edu.ics.uci.regex.optimizer.expression.SpanInputRef;
import edu.ics.uci.regex.optimizer.expression.SpanInputRef.SpanAccess;
import edu.ics.uci.regex.optimizer.operators.LogicalJoinOperator;
import edu.ics.uci.regex.optimizer.operators.LogicalProjectOperator;
import edu.ics.uci.regex.optimizer.operators.ProjectOperator;
import edu.ics.uci.regex.runtime.regexMatcher.execution.Project;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static edu.ics.uci.optimizer.rule.PatternNode.*;

public class ProjectJoinTransposeRule implements TransformRule, Serializable {

    public static ProjectJoinTransposeRule LEFT_PROJECT = new ProjectJoinTransposeRule(
            operand(LogicalJoinOperator.class).children(exact(
                    operand(LogicalProjectOperator.class).children(any()),
                    operand(Operator.class).children(any()))
            ).build()
    );

    public static ProjectJoinTransposeRule RIGHT_PROJECT = new ProjectJoinTransposeRule(
            operand(LogicalJoinOperator.class).children(exact(
                    operand(Operator.class).children(any()),
                    operand(LogicalProjectOperator.class).children(any()))
            ).build()
    );

    public static ProjectJoinTransposeRule BOTH_PROJECT = new ProjectJoinTransposeRule(
            operand(LogicalJoinOperator.class).children(exact(
                    operand(LogicalProjectOperator.class).children(any()),
                    operand(LogicalProjectOperator.class).children(any()))
            ).build()
    );

    private final String description;
    private final PatternNode matchPattern;

    private ProjectJoinTransposeRule(PatternNode pattern) {
        this.description = this.getClass().getName();
        this.matchPattern = pattern;
    }

    @Override
    public PatternNode getMatchPattern() {
        return this.matchPattern;
    }

    @Override
    public void onMatch(RuleCall ruleCall) {
        OperatorNode joinNode = ruleCall.getOperator(0);
        OperatorNode leftNode = ruleCall.getOperator(1);
        OperatorNode rightNode = ruleCall.getOperator(2);

        OptimizerContext context = ruleCall.getContext();

        LogicalJoinOperator logicalJoin = joinNode.getOperator();
        SubsetNode newLeft = null;
        SubsetNode newRight = null;

        LogicalProjectOperator leftProject = null;
        LogicalProjectOperator rightProject = null;

        int oldLeftColumnCount = leftNode.getOperatorMemo().getOutputRowType().get().getFields().size();
        Integer newLeftColumnCount;

        Map<SpanInputRef, SpanInputRef> inputRefMapping = new HashMap<>();

        if (leftNode.getOperator() instanceof LogicalProjectOperator) {
             leftProject = leftNode.getOperator();
            RowType projectInputRowType = leftNode.getInputs().get(0).getEquivSet().getSetMemo().getOutputRowType().get();
            RowType projectOutputRowType = leftNode.getOperatorMemo().getOutputRowType().get();

            inputRefMapping.putAll(
                    getInputMapping(0, 0, leftProject, projectInputRowType, projectOutputRowType)
            );

            newLeft = leftNode.getInputs().get(0);
            newLeftColumnCount = newLeft.getEquivSet().getSetMemo().getOutputRowType().get().getFields().size();

        } else {
            newLeft = joinNode.getInputs().get(0);
            newLeftColumnCount = oldLeftColumnCount;
        }

        if (rightNode.getOperator() instanceof LogicalProjectOperator) {
             rightProject = rightNode.getOperator();
            RowType projectInputRowType = rightNode.getInputs().get(0).getEquivSet().getSetMemo().getOutputRowType().get();
            RowType projectOutputRowType = rightNode.getOperatorMemo().getOutputRowType().get();

            inputRefMapping.putAll(
                    getInputMapping(oldLeftColumnCount, newLeftColumnCount, rightProject, projectInputRowType, projectOutputRowType)
            );

            newRight = rightNode.getInputs().get(0);
        } else {
            newRight = joinNode.getInputs().get(1);
        }

        ExprOperand transformedExpr = logicalJoin.getCondition().transform(node -> transformJoinExpr(node, inputRefMapping));
        Verify.verify(transformedExpr instanceof Expression);
        LogicalJoinOperator newJoinOperator = new LogicalJoinOperator((Expression) transformedExpr);

        OperatorNode newJoinNode = OperatorNode.create(context, newJoinOperator, joinNode.getTraitSet(), newLeft, newRight);

        OperatorNode newProjectNode = null;
        if ( leftNode.getOperator() instanceof LogicalProjectOperator ) {
            newProjectNode = OperatorNode.create(context, leftNode.getOperator(), leftNode.getTraitSet(), SubsetNode.create(context, newJoinNode));

            if (rightNode.getOperator() instanceof LogicalProjectOperator) {
                newProjectNode = OperatorNode.create(context, rightNode.getOperator(), rightNode.getTraitSet(), SubsetNode.create(context, newProjectNode));
            }
        } else if (rightNode.getOperator() instanceof LogicalProjectOperator) {
            newProjectNode = OperatorNode.create(context, rightNode.getOperator(), rightNode.getTraitSet(), SubsetNode.create(context, newJoinNode));
        }

        ruleCall.transformTo(newProjectNode);
    }

    private static ExprOperand transformJoinExpr(ExprOperand node, Map<SpanInputRef, SpanInputRef> inputMapping) {
        ExprOperand newNode;
        if (node instanceof SpanInputRef) {
            if (inputMapping.containsKey(node)) {
                newNode = inputMapping.get(node);
            } else {
                newNode = node;
            }
        } else if (node instanceof Expression) {
            List<ExprOperand> newOperands = ((Expression) node).getOperands().stream()
                    .map(operand -> operand.transform(op -> transformJoinExpr(op, inputMapping)))
                    .collect(Collectors.toList());
            newNode = ((Expression) node).copyWithNewOperands(newOperands);
        } else {
            newNode = node;
        }
        return newNode;

    }



    public static Map<SpanInputRef, SpanInputRef> getInputMapping(
            int oldStartIndex, int newStartIndex, LogicalProjectOperator project,
            RowType projectInputRowType, RowType projectOutputRowType
    ) {
        Map<SpanInputRef, SpanInputRef> inputMapping = new HashMap<>();
        int combineIndex = oldStartIndex + projectOutputRowType.getFields().size() - 1;

        if(project.getResultIndex() > projectOutputRowType.getFields().size()-1)
            throw new UnsupportedOperationException("resultIndex is out of range");

        for (int i = 0; i < projectInputRowType.getFields().size(); i++) {
            //resultIndex > rightIndex

            if (i == project.getLeftIndex()) {
                inputMapping.put(
                        SpanInputRef.of(oldStartIndex + project.getResultIndex(), SpanAccess.START),
                        SpanInputRef.of(newStartIndex + project.getLeftIndex(), SpanAccess.START)
                );
            } else if (i == project.getRightIndex()) {
                inputMapping.put(
                        SpanInputRef.of(oldStartIndex + project.getResultIndex(), SpanAccess.END),
                        SpanInputRef.of(newStartIndex + project.getRightIndex(), SpanAccess.END)
                );
            } else if (i == project.getResultIndex()) {
                inputMapping.put(
                        SpanInputRef.of(oldStartIndex + project.getLeftIndex(), SpanAccess.START),
                        SpanInputRef.of(newStartIndex + i, SpanAccess.START)
                );
                inputMapping.put(
                        SpanInputRef.of(oldStartIndex + project.getLeftIndex(), SpanAccess.END),
                        SpanInputRef.of(newStartIndex + i, SpanAccess.END)
                );
            } else {
                Integer oldIndex;
                Integer newIndex;
                if (project.getResultIndex() >= project.getRightIndex()) {
                    if (i < project.getLeftIndex()) {
                        oldIndex = oldStartIndex + i;
                        newIndex = newStartIndex + i;
                    } else if (i > project.getLeftIndex() && i < project.getRightIndex()) {
                        oldIndex = oldStartIndex + i - 1;
                        newIndex = newStartIndex + i;
                    } else {
                        oldIndex = oldStartIndex + i - 2;
                        newIndex = newStartIndex + i;
                    }
                } else if (project.getResultIndex() <= project.getLeftIndex()) {
                    //resultIndex < leftIndex
                    if (i < project.getResultIndex()) {
                        oldIndex = oldStartIndex + i;
                        newIndex = newStartIndex + i;
                    } else if (project.getResultIndex() < i && i < project.getLeftIndex()) {
                        oldIndex = oldStartIndex + i - 1;
                        newIndex = newStartIndex + i;
                    } else if (project.getLeftIndex() < i && i < project.getRightIndex()) {
                        oldIndex = oldStartIndex + i;
                        newIndex = newStartIndex + i;
                    } else {
                        oldIndex = oldStartIndex + i - 1;
                        newIndex = newStartIndex + i;
                    }
                } else {
                    //leftIndex < resultIndex < rightIndex

                    if (i < project.getLeftIndex()) {
                        oldIndex = oldStartIndex + i;
                        newIndex = newStartIndex + i;
                    } else if (i > project.getLeftIndex() && i < project.getResultIndex()) {
                        oldIndex = oldStartIndex + i - 1;
                        newIndex = newStartIndex + i;
                    } else if (i > project.getResultIndex() && i < project.getRightIndex()) {
                        oldIndex = oldStartIndex + i;
                        newIndex = newStartIndex + i;
                    } else {
                        oldIndex = oldStartIndex + i - 1;
                        newIndex = newStartIndex + i;
                    }
                }




                inputMapping.put(SpanInputRef.of(oldIndex, SpanAccess.START), SpanInputRef.of(newIndex, SpanAccess.START));
                inputMapping.put(SpanInputRef.of(oldIndex, SpanAccess.END), SpanInputRef.of(newIndex, SpanAccess.END));
            }

        }

        return inputMapping;
    }

}
