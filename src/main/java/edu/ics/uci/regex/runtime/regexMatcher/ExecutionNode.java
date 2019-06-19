package edu.ics.uci.regex.runtime.regexMatcher;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ExecutionNode implements Serializable {
   private final ExecutionOperator executionOperator;
   private List<ExecutionNode> children;
   private Multimap<ExecutionNode, ExecutionNode> parentChildMap = HashMultimap.create();


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
        this.children = this.children;
   }

    public ExecutionOperator getExecutionOperator() {
        return executionOperator;
    }

    public void accept(Consumer<ExecutionNode> visitor) {
       this.children.forEach(child-> child.accept(visitor));
    }

    public List<ExecutionNode> getChildren() {
        return children;
    }

    public Multimap<ExecutionNode, ExecutionNode> getParentChildMap() {
        return parentChildMap;
    }

    public void addNode(ExecutionNode parent, ExecutionNode child) {
       this.children.add(child);
       this.parentChildMap.put(parent, child);
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
