package edu.ics.uci.optimizer.rule;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import edu.ics.uci.optimizer.operator.Operator;
import io.vavr.Tuple2;
import io.vavr.control.Option;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Verify.verify;

/**
 * Node in the regex exprssion which determines whether a {@link TransformRule} can be applied to a particular expression.
 */

public class PatternNode implements Serializable {

    // id in pre-order traversal of the pattern tree, starting from 0
    private final int id;
    // index the children of parent node
    private final Option<Integer> indexInParent;

    private final Class<? extends Operator> operatorClass;

    private final Predicate<? extends Operator> predicate;

    private final ChildPolicy childPolicy;

    private final List<PatternNode> children;

    private PatternNode(
            int id, Option<Integer> indexInParent, Class<? extends Operator> operatorClass, Predicate<? extends Operator> predicate,
            ChildPolicy childPolicy, List<PatternNode> children) {
        Preconditions.checkNotNull(indexInParent);
        Preconditions.checkNotNull(operatorClass);
        Preconditions.checkNotNull(predicate);
        Preconditions.checkNotNull(childPolicy);
        Preconditions.checkNotNull(children);

        this.id = id;
        this.indexInParent = indexInParent;
        this.operatorClass = operatorClass;
        this.predicate = predicate;
        this.childPolicy = childPolicy;
        this.children = ImmutableList.copyOf(children);
    }

    // ---------- PatternNode Builder with Fluent API ----------

    public static class Builder<T extends Operator> implements Serializable {

        private Class<T> operatorClass;
        private Predicate<T> predicate;
        private ChildPolicy childPolicy;
        private List<PatternNode.Builder> children;

        public Builder(Class<T> operatorClass) {
            this.operatorClass = operatorClass;
        }

        public Builder predicate(Predicate<T> predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder children(Tuple2<ChildPolicy, List<Builder>> children) {
            this.childPolicy = children._1;
            this.children = children._2;
            return this;
        }

        public PatternNode build() {
            return buildInternal(new AtomicInteger(0), Option.none());
        }

        private PatternNode buildInternal(AtomicInteger nextID, Option<Integer> indexInParent) {
            verify(this.operatorClass != null);
            verify(this.childPolicy != null);
            verify(this.children != null);

            if (this.predicate == null) {
                this.predicate = operator -> true;
            }
            int selfID = nextID.getAndIncrement();

            List<PatternNode> childrenNodes = IntStream.range(0, this.children.size())
                    .mapToObj(i -> this.children.get(i).buildInternal(nextID, Option.of(i)))
                    .collect(Collectors.toList());

            return new PatternNode(selfID, indexInParent, this.operatorClass, this.predicate, this.childPolicy, childrenNodes);
        }
    }

    public static <T extends Operator> PatternNode.Builder<T> operand(Class<T> operatorClass) {
        return new PatternNode.Builder<>(operatorClass);
    }

    public static Tuple2<ChildPolicy, List<PatternNode.Builder>> exact(PatternNode.Builder... children) {
        return exact(ImmutableList.copyOf(children));
    }

    public static Tuple2<ChildPolicy, List<PatternNode.Builder>> exact(List<PatternNode.Builder> children) {
        return new Tuple2<>(ChildPolicy.EXACT, children);
    }

    public static Tuple2<ChildPolicy, List<PatternNode.Builder>> none() {
        return new Tuple2<>(ChildPolicy.EXACT, ImmutableList.of());
    }

    public static Tuple2<ChildPolicy, List<PatternNode.Builder>> any() {
        return new Tuple2<>(ChildPolicy.ANY, ImmutableList.of());
    }


    // ---------- Visitor Pattern ----------

    public void accept(Consumer<PatternNode> visitor) {
        this.children.forEach(children -> children.accept(visitor));
        visitor.accept(this);
    }

    public Map<PatternNode, PatternNode> inverse() {
        HashMap<PatternNode, PatternNode> parentMap = new HashMap<>();
        this.accept(node -> node.getChildren().forEach(child -> parentMap.put(child, this)));
        return parentMap;
    }

    public Set<PatternNode> getAllNodes() {
        // TODO: change this back to hash set and verify if the bug still exists
//        Set<PatternNode> nodes = new HashSet<>();
        Set<PatternNode> nodes = new LinkedHashSet<>();
        this.accept(nodes::add);
        return nodes;
    }


    // ---------- Getters, equals, hashcode, toString ----------

    public int getId() {
        return id;
    }

    public Option<Integer> getIndexInParent() {
        return indexInParent;
    }

    public Class<? extends Operator> getOperatorClass() {
        return operatorClass;
    }

    public Predicate<? extends Operator> getPredicate() {
        return predicate;
    }

    public ChildPolicy getChildPolicy() {
        return childPolicy;
    }

    public List<PatternNode> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatternNode that = (PatternNode) o;
        return id == that.id &&
                indexInParent.equals(that.indexInParent) &&
                operatorClass.equals(that.operatorClass) &&
                predicate.equals(that.predicate) &&
                childPolicy == that.childPolicy &&
                children.equals(that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indexInParent, operatorClass, predicate, childPolicy, children);
    }

    @Override
    public String toString() {
        return "PatternNode{" +
                "id=" + id +
                ", indexInParent=" + indexInParent +
                ", operatorClass=" + operatorClass +
                ", predicate=" + predicate +
                ", childPolicy=" + childPolicy +
                ", children=" + children +
                '}';
    }
}
