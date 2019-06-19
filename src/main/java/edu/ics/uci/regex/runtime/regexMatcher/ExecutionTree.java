package edu.ics.uci.regex.runtime.regexMatcher;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ExecutionTree implements Serializable {
    private final Map<Integer,ExecutionNode> executionNodeMap = new HashMap<>();
    private Multimap<ExecutionNode, ExecutionNode> parentChildMap = HashMultimap.create();

    public void addNode(ExecutionNode parent, ExecutionNode child) {
        this.parentChildMap.put(parent, child);
    }
}
