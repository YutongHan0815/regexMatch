package edu.ics.uci.optimizer;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;


import edu.ics.uci.optimizer.operator.*;
import edu.ics.uci.optimizer.rule.RuleCall;
import edu.ics.uci.optimizer.rule.RuleMatcher;
import edu.ics.uci.optimizer.rule.TransformRule;
import edu.ics.uci.optimizer.triat.TraitDef;
import edu.ics.uci.optimizer.triat.TraitSet;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;


import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


public class OptimizerPlanner implements Serializable {

    private final OptimizerContext context;

    private final List<TraitDef> traitDefs = new ArrayList<>();

    private final AndOrTree andOrTree;
    private SubsetNode root;

    private final Set<TransformRule> ruleSet = new HashSet<>();
    private final Multimap<Class<? extends Operator>, TransformRule> operatorRuleIndex = HashMultimap.create();

    private final Queue<RuleCall> ruleCallQueue = new ArrayDeque<>();

    public static OptimizerPlanner create() {
        return new OptimizerPlanner();
    }

    private OptimizerPlanner() {
        this.context = new OptimizerContext(this);
        this.andOrTree = AndOrTree.create(context);
    }

    public void setRoot(SubsetNode root) {
        this.root = root;
        this.registerNewSet(root.getEquivSet());
    }

    public void addRule(TransformRule rule) {
        Preconditions.checkArgument(! this.ruleSet.contains(rule));
        this.ruleSet.add(rule);
        rule.getMatchPattern().accept(pattern ->
            this.operatorRuleIndex.put(pattern.getOperatorClass(), rule)
        );
    }

    public void removeRule(TransformRule rule) {
        this.ruleSet.remove(rule);
        this.operatorRuleIndex.entries().stream().filter(e -> e.getValue().equals(rule))
                .forEach(e -> this.operatorRuleIndex.remove(e.getKey(), e.getValue()));
    }

    public void addTraitDef(TraitDef traitDef) {
        Preconditions.checkArgument(! this.traitDefs.contains(traitDef));
        this.traitDefs.add(traitDef);
    }

    public TraitSet defaultTraitSet() {
        return TraitSet.of(this.traitDefs.stream().map(def -> def.getDefault()).collect(toList()));
    }

    public void optimize() {
        while (! ruleCallQueue.isEmpty()) {
            RuleCall ruleCall = ruleCallQueue.poll();
            ruleCall.getRule().onMatch(ruleCall);
        }
    }

    /**
     * Adds a new SubsetNode into the planner.
     *
     * @return the setID of the newly registered set
     */
    private int registerNewSet(@NotNull EquivSet set) {

        Set<EquivSet> existingEquivSets = set.getOperators().stream()
                .filter(op -> this.andOrTree.getOperators().containsValue(op))
                .map(op -> this.andOrTree.getOperatorSet(op.getOperatorID()))
                .collect(toSet());

        if (! (existingEquivSets.size() == 0 || existingEquivSets.size() == 1)) {
            throw new UnsupportedOperationException("TODO: a set equivalent to multiple other sets is not handled yet");
        }

        if (existingEquivSets.size() == 1) {
            int currentSetID = existingEquivSets.iterator().next().getSetID();
            set.getOperators().forEach(op -> this.registerOperator(op, currentSetID));
            return currentSetID;
        }

        int newSetID = this.andOrTree.addSet(EquivSet.create(this.context, new ArrayList<>()));
        set.getOperators().forEach(op -> this.registerOperator(op, newSetID));

        return  newSetID;
    }


    public int registerOperator(@NotNull OperatorNode operator, int setID) {
        // recursively add children first
        List<SubsetNode> registeredChildren = operator.getInputs().stream()
                .map(subset -> {
                    EquivSet equivSet = this.andOrTree.getSet(this.registerNewSet(subset.getEquivSet()));
                    return equivSet.getSubset(subset.getTraitSet());
                }).collect(toList());

        // newOperator is operator with all children already registered into the planner
        OperatorNode newOperator = OperatorNode.create(operator.getOperatorID(), operator.getOperator(), operator.getTraitSet(), registeredChildren);

        // check duplicate
        if (this.andOrTree.getOperators().containsValue(newOperator)) {
            int duplicateOpID = this.andOrTree.getOperators().inverse().get(newOperator);
            int duplicateOpSet = this.andOrTree.getOperatorSet(duplicateOpID).getSetID();
            if (duplicateOpSet != setID) {
                throw new UnsupportedOperationException("TODO: set merge is not implemented yet");
            }
            return duplicateOpID;
        }

        int newOperatorID = this.andOrTree.addOperator(newOperator, setID);

        fireRules(newOperator);

        return newOperatorID;
    }


    private void fireRules(OperatorNode operatorNode) {
        Collection<TransformRule> relevantRules = operatorRuleIndex.get(operatorNode.getOperator().getClass());

        relevantRules.stream().map(rule -> new RuleMatcher(this, operatorNode, rule).match())
                .forEach(ruleCall -> ruleCall.ifPresent(this.ruleCallQueue::add));
    }


    public OptimizerContext getContext() {
        return context;
    }

    public List<TransformRule> getRuleSet() {
        return new ArrayList<>(ruleSet);
    }

    public Queue<RuleCall> getRuleCallQueue() {
        return new LinkedList<>(ruleCallQueue);
    }

    public AndOrTree getAndOrTree() {
        return andOrTree;
    }

    public SubsetNode getRoot() {
        return root;
    }
}
