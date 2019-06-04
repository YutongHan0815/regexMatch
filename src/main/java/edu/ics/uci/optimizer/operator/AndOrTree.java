package edu.ics.uci.optimizer.operator;

import com.google.common.base.Preconditions;

import com.google.common.collect.*;
import edu.ics.uci.optimizer.OptimizerContext;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class AndOrTree implements Serializable {

    private final OptimizerContext context;

    private final Map<Integer, MetaSet> sets = new HashMap<>();
    private final BiMap<Integer, OperatorNode> operators = HashBiMap.create();
    private final Map<Integer, Integer> operatorToSet = new HashMap<>();

    private final Multimap<OperatorNode, OperatorNode> operatorParentMap = HashMultimap.create();

    public static AndOrTree create(OptimizerContext context) {
        return new AndOrTree(context);
    }

    private AndOrTree(OptimizerContext context) {
        this.context = context;
    }

    public Collection<OperatorNode> getOperatorParents(OperatorNode operatorNode) {
        checkArgument(this.operators.containsValue(operatorNode));
        return new HashSet<>(this.operatorParentMap.get(operatorNode));
    }

    public int getOperatorSetID(OperatorNode operatorNode) {
        checkArgument(this.operators.containsValue(operatorNode));
        return this.operatorToSet.get(this.operators.inverse().get(operatorNode));
    }

    public Map<Integer, MetaSet> getSets() {
        return new HashMap<>(sets);
    }

    public MetaSet getSet(int setID) {
        checkArgument(this.sets.containsKey(setID), "no set with ID: " + setID);
        return this.sets.get(setID);
    }

    public BiMap<Integer, OperatorNode> getOperators() {
        return HashBiMap.create(operators);
    }

    public OperatorNode getOperator(int operatorID) {
        checkArgument(this.operators.containsKey(operatorID), "no operator with ID: " + operatorID);
        return this.operators.get(operatorID);
    }

    public int addSet(MetaSet set) {
        int setID = this.context.nextSetID();
        this.sets.put(setID, set);
        return setID;
    }

    public int addOperator(OperatorNode operator, int setID) {
        checkNotNull(operator);
        // check setID exists
        checkArgument(this.sets.containsKey(setID), "no set with ID: " + setID);
        // check no duplicate operator - caller should ensure this
        checkArgument(! this.operators.containsValue(operator), "duplicate operator " + operator);
        // check all children are registered - caller should ensure this
        operator.getInputs().stream().flatMap(subset -> subset.getOperators().stream()).forEach(input ->
                checkArgument(this.operators.containsValue(input), "input is not registered " + input));

        // assign operatorID and add it
        int operatorID = this.context.nextOperatorID();
        this.operators.put(operatorID, operator);

        // update set to include the operator in the set
        this.operatorToSet.put(operatorID, setID);
        this.sets.put(setID, MetaSet.withNewOperator(this.sets.get(setID), operator));

        // add parents pointers from this to existing parents in the same subset
        Set<OperatorNode> existingParents = this.getSet(setID).getSubset(operator.getTraitSet()).getOperators().stream()
                .map(op -> this.getOperatorParents(op))
                .flatMap(l -> l.stream()).collect(Collectors.toSet());
        existingParents.forEach(parent -> this.operatorParentMap.put(operator, parent));

        // add parent pointers from the new operator's children
        operator.getInputs().stream().flatMap(subset -> subset.getOperators().stream()).forEach(child -> {
            this.operatorParentMap.put(child, operator);
        });

        return operatorID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndOrTree andOrTree = (AndOrTree) o;
        return Objects.equals(context, andOrTree.context) &&
                Objects.equals(sets, andOrTree.sets) &&
                Objects.equals(operators, andOrTree.operators) &&
                Objects.equals(operatorToSet, andOrTree.operatorToSet) &&
                Objects.equals(operatorParentMap, andOrTree.operatorParentMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context, sets, operators, operatorToSet, operatorParentMap);
    }

    @Override
    public String toString() {
        return "AndOrTree{" +
                "context=" + context +
                ", sets=" + sets +
                ", operators=" + operators +
                ", operatorToSet=" + operatorToSet +
                ", operatorParentMap=" + operatorParentMap +
                '}';
    }
}
