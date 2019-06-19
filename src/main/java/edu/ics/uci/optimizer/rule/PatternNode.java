package edu.ics.uci.optimizer.rule;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import edu.ics.uci.optimizer.operator.Operator;
import io.vavr.Tuple2;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Verify.verify;


public class PatternNode implements Serializable {

    private final int id;

    private final Class<? extends Operator> operatorClass;

    private final Predicate<? extends Operator> predicate;

    private final ChildPolicy childPolicy;

    private final List<PatternNode> children;

    private PatternNode(
            int id, Class<? extends Operator> operatorClass, Predicate<? extends Operator> predicate,
            ChildPolicy childPolicy, List<PatternNode> children) {
        Preconditions.checkNotNull(operatorClass);
        Preconditions.checkNotNull(predicate);
        Preconditions.checkNotNull(childPolicy);
        Preconditions.checkNotNull(children);

        this.id = id;
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
            return buildInternal(new AtomicInteger(0));
        }

        private PatternNode buildInternal(AtomicInteger nextID) {
            verify(this.operatorClass != null);
            verify(this.childPolicy != null);
            verify(this.children != null);

            if (this.predicate == null) {
                this.predicate = operator -> true;
            }
            int selfID = nextID.getAndIncrement();

            List<PatternNode> childrenNodes = this.children.stream()
                    .map(child -> child.buildInternal(nextID)).collect(Collectors.toList());

            return new PatternNode(selfID, this.operatorClass, this.predicate, this.childPolicy, childrenNodes);
        }
    }

    public static <T extends Operator> PatternNode.Builder<T> operand(Class<T> operatorClass) {
        return new PatternNode.Builder<>(operatorClass);
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


    // ---------- Getters, equals, hashcode, toString ----------

    public int getId() {
        return id;
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
                operatorClass.equals(that.operatorClass) &&
                predicate.equals(that.predicate) &&
                childPolicy == that.childPolicy &&
                children.equals(that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, operatorClass, predicate, childPolicy, children);
    }

    @Override
    public String toString() {
        return "PatternNode{" +
                "id=" + id +
                ", operatorClass=" + operatorClass +
                ", predicate=" + predicate +
                ", childPolicy=" + childPolicy +
                ", children=" + children +
                '}';
    }
}
