package edu.ics.uci.optimizer.rule;

import java.util.List;

public class PatternTree {

    private int solveOrdinal;
    private PatternNode patternNode;
    private List<PatternTree> children;

    public PatternTree(int solveOrdinal, PatternNode patternNode, List<PatternTree> children) {
        this.solveOrdinal = solveOrdinal;
        this.patternNode = patternNode;
        this.children = children;
    }
}
