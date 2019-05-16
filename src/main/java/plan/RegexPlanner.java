package plan;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;


import static java.util.stream.Collectors.toList;


public class RegexPlanner implements Planner, Serializable {

    private final OptimizerContext context;
    private final SetNode root;

    private final BiMap<Integer, SetNode> sets = HashBiMap.create();
    private final BiMap<Integer, OperatorNode> operators = HashBiMap.create();
    private final BiMap<OperatorNode, Integer> operatorToSet = HashBiMap.create();

    private final Queue<RuleCall> ruleCallQueue = new ArrayDeque<>();


    public static RegexPlanner create(SetNode root) {
        return new RegexPlanner(root);
    }

    private RegexPlanner(SetNode root) {
        this.context = new OptimizerContext(this);
        this.root = root;
    }

    public void optimize() {

    }

    /**
     * Registers a set with the planner.
     * If setID is not null, add all operators in the set into the existing setID.
     * Else, creates a new set.
     *
     * @return the setID of the newly registered set
     */
    public int registerSet(SetNode node, @Nullable Integer setID) {
        // check for duplicate
        if (this.sets.values().contains(node)) {
            return this.sets.inverse().get(node);
        }

        if (setID != null) {
            node.getOperators().forEach(op -> registerOperator(op, setID));
            return setID;
        } else {
            int nextSetID = this.context.nextSetID();
            SetNode newSetNode = SetNode.create(node.getOperators());
            this.sets.put(nextSetID, newSetNode);
            for (OperatorNode operatorNode : node.getOperators()) {
                this.operatorToSet.put(operatorNode,nextSetID);
            }
            return  nextSetID;

            // TODO
        }

    }

    public int registerOperator(OperatorNode operator, @Nullable Integer setID) {
        // recursively add children
        List<SetNode> registeredChildren = operator.getInputs().stream()
                .map(set -> registerSet(set, null)).map(childSetID -> this.sets.get(childSetID)).collect(toList());
        OperatorNode operatorWithRegisteredChildren = OperatorNode.create(operator.getOperator(), registeredChildren);

        if(this.operatorToSet.containsKey(operatorWithRegisteredChildren)) {
            return this.operatorToSet.get(operatorWithRegisteredChildren);
        }

        int nextOperatorID = this.context.nextOperatorID();
        if (setID != null) {

            addNewOperatorNode(operatorWithRegisteredChildren, setID);
            this.operators.put(nextOperatorID, operatorWithRegisteredChildren);
            this.operatorToSet.put(operatorWithRegisteredChildren, setID);

        } else {
            int nextSetID = this.context.nextSetID();
            SetNode newSetNode = SetNode.create(operatorWithRegisteredChildren);
            addNewOperatorNode(operatorWithRegisteredChildren, nextSetID);
            this.sets.put(nextSetID, newSetNode);
            this.operatorToSet.put(operatorWithRegisteredChildren, nextSetID);
            this.operators.put(nextOperatorID,operatorWithRegisteredChildren);

        }

        return nextOperatorID;

    }


    private void addNewOperatorNode(OperatorNode newOperator, int setID) {
        int newOperatorID = this.context.nextOperatorID();
        this.operators.put(newOperatorID, newOperator);
        this.sets.get(setID).addOperatorNode(newOperator);
        this.operatorToSet.put(newOperator, setID);

        // TODO: fire rule
        fireRules(newOperator);

    }
    private void fireRules(OperatorNode operatorNode) {



    }




}
