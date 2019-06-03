package edu.ics.uci.optimizer.operator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AndOrTree {

    private SubsetNode root;

    private final Map<Integer, MetaSet> sets = new HashMap<>();
    private final BiMap<Integer, OperatorNode> operators = HashBiMap.create();
    private final Map<Integer, Integer> operatorToSet = new HashMap<>();

    private final Multimap<OperatorNode, OperatorNode> operatorParentMap = HashMultimap.create();

    public static AndOrTree create() {
    }

    public Collection<OperatorNode> getOperatorParents(OperatorNode operatorNode) {
        return new HashSet<>(this.operatorParentMap.get(operatorNode));
    }

    public SubsetNode getRoot() {
        return root;
    }

    public Map<Integer, MetaSet> getSets() {
        return new HashMap<>(sets);
    }

    public BiMap<Integer, OperatorNode> getOperators() {
        return HashBiMap.create(operators);
    }

    public Map<Integer, Integer> getOperatorToSet() {
        return new HashMap<>(operatorToSet);
    }


}
