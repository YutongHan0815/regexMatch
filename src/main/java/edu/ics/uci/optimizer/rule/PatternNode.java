package edu.ics.uci.optimizer.rule;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import edu.ics.uci.optimizer.operator.Operator;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class PatternNode implements Serializable {

    private final Class<? extends Operator> operatorClass;

    private final Predicate<? extends Operator> predicate;

    private final ImmutableList<PatternNode> children;

    private final ChildrenPolicy childrenPolicy;

    private PatternNode(Class<? extends Operator> operatorClass, Predicate<? extends Operator> predicate,
                        List<PatternNode> children, ChildrenPolicy childrenPolicy) {
        Preconditions.checkNotNull(operatorClass);
        Preconditions.checkNotNull(predicate);
        Preconditions.checkNotNull(children);
        Preconditions.checkNotNull(childrenPolicy);

        this.operatorClass = operatorClass;
        this.predicate = predicate;
        this.children = ImmutableList.copyOf(children);
        this.childrenPolicy = childrenPolicy;
    }

    public static <T extends Operator> PatternNode leaf(Class<T> operatorClass) {
        return leaf(operatorClass, op -> true);
    }

    public static <T extends Operator> PatternNode leaf(Class<T> operatorClass, Predicate<T> predicate) {
        return exact(operatorClass, predicate, new ArrayList<>());
    }

    public static <T extends Operator> PatternNode exact(Class<T> operatorClass, PatternNode children) {
        return exact(operatorClass, op -> true, Collections.singletonList(children));
    }

    public static <T extends Operator> PatternNode exact(Class<T> operatorClass, List<PatternNode> children) {
        return exact(operatorClass, op -> true, children);
    }

    public static <T extends Operator> PatternNode exact(Class<T> operatorClass, Predicate<T> predicate, List<PatternNode> children) {
        return new PatternNode(operatorClass, predicate, children, ChildrenPolicy.EXACT);
    }

    public static <T extends Operator> PatternNode any(Class<T> operatorClass) {
        return any(operatorClass, op -> true);
    }

    public static <T extends Operator> PatternNode any(Class<T> operatorClass, Predicate<T> predicate) {
        return new PatternNode(operatorClass, predicate, ImmutableList.of(), ChildrenPolicy.ANY);
    }

    public Class<? extends Operator> getOperatorClass() {
        return operatorClass;
    }

    public Predicate<? extends Operator> getPredicate() {
        return predicate;
    }

    public List<PatternNode> getChildren() {
        return children;
    }

    public ChildrenPolicy getChildrenPolicy() {
        return childrenPolicy;
    }

    public Map<PatternNode, PatternNode> inverse() {
        HashMap<PatternNode, PatternNode> parentMap = new HashMap<>();
        this.accept(node -> node.getChildren().forEach(child -> parentMap.put(child, this)));
        return parentMap;
    }

    public Set<PatternNode> getAllNodes() {
//        Set<PatternNode> nodes = new HashSet<>();
        Set<PatternNode> nodes = new LinkedHashSet<>();
        this.accept(nodes::add);
        return nodes;
    }


    public void accept(Consumer<PatternNode> visitor) {
        this.children.forEach(children -> children.accept(visitor));
        visitor.accept(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatternNode that = (PatternNode) o;
        return Objects.equals(operatorClass, that.operatorClass) &&
                Objects.equals(predicate, that.predicate) &&
                Objects.equals(children, that.children) &&
                childrenPolicy == that.childrenPolicy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operatorClass, predicate, children, childrenPolicy);
    }

    @Override
    public String toString() {
        return "PatternNode{" +
                "operatorClass=" + operatorClass +
                ", predicate=" + predicate +
                ", children=" + children +
                ", childrenPolicy=" + childrenPolicy +
                '}';
    }
}
