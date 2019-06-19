package edu.ics.uci.regex.runtime.regexMatcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExecutionNode implements Serializable {
   private final ExecutionOperator executionOperator;
   private List<ExecutionNode> children;



   public static ExecutionNode create(ExecutionOperator root) {
        return new ExecutionNode(root, new ArrayList<>());
   }
    public static ExecutionNode create(ExecutionOperator root, ExecutionNode children) {
        return new ExecutionNode(root, Arrays.asList(children));
    }
   public static ExecutionNode create(ExecutionOperator root, List<ExecutionNode> children) {
        return new ExecutionNode(root, children);
   }
   private ExecutionNode(ExecutionOperator executionOperator, List<ExecutionNode> children) {
        this.executionOperator = executionOperator;
        this.children = children;
   }

    public ExecutionOperator getExecutionOperator() {
        return executionOperator;
    }

    public List<ExecutionNode> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionNode planTree = (ExecutionNode) o;
        return Objects.equals(executionOperator, planTree.executionOperator) &&
                Objects.equals(children, planTree.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executionOperator, children);
    }
}
