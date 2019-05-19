package plan;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import operators.Operator;
import org.jetbrains.annotations.NotNull;
import rules.TransformationRule;

import java.io.Serializable;
import java.util.*;


import static java.util.stream.Collectors.toList;


public class OptimizerPlanner implements Planner, Serializable {

    private final OptimizerContext context;
    private final SetNode root;

    private final BiMap<Integer, SetNode> sets = HashBiMap.create();
    private final BiMap<Integer, OperatorNode> operators = HashBiMap.create();
    private final BiMap<Integer, Integer> operatorToSet = HashBiMap.create();

    private final List<TransformationRule> ruleSet = new ArrayList<>();
    private final Multimap<Class<? extends Operator>, TransformationRule> operatorRuleIndex = HashMultimap.create();

    private final Queue<RuleCall> ruleCallQueue = new ArrayDeque<>();


    public static OptimizerPlanner create(SetNode root) {
        return new OptimizerPlanner(root);
    }

    private OptimizerPlanner(SetNode root) {
        this.context = new OptimizerContext(this);
        this.root = root;
        this.registerSet(root);
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
     * Adds all OperatorNodes in the SetNode into the set with setID.
     * The SetNode will not be registered into the planner.
     * Returns the setID that the operators are added into.
     *
     * @param node
     * @param setID
     * @return
     */
    public int addToEquivSet(@NotNull SetNode node, int setID) {
        node.getOperators().forEach(op -> registerOperator(op, setID));
        return setID;
    }

    /**
     * Registers a new SetNode into the planner.
     *
     * @return the setID of the newly registered set
     */
    public int registerSet(@NotNull SetNode node) {
        // check for duplicate
        if (this.sets.values().contains(node)) {
            return this.sets.inverse().get(node);
        }

        int newSetID = this.context.nextSetID();
        SetNode newSetNode = SetNode.create(new ArrayList<>());
        this.sets.put(newSetID, newSetNode);

        // register operators in this set
        List<Integer> registeredOperators = node.getOperators().stream()
                .map(op -> this.registerOperator(op, newSetID)).collect(toList());

        registeredOperators.forEach(opID -> newSetNode.addOperatorNode(this.operators.get(opID)));
        registeredOperators.forEach(opID -> operatorToSet.put(opID, newSetID));

        return  newSetID;
    }


    public int registerOperator(@NotNull OperatorNode operator, int setID) {
        // recursively add children
        List<SetNode> registeredChildren = operator.getInputs().stream()
                .map(set -> registerSet(set)).map(childSetID -> this.sets.get(childSetID)).collect(toList());

        // newOperator is operator with all children already registered into the planner
        OperatorNode newOperator = OperatorNode.create(operator.getOperator(), registeredChildren);

        // check duplicate
        if (this.operators.values().contains(newOperator)) {
            return this.operators.inverse().get(newOperator);
        }

        int newOperatorID = this.context.nextOperatorID();
        this.operators.put(newOperatorID, newOperator);
        this.sets.get(setID).addOperatorNode(newOperator);
        this.operatorToSet.put(newOperatorID, setID);

        // TODO: fire rule
        fireRules(newOperator, setID);

        return newOperatorID;
    }


    private void fireRules(OperatorNode operatorNode, int setID) {
        List<TransformationRule> ruleList = new ArrayList<>(operatorRuleIndex.get(operatorNode.getOperator().getClass()));

        for(TransformationRule rule : ruleList) {
            PatternNode patternNode = rule.
            final PatternRuleCall ruleCall;
            if(patternNode.match(operatorNode)) {
                ruleCall = PatternRuleCall.
            }
            ruleCall.match(operatorNode);
        }


    }




}
