package plan;


import operators.Operator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SetNode implements Serializable {

    private int NodeId;
    public List<Operator> operatorVector;
    protected  List<SetNode> children;
    protected SetNode parentSetNode;


    public SetNode(int nodeId, Operator operator){
        this.NodeId = nodeId;
        this.parentSetNode = parentSetNode;
        this.children = null;
        this.operatorVector = new ArrayList<>();
        operatorVector.add(operator);

    }

    public int getNodeId() {
        return NodeId;
    }

    public void setNodeId(int nodeId) {
        NodeId = nodeId;
    }

    public List<SetNode> getChildren() {
        return children;
    }

    public void setChildren(List<SetNode> children) {
        this.children = children;
    }

    public SetNode getParentSetNode() {
        return parentSetNode;
    }

    public void setParentSetNode(SetNode parentSetNode) {
        this.parentSetNode = parentSetNode;
    }

    public boolean isLeafNode() {
        if (this.children == null) {
            return true;
        } else{
                if (this.children.isEmpty())
                    return true;
                else
                    return false;
            }

    }




}
