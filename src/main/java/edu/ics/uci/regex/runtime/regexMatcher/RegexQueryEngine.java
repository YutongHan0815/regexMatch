package edu.ics.uci.regex.runtime.regexMatcher;

import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.regex.runtime.regexMatcher.relation.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RegexQueryEngine implements QueryEngine, Serializable {
    private OperatorNode root;
    private ExecutionNode planTree;

    public RegexQueryEngine(OperatorNode root) {
        this.root = root;
        this.planTree = ExecutionNode.create(root.getOperator().getExecution());

    }
    public void compile() {
        convert(root);
    }

    private ExecutionNode convert(OperatorNode operatorNode) {
        ExecutionNode parentExecutionOperator = ExecutionNode.create(operatorNode.getOperator().getExecution());
        if(operatorNode.getInputs().isEmpty()) {
            planTree.addNode(parentExecutionOperator, );
            return parentExecutionOperator;
        }
        List<ExecutionNode> children = new ArrayList<>();
        operatorNode.getInputs().forEach(subsetNode -> subsetNode.getOperators().stream().forEach(
                op -> {
                    ExecutionNode childExecutionOperator = convert(op.getOperator());
                    children.add(childExecutionOperator);

                }));
        parentExecutionOperator = ExecutionNode.create(operatorNode.getOperator().getExecution(), children);
        children.forEach(child-> planTree.addNode(parentExecutionOperator, child));
        return parentExecutionOperator;
    }

    @Override
    public Relation executeQuery(String fieldValue) {
        Relation relation = Relation.create(fieldValue).getInitializeRelation();

        ExecutionOperator root = planTree.getExecutionOperator();
        root.

        List<Relation> tempResult = new ArrayList<>();


        return relation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexQueryEngine that = (RegexQueryEngine) o;
        return Objects.equals(root, that.root) &&
                Objects.equals(planTree, that.planTree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, planTree);
    }
}
