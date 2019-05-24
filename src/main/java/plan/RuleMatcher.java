package plan;

import com.google.common.base.Verify;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import plan.rule.RuleCall;
import rules.TransformationRule;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class RuleMatcher {

    private final OptimizerPlanner planner;
    private final OperatorNode triggerOperator;
    private final TransformationRule rule;
    private final Map<PatternNode, PatternNode> patternInverse;
    private final Multimap<PatternNode, OperatorNode> lookupTable = HashMultimap.create();

    private boolean potentialMatch = true;

    public RuleMatcher(OptimizerPlanner planner, OperatorNode operatorNode, TransformationRule rule) {
        this.planner = planner;
        this.triggerOperator = operatorNode;
        this.rule = rule;
        this.patternInverse = this.rule.getMatchPattern().inverse();
    }

    public Optional<RuleCall> match() {
        Set<PatternNode> allPatternNodes = rule.getMatchPattern().getAllNodes();

        Optional<PatternNode> relevantPattern = allPatternNodes.stream()
                .filter(node -> node.getOperatorClass().isAssignableFrom(triggerOperator.getClass()))
                .filter(node -> node.getPredicate().test(triggerOperator.getOperator()))
                .findAny();
        if (! relevantPattern.isPresent()) {
            return Optional.empty();
        }

        matchFrom(triggerOperator, relevantPattern.get());

        if (! potentialMatch) {
            return Optional.empty();
        }

        BiMap<Integer, PatternNode> patternOrdinals = HashBiMap.create();

        int ordinal = 0;
        Queue<PatternNode> toVisit = new LinkedList<>();
        toVisit.add(rule.getMatchPattern());
        while (! toVisit.isEmpty()) {
            PatternNode patternNode = toVisit.poll();
            patternOrdinals.put(ordinal, patternNode);
            ordinal++;
            toVisit.addAll(patternNode.getChildren());
        }

        BiMap<Integer, OperatorNode> matchedOperators = HashBiMap.create();

        allPatternNodes.forEach(patternNode -> {
            Collection<OperatorNode> operatorNodes = this.lookupTable.get(patternNode);
            Verify.verify(! operatorNodes.isEmpty());
            if (operatorNodes.size() > 1) {
                throw new UnsupportedOperationException("matching multiple operators per pattern node not implemented");
            }
            matchedOperators.put(patternOrdinals.inverse().get(patternNode), operatorNodes.iterator().next());
        });


        PatternRuleCall ruleCall = new PatternRuleCall(planner, rule, matchedOperators);
        return Optional.of(ruleCall);
    }

    private void matchFrom(OperatorNode operatorNode, PatternNode patternNode) {

        Multimap<PatternNode, OperatorNode> matchTable = HashMultimap.create();

        // match descending from this node
        this.matchDescending(triggerOperator, patternNode);

        // match ascending from this node
        Optional<PatternNode> parentPattern = Optional.ofNullable(patternInverse.get(patternNode));
        if (! parentPattern.isPresent()) {
            return;
        }
        List<OperatorNode> parents = planner.operatorParentMap.get(planner.operators.inverse().get(operatorNode))
                .stream().map(id -> planner.operators.get(id)).collect(toList());

        for (OperatorNode parent: parents) {
            OperatorNode tempParent = new OperatorNode(parent.getOperator(), parent.getTraitSet(),
                    Collections.singletonList(SubsetNode.create(operatorNode)));
            this.matchDescending(tempParent, parentPattern.get());
        }
    }


    private boolean matchDescending(OperatorNode operatorNode, PatternNode patternNode) {
        // early exit mechanism
        if (! potentialMatch) {
            return false;
        }
        if (lookupTable.containsKey(patternNode) && lookupTable.get(patternNode).size() > 0) {
            return true;
        }
        if (! matchSelf(operatorNode, patternNode)) {
            potentialMatch = false;
            return false;
        }

        switch (patternNode.getChildrenPolicy()) {
            case ANY:
                // ANY have no children and "self" node is already matched, directly return true
                Verify.verify(patternNode.getChildren().isEmpty());
                Verify.verify(matchSelf(operatorNode, patternNode));

                lookupTable.put(patternNode, operatorNode);
                return true;
            case EXACT:
                if (operatorNode.getInputs().size() != patternNode.getChildren().size()) {
                    return false;
                }

                for (int i = 0; i < operatorNode.getInputs().size(); i++) {
                    boolean childMatches = false;
                    for (OperatorNode op : operatorNode.getInputs().get(i).getOperators()) {
                        boolean match = matchDescending(op, patternNode.getChildren().get(i));
                        childMatches = childMatches || match;
                    }
                    if (! childMatches) {
                        this.potentialMatch = false;
                        return false;
                    }
                }
                lookupTable.put(patternNode, operatorNode);
                return true;
        }
        throw new RuntimeException("matchDescending child policy not handled");
    }

    private static boolean matchSelf(OperatorNode operatorNode, PatternNode patternNode) {
        return patternNode.getOperatorClass().isAssignableFrom(operatorNode.getOperator().getClass())
                && patternNode.getPredicate().test(operatorNode.getOperator());
    }


}
