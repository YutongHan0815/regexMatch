package plan;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import plan.triat.Trait;
import plan.triat.TraitSet;

import java.util.*;

public class MetaSet {

    private final Set<OperatorNode> operatorNodes = new HashSet<>();
    private final Multimap<TraitSet, OperatorNode> traits = HashMultimap.create();

    public static MetaSet create(OperatorNode operatorNode) {
        return create(Collections.singletonList(operatorNode));
    }

    public static MetaSet create(List<OperatorNode> operatorNodes) {
        return new MetaSet(operatorNodes);
    }

    private MetaSet(List<OperatorNode> operatorNodes) {
        operatorNodes.forEach(op -> this.addOperatorNode(op));
    }

    public void addOperatorNode(OperatorNode operatorNode) {
        Preconditions.checkNotNull(operatorNode);
        // check duplicate operatorNode in this set
        if (this.operatorNodes.contains(operatorNode)) {
            return;
        }

        this.operatorNodes.add(operatorNode);
        TraitSet traitSet = operatorNode.getTraitSet();
        traits.put(traitSet, operatorNode);
        traits.keySet().forEach(otherTrait -> {
            if (! otherTrait.equals(traitSet) && traitSet.satisfy(otherTrait)) {
                traits.put(otherTrait, operatorNode);
            }
        });
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



}
