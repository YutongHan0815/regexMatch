package edu.ics.uci.optimizer.operator;

import com.google.common.collect.*;
import edu.ics.uci.optimizer.triat.TraitSet;

import java.io.Serializable;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class MetaSet implements Serializable {

    private final ImmutableSet<OperatorNode> operatorNodes;
    private final ImmutableMultimap<TraitSet, OperatorNode> traits;

    public static MetaSet create(OperatorNode operatorNode) {
        return create(Collections.singletonList(operatorNode));
    }

    public static MetaSet create(Collection<OperatorNode> operatorNodes) {
        return new MetaSet(operatorNodes);
    }

    private MetaSet(Collection<OperatorNode> operatorNodes) {
        this.operatorNodes = ImmutableSet.copyOf(operatorNodes);
        traits = ImmutableMultimap.copyOf(
                this.operatorNodes.stream().map(op -> Maps.immutableEntry(op.getTraitSet(), op)).collect(toList())
        );
    }

    public static MetaSet withNewOperator(MetaSet metaSet, OperatorNode newOperator) {
        return create(ImmutableSet.<OperatorNode>builder().addAll(metaSet.operatorNodes).add(newOperator).build());
    }

    public static MetaSet withNewOperators(MetaSet metaSet, Iterable<OperatorNode> newOperators) {
        return create(ImmutableSet.<OperatorNode>builder().addAll(metaSet.operatorNodes).addAll(newOperators).build());
    }

    public Set<OperatorNode> getOperators() {
        return new HashSet<>(operatorNodes);
    }

    public SubsetNode getSubset(TraitSet traitSet) {
        return SubsetNode.create(this, traitSet);
    }

    public Set<OperatorNode> getOperators(TraitSet traitSet) {
        return new HashSet<>(this.traits.get(traitSet));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaSet metaSet = (MetaSet) o;
        return Objects.equals(operatorNodes, metaSet.operatorNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operatorNodes);
    }

    @Override
    public String toString() {
        return "MetaSet{" +
                "operatorNodes=" + operatorNodes +
                ", traits=" + traits +
                '}';
    }
}
