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
import java.util.stream.Collectors;


import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


public class OptimizerPlanner implements Serializable {

    private final OptimizerContext context;

    private final List<TraitDef> traitDefs = new ArrayList<>();

    private final AndOrTree andOrTree = AndOrTree.create();

    private final Set<TransformRule> ruleSet = new HashSet<>();
    private final Multimap<Class<? extends Operator>, TransformRule> operatorRuleIndex = HashMultimap.create();

    private final Queue<RuleCall> ruleCallQueue = new ArrayDeque<>();

    public static OptimizerPlanner create() {
        return new OptimizerPlanner();
    }

    private OptimizerPlanner() {
        this.context = new OptimizerContext(this);
    }

    public void setRoot(SubsetNode root) {
//        this.root = root;
        this.registerNewSet(root.getMetaSet());
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
    private int registerNewSet(@NotNull MetaSet set) {

        Set<Integer> existingEquivSets = set.getOperators().stream()
                .filter(op -> this.operators.containsValue(op))
                .map(op -> this.operatorToSet.get(this.operators.inverse().get(op)))
                .collect(toSet());

        if (! (existingEquivSets.size() == 0 || existingEquivSets.size() == 1)) {
            throw new UnsupportedOperationException("TODO: a set equivalent to multiple other sets is not handled yet");


        }

        if (existingEquivSets.size() == 1) {
            int currentSetID = existingEquivSets.iterator().next();
            set.getOperators().forEach(op -> this.sets.get(currentSetID).addOperatorNode(op));
            return currentSetID;
        }

        int newSetID = this.context.nextSetID();

        MetaSet newMetaSet = MetaSet.create(new ArrayList<>());
        this.sets.put(newSetID, newMetaSet);

        // register operators in this set
        List<Integer> registeredOperators = set.getOperators().stream()
                .map(op -> this.registerOperator(op, newSetID)).collect(toList());

        registeredOperators.forEach(opID -> newMetaSet.addOperatorNode(this.operators.get(opID)));
        registeredOperators.forEach(opID -> operatorToSet.put(opID, newSetID));

        return  newSetID;
    }


    public int registerOperator(@NotNull OperatorNode operator, int setID) {
        // recursively add children
        List<SubsetNode> registeredChildren = operator.getInputs().stream()
                .map(subset -> {
                    MetaSet metaSet = this.sets.get(this.registerNewSet(subset.getMetaSet()));
                    return metaSet.getSubset(subset.getTraitSet());
                }).collect(toList());

        // newOperator is operator with all children already registered into the planner
        OperatorNode newOperator = OperatorNode.create(operator.getOperator(), operator.getTraitSet(), registeredChildren);

        // check duplicate
       // if (this.operators.values().contains(newOperator)) {
            if(this.getOperators().values().contains(newOperator)){
            int duplicateOpID = this.getOperators().inverse().get(newOperator);
            int duplicateOpSet = this.operatorToSet.get(duplicateOpID);
            if (duplicateOpSet != setID) {
                throw new UnsupportedOperationException("TODO: set merge is not implemented yet");
            }
            return duplicateOpID;
        }
       // TODO: else invalid setID

        //add forwards parents pointers from this to parents
        Set<OperatorNode> operatorNodes = this.getSets().get(setID).getOperators();
        Set<OperatorNode> parents = new HashSet<>();
        operatorNodes.forEach(operatorNode -> {
            parents.addAll(this.operatorParentMap.get(operatorNode));
        });
        parents.forEach(parent->this.operatorParentMap.put(newOperator, parent));

        // new operatorNode
        int newOperatorID = this.context.nextOperatorID();
        this.operators.put(newOperatorID, newOperator);
        this.sets.get(setID).addOperatorNode(newOperator);
        this.operatorToSet.put(newOperatorID, setID);

        // add backwards parent pointers from children to this
        newOperator.getInputs().stream().flatMap(subset -> subset.getOperators().stream()).forEach(child -> {
            this.operatorParentMap.put(child, newOperator);
        });





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

}
