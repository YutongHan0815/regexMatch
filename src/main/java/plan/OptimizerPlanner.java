package plan;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import operators.Operator;
import org.jetbrains.annotations.NotNull;
import plan.rule.RuleCall;
import rules.TransformationRule;

import java.io.Serializable;
import java.util.*;


import static java.util.stream.Collectors.toList;


public class OptimizerPlanner implements Planner, Serializable {

    private final OptimizerContext context;
    private final SubsetNode root;

    private final BiMap<Integer, MetaSet> sets = HashBiMap.create();
    private final BiMap<Integer, OperatorNode> operators = HashBiMap.create();
    private final BiMap<Integer, Integer> operatorToSet = HashBiMap.create();
   // private final Multimap<Class<? extends>>

    private final Multimap<Integer, Integer> operatorParentMap = HashMultimap.create();

    private final List<TransformationRule> ruleSet = new ArrayList<>();
    private final Multimap<Class<? extends Operator>, TransformationRule> operatorRuleIndex = HashMultimap.create();

    private final Queue<RuleCall> ruleCallQueue = new ArrayDeque<>();


    public static OptimizerPlanner create(SubsetNode root) {
        return new OptimizerPlanner(root);
    }

    private OptimizerPlanner(SubsetNode root) {
        this.context = new OptimizerContext(this);
        this.root = root;
        this.registerNewSet(root.getMetaSet());
    }

    public void optimize() {

    }

    public void addRule(TransformationRule rule) {
        Preconditions.checkArgument(! this.ruleSet.contains(rule));
        this.ruleSet.add(rule);

        rule.getMatchPattern().visit(pattern -> {
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
    public int registerNewSet(@NotNull MetaSet node) {
        // check for duplicate
        if (this.sets.values().contains(node)) {
            return this.sets.inverse().get(node);
        }

        int newSetID = this.context.nextSetID();
        MetaSet newMetaSet = MetaSet.create(new ArrayList<>());
        this.sets.put(newSetID, newMetaSet);

        // register operators in this set
        List<Integer> registeredOperators = node.getOperators().stream()
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
            return this.operators.inverse().get(newOperator);
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

        // TODO: fire rule
        fireRules(newOperator, setID);

        return newOperatorID;
    }


    private void fireRules(OperatorNode operatorNode, int setID) {



    }




}
