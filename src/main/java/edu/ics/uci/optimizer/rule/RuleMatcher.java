package edu.ics.uci.optimizer.rule;

import com.google.common.base.Verify;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.ics.uci.optimizer.OptimizerPlanner;
import edu.ics.uci.optimizer.operator.OperatorNode;
import edu.ics.uci.optimizer.operator.SubsetNode;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class RuleMatcher implements Serializable {

    private final OptimizerPlanner planner;
    private final OperatorNode triggerOperator;
    private final TransformRule rule;
    private final Map<PatternNode, PatternNode> patternInverse;
    private final BiMap<OperatorNode, OperatorNode> temporaryOperators = HashBiMap.create();
    private final Multimap<PatternNode, OperatorNode> lookupTable = HashMultimap.create();

    private boolean matchFailed = false;

    public RuleMatcher(OptimizerPlanner planner, OperatorNode operatorNode, TransformRule rule) {
        this.planner = planner;
        this.triggerOperator = operatorNode;
        this.rule = rule;
        this.patternInverse = this.rule.getMatchPattern().inverse();
    }

    public Optional<RuleCall> match() {
        Set<PatternNode> allPatternNodes = rule.getMatchPattern().getAllNodes();

        Optional<PatternNode> relevantPattern = allPatternNodes.stream()
                .filter(node -> node.getOperatorClass().isAssignableFrom(triggerOperator.getOperator().getClass()))
                .filter(node -> node.getPredicate().test(triggerOperator.getOperator()))
                .findAny();
        if (! relevantPattern.isPresent()) {
            return Optional.empty();
        }

        matchNode(triggerOperator, relevantPattern.get());

        if (matchFailed) {
            return Optional.empty();
        }

        BiMap<PatternNode, Integer> patternOrdinals = HashBiMap.create();
        int ordinal = 0;
        Queue<PatternNode> toVisit = new LinkedList<>();
        toVisit.add(rule.getMatchPattern());

        while (! toVisit.isEmpty()) {
            PatternNode patternNode = toVisit.poll();
            toVisit.addAll(patternNode.getChildren());
            patternOrdinals.put(patternNode, ordinal);
            ordinal++;
        }
        BiMap<Integer, OperatorNode> matchedOperators = HashBiMap.create();

        for (PatternNode patternNode : allPatternNodes) {
            Collection<OperatorNode> operatorNodes = this.lookupTable.get(patternNode);
            Verify.verify(!operatorNodes.isEmpty());
            if (operatorNodes.size() > 1) {
                throw new UnsupportedOperationException("matching multiple edu.ics.uci.regex.operators per pattern node not implemented");
            }
            OperatorNode matchedOperatorNode = operatorNodes.iterator().next();
            if (temporaryOperators.containsKey(matchedOperatorNode)) {
                matchedOperatorNode = temporaryOperators.get(matchedOperatorNode);
            }
             matchedOperators.put(patternOrdinals.get(patternNode), matchedOperatorNode);
        }

        RuleCall ruleCall = new RuleCall(planner, rule, matchedOperators);
        return Optional.of(ruleCall);
    }

    private void matchNode(OperatorNode operatorNode, PatternNode patternNode) {
        if (this.matchFailed) {
            return;
        }

        // match descending from this node
        boolean match = this.matchDescending(operatorNode, patternNode);

        if (! match) {
            this.matchFailed = true;
            return;
        }

        // match ascending from this node
        Optional<PatternNode> parentPattern = Optional.ofNullable(patternInverse.get(patternNode));
        if (! parentPattern.isPresent()) {
            return;
        }

        Collection<OperatorNode> parents = planner.getAndOrTree().getOperatorParents(operatorNode);

        boolean parentAnyMatch = false;
        for (OperatorNode parent: parents) {
            List<SubsetNode> tempInputs = parent.getInputs().stream().map(input ->
                input.getOperators().contains(operatorNode) ? SubsetNode.create(operatorNode) : input
            ).collect(toList());
            OperatorNode tempParent = OperatorNode.create(parent.getOperator(), parent.getTraitSet(), tempInputs);
            temporaryOperators.put(tempParent, parent);

            boolean parentMatch = this.matchDescending(tempParent, parentPattern.get());
            parentAnyMatch = parentAnyMatch || parentMatch;
        }

        if (!parentAnyMatch) {
            this.matchFailed = true;
        }
    }


    private boolean matchDescending(OperatorNode operatorNode, PatternNode patternNode) {
        // directly exit if the match has already failed
        if (matchFailed) {
            return false;
        }
        // we always match from top to bottom, therefore if this node is in the lookup table
        // then the whole subtree has already matched
        if (lookupTable.containsKey(patternNode) && lookupTable.get(patternNode).size() > 0) {
            return true;
        }
        // check if the operator matches the pattern's class and predicate
        if (! matchSelf(operatorNode, patternNode)) {
            this.matchFailed = true;
            return false;
        }

        // start matching the pattern's child
        boolean match = false;
        switch (patternNode.getChildrenPolicy()) {
            case ANY:
                // ANY have no children and "self" node is already matched, directly return true
                Verify.verify(patternNode.getChildren().isEmpty());
                Verify.verify(matchSelf(operatorNode, patternNode));

                match = true;
                break;
            case EXACT:
                if (operatorNode.getInputs().size() == patternNode.getChildren().size()) {
                    match = IntStream.range(0, operatorNode.getInputs().size()).allMatch(i ->
                            operatorNode.getInputs().get(i).getOperators().stream()
                                    .map(op -> matchDescending(op, patternNode.getChildren().get(i)))
                                    .reduce((r1, r2) -> r1 && r2).orElse(false)
                    );
                }
                break;
            default:
                throw new RuntimeException("matchDescending child policy not handled");
        }

        if (! match) {
            this.matchFailed = true;
        } else {
            lookupTable.put(patternNode, operatorNode);
        }
        return match;
    }

    /**
     * Checks if the operator matches the pattern's class and predicate condition
     */
    private static boolean matchSelf(OperatorNode operatorNode, PatternNode patternNode) {
        return patternNode.getOperatorClass().isAssignableFrom(operatorNode.getOperator().getClass())
                && patternNode.getPredicate().test(operatorNode.getOperator());
    }


}
