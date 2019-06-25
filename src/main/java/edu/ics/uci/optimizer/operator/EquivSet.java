package edu.ics.uci.optimizer.operator;

import com.google.common.base.Verify;
import com.google.common.collect.*;
import edu.ics.uci.optimizer.OptimizerContext;
import edu.ics.uci.optimizer.memo.SetMemo;
import edu.ics.uci.optimizer.operator.schema.RowType;
import edu.ics.uci.optimizer.triat.TraitSet;

import java.io.Serializable;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class EquivSet implements Serializable {

    private final int setID;
    private final Set<OperatorNode> operatorNodes;
    private final Multimap<TraitSet, OperatorNode> traits;
    private final SetMemo setMemo;

    public static EquivSet create(OptimizerContext context, OperatorNode operatorNode) {
        return create(context, Collections.singletonList(operatorNode));
    }

    public static EquivSet create(OptimizerContext context, Collection<OperatorNode> operatorNodes) {
        return new EquivSet(context, operatorNodes);
    }

    private EquivSet(OptimizerContext context, Collection<OperatorNode> operatorNodes) {
        this.setID = context.nextSetID();
        this.operatorNodes = new HashSet<>();
        this.traits = HashMultimap.create();
        this.setMemo = SetMemo.create();

        operatorNodes.forEach(this::addOperator);
    }

    public void addOperator(OperatorNode operatorNode) {
        this.operatorNodes.add(operatorNode);
        this.traits.put(operatorNode.getTraitSet(), operatorNode);

        // check operator row type consistency, set row type if not present
        RowType operatorRowType = operatorNode.getOperatorMemo().getOutputRowType().get();
        if (this.setMemo.getOutputRowType().isPresent()) {
            Verify.verify(this.setMemo.getOutputRowType().get().equals(operatorRowType));
        } else {
            this.setMemo.setOutputRowType(operatorRowType);
        }
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

    public SetMemo getSetMemo() {
        return setMemo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquivSet equivSet = (EquivSet) o;
        return Objects.equals(operatorNodes, equivSet.operatorNodes) &&
                Objects.equals(traits, equivSet.traits);
    }

    @Override
    public int hashCode() {
        return Objects.hash( operatorNodes, traits);
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
