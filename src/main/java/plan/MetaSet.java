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
    private final Multimap<SubsetNode, OperatorNode> subsets = HashMultimap.create();
    private final BiMap<TraitSet, SubsetNode> subsetTraits = HashBiMap.create();

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
        Preconditions.checkArgument(! this.operatorNodes.contains(operatorNode));

        this.operatorNodes.add(operatorNode);
        TraitSet traitSet = operatorNode.getTraitSet();
        if (! this.subsetTraits.containsKey(traitSet)) {
            subsetTraits.put(traitSet, SubsetNode.create(this, traitSet));
        }
        subsets.keySet().forEach(subset -> {
            if (traitSet.satisfy(subset.getTraitSet())) {
                subsets.put(subset, operatorNode);
            }
        });
    }

    public Set<OperatorNode> getOperators() {
        return new HashSet<>(operatorNodes);
    }

    public SubsetNode getSubset(TraitSet traitSet) {
        if (! this.subsetTraits.containsKey(traitSet)) {
            return SubsetNode.create(this, traitSet);
        }
        return this.subsetTraits.get(traitSet);
    }

    public Set<OperatorNode> getOperators(TraitSet traitSet) {
        if (! this.subsetTraits.containsKey(traitSet)) {
            return new HashSet<>();
        }
        return new HashSet<>(this.subsets.get(this.subsetTraits.get(traitSet)));
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
