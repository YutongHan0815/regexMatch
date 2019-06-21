package edu.ics.uci.regex.runtime.regexMatcher;

import edu.ics.uci.optimizer.operator.Operator;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.PhysicalOperator;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RegexQueryEngine implements QueryEngine, Serializable {
    private ExecutionTree planTree;

    public RegexQueryEngine(ExecutionTree planTree) {
        this.planTree = planTree;

    }
    public void compile(OperatorNode root) {
        convert(root);
    }

    private ExecutionNode convert(OperatorNode operatorNode) {
        List<ExecutionNode> children = new ArrayList<>();
        if(!operatorNode.getInputs().isEmpty()) {
            operatorNode.getInputs().forEach(subsetNode -> subsetNode.getOperators().stream().forEach(
                    op -> {
                        children.add(convert(op.getOperator()));
                    }));
        }
        Operator operator = operatorNode.getOperator();
        if (! (operator instanceof PhysicalOperator)) {
            throw new RuntimeException(operator + " is not a physical operator");
        }
        ExecutionNode parentExecutionOperator = ExecutionNode.create(((PhysicalOperator) operator).getExecution(), children);
        children.forEach(child-> planTree.addNode(parentExecutionOperator, child));
        return parentExecutionOperator;
    }

    @Override
    public Relation executeQuery(String fieldValue) {
        Relation relation = Relation.create(fieldValue).getInitializeRelation();



        return relation;
    }

}
