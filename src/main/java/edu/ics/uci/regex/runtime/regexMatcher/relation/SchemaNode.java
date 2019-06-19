package edu.ics.uci.regex.runtime.regexMatcher.relation;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchemaNode implements Serializable {
    private final String name;
    private final List<SchemaNode> childrenNode;


    public static SchemaNode create(String name) {
        return new SchemaNode(name, new ArrayList<>());
    }

    public SchemaNode(String name, List<SchemaNode> childrenNode) {
        this.name = name;
        this.childrenNode = childrenNode;
    }


    public List<SchemaNode> getChildrenNode() {
        return childrenNode;
    }

    public String getName() {
        return name;
    }
}
