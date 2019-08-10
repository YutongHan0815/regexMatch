package edu.ics.uci.optimizer.operator;


import com.google.common.collect.*;
import edu.ics.uci.optimizer.OptimizerContext;
import io.vavr.Tuple2;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * AndOrTree contains {@link EquivSet} and {@link Operator} in a and-or tree. The Operators in the same EquivSet is disjunction.
 * The inputs of an operator are EquivSets in the tree. The relationship between the operator and its input EquivSets is conjunction.
 * When the {@link edu.ics.uci.optimizer.OptimizerPlanner} is initialized, a AndOrTree will be initialized.
 * AndOrTree is used to register a new Operator or a new EquivSet.
 * If there are new operator or EquivSet need to be register in the {@link edu.ics.uci.optimizer.OptimizerPlanner},
 * it will call functions to add the operator or EquivSet into AndOrTree.
 */
public class AndOrTree implements Serializable {

    //
    private final OptimizerContext context;

    private final Map<Integer, EquivSet> sets = new HashMap<>();
    private final Map<Integer, OperatorNode> operators = new HashMap<>();
    private final Map<Integer, Integer> operatorToSet = new HashMap<>();

    private final Multimap<Integer, Tuple2<OperatorNode, Integer>> operatorParentMap = HashMultimap.create();

    public static AndOrTree create(OptimizerContext context) {
        return new AndOrTree(context);
    }

    private AndOrTree(OptimizerContext context) {
        this.context = context;
    }

    public Map<Integer, EquivSet> getSets() {
        return new HashMap<>(sets);
    }

    public EquivSet getSet(int setID) {
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

    public int addSet(EquivSet set) {
        int setID = set.getSetID();
        checkArgument(! this.sets.containsKey(setID), "SetID " + setID + " already exists");

        this.sets.put(setID, set);
        return setID;
    }

    public int addOperator(OperatorNode operator, int setID) {
        checkNotNull(operator);
        // check setID exists
        checkArgument(this.sets.containsKey(setID), "no set with ID: " + setID);
        // check no duplicate operator - caller should ensure this
        int operatorID = operator.getOperatorID();
        checkArgument(! this.operators.containsKey(operatorID), "duplicate operatorID " + operatorID);
        checkArgument(! this.operators.containsValue(operator), "duplicate operator " + operator);
        // check all children are registered - caller should ensure this
        operator.getInputs().stream().flatMap(subset -> subset.getOperators().stream()).forEach(input ->
                checkArgument(this.operators.containsValue(input), "input is not registered " + input));

        System.out.println("new Operator" + operator);
        this.operators.put(operatorID, operator);

        // update set to include the operator in the set
        this.operatorToSet.put(operatorID, setID);
        this.sets.get(setID).addOperator(operator);

        // add parents pointers from this to existing parents in the same subset
        Set<Tuple2<OperatorNode, Integer>> existingParents = this.getSet(setID)
                .getSubset(operator.getTraitSet()).getOperators().stream()
                .map(op -> this.getOperatorParents(op.getOperatorID()))
                .flatMap(l -> l.stream()).collect(toSet());
        existingParents.forEach(parent -> this.operatorParentMap.put(operator.getOperatorID(), parent));

        // add parent pointers from the new operator's children
        for (int i = 0; i < operator.getInputs().size(); i++) {
            for (OperatorNode inputOp : operator.getInputs().get(i).getOperators()) {
                this.operatorParentMap.put(inputOp.getOperatorID(), new Tuple2<>(operator, i));
            }
        }

        return operatorID;
    }

    public Collection<Tuple2<OperatorNode, Integer>> getOperatorParents(int operatorID) {
        checkArgument(this.operators.containsKey(operatorID));
        return this.operatorParentMap.get(operatorID);
    }

    public Collection<Tuple2<OperatorNode, Integer>>  getOperatorParents(OperatorNode operator) {
        return getOperatorParents(operator.getOperatorID());
    }

    public EquivSet getOperatorSet(int operatorID) {
        checkArgument(this.operators.containsKey(operatorID));
        return this.sets.get(this.operatorToSet.get(operatorID));
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
