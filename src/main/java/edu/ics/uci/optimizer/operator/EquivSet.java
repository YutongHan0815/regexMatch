package edu.ics.uci.optimizer.operator;

import com.google.common.collect.*;
import edu.ics.uci.optimizer.OptimizerContext;
import edu.ics.uci.optimizer.triat.TraitSet;

import java.io.Serializable;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class EquivSet implements Serializable {

    private final int setID;
    private final Set<OperatorNode> operatorNodes;
    private final Multimap<TraitSet, OperatorNode> traits;

    public static EquivSet create(OptimizerContext context, OperatorNode operatorNode) {
        return create(context, Collections.singletonList(operatorNode));
    }

    public static EquivSet create(OptimizerContext context, Collection<OperatorNode> operatorNodes) {
        return new EquivSet(context, operatorNodes);
    }

    private EquivSet(OptimizerContext context, Collection<OperatorNode> operatorNodes) {
        this.setID = context.nextSetID();
        this.operatorNodes = new HashSet<>(operatorNodes);
        this.traits = HashMultimap.create();
        this.operatorNodes.forEach(op -> this.traits.put(op.getTraitSet(), op));
    }

    public void addOperator(OperatorNode operatorNode) {
        this.operatorNodes.addAll(operatorNodes);
        this.traits.put(operatorNode.getTraitSet(), operatorNode);
    }

    public int getSetID() {
        return setID;
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
        EquivSet equivSet = (EquivSet) o;
        return setID == equivSet.setID &&
                Objects.equals(operatorNodes, equivSet.operatorNodes) &&
                Objects.equals(traits, equivSet.traits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(setID, operatorNodes, traits);
    }

    @Override
    public String toString() {
        return "EquivSet{" +
                "setID=" + setID +
                ", operatorNodes=" + operatorNodes +
                ", traits=" + traits +
                '}';
    }
}
