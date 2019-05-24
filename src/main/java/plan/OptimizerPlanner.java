package plan;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;


import operators.Operator;
import org.jetbrains.annotations.NotNull;
import plan.rule.RuleCall;
import rules.TransformationRule;

import java.io.Serializable;
import java.util.*;


import static java.util.stream.Collectors.toList;


public class OptimizerPlanner implements Planner, Serializable {

    final OptimizerContext context;
    final SubsetNode root;

    final BiMap<Integer, MetaSet> sets = HashBiMap.create();
    final BiMap<Integer, OperatorNode> operators = HashBiMap.create();
    final BiMap<Integer, Integer> operatorToSet = HashBiMap.create();

    final Multimap<Integer, Integer> operatorParentMap = HashMultimap.create();

    final List<TransformationRule> ruleSet = new ArrayList<>();
    final Multimap<Class<? extends Operator>, TransformationRule> operatorRuleIndex = HashMultimap.create();

    final Queue<RuleCall> ruleCallQueue = new ArrayDeque<>();


    public static OptimizerPlanner create(SubsetNode root, Set<TransformationRule> rules) {
        return new OptimizerPlanner(root, rules);
    }

    private OptimizerPlanner(SubsetNode root, Set<TransformationRule> rules) {
        this.context = new OptimizerContext(this);
        this.root = root;
        rules.forEach(rule -> this.addRule(rule));
        this.registerNewSet(root.getMetaSet());
    }

    public void optimize() {
        while (! ruleCallQueue.isEmpty()) {
            RuleCall ruleCall = ruleCallQueue.poll();
            ruleCall.getMatchedRule().onMatch(ruleCall);
        }
    }

    public void addRule(TransformationRule rule) {
        Preconditions.checkArgument(! this.ruleSet.contains(rule));
        this.ruleSet.add(rule);

        rule.getMatchPattern().accept(pattern -> {
            this.operatorRuleIndex.put(pattern.getOperatorClass(), rule);
        });
    }

    public void removeRuleIfExists(TransformationRule rule) {
        if (! this.ruleSet.contains(rule)) {
            return;
        }
        this.ruleSet.remove(rule);
        HashMultimap.create(this.operatorRuleIndex).forEach((op, existingRule) -> {
            if (existingRule.equals(rule)) {
                this.operatorRuleIndex.remove(op, existingRule);
            }
        });
    }

    /**
     * Adds a new SubsetNode into the planner.
     *
     * @return the setID of the newly registered set
     */
    private int registerNewSet(@NotNull MetaSet set) {
        // check for duplicate
        if (this.sets.values().contains(set)) {
            return this.sets.inverse().get(set);
        }

        List<Integer> existingEquivSets = set.getOperators().stream()
                .filter(op -> this.operators.containsValue(op))
                .map(op -> this.operatorToSet.get(this.operators.inverse().get(op)))
                .collect(toList());

        if (! (existingEquivSets.size() == 0 || existingEquivSets.size() == 1)) {
            throw new UnsupportedOperationException("TODO: a set equivalent to multiple other sets is not handled yet");
        }

        if (existingEquivSets.size() == 1) {
            int currentSetID = existingEquivSets.get(0);
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
        if (this.operators.values().contains(newOperator)) {
            int duplicateOpID = this.operators.inverse().get(newOperator);
            int duplicateOpSet = this.operatorToSet.get(duplicateOpID);
            if (duplicateOpSet != setID) {
                throw new UnsupportedOperationException("TODO: set merge is not implemented yet");
            }
            return duplicateOpID;
        }

        int newOperatorID = this.context.nextOperatorID();
        this.operators.put(newOperatorID, newOperator);
        this.sets.get(setID).addOperatorNode(newOperator);
        this.operatorToSet.put(newOperatorID, setID);

        // add backwards parent pointers from children to this
        newOperator.getInputs().stream().flatMap(subset -> subset.getOperators().stream()).forEach(child -> {
            int childOpID = this.operators.inverse().get(child);
            this.operatorParentMap.put(childOpID, newOperatorID);
        });

        fireRules(newOperator);

        return newOperatorID;
    }


    private void fireRules(OperatorNode operatorNode) {
        Collection<TransformationRule> relevantRules = operatorRuleIndex.get(operatorNode.getOperator().getClass());

        relevantRules.stream().map(rule -> new RuleMatcher(this, operatorNode, rule).match())
                .forEach(ruleCall -> ruleCall.ifPresent(this.ruleCallQueue::add));
    }








}
