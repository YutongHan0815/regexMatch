package plan;




import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SetNode implements Serializable {

    private int nodeId;
    private int nextId = 0;
    public List<OperatorInput> operatorList;
    private List<SetNode> children;
    private SetNode parentSetNode;

    public SetNode() {
        this.nodeId = nextId++;
        initOperatorList();
        initChildList();
    }
    public SetNode(SetNode parentSetNode) {
        this.nodeId = nextId++;
        initOperatorList();
        initChildList();
        this.parentSetNode = parentSetNode;
    }

    public SetNode(List<OperatorInput> operatorList,
                   List<SetNode> children, SetNode parentSetNode) {
        nodeId = nextId++;
        this.operatorList = operatorList;
        this.children = children;
        this.parentSetNode = parentSetNode;
    }

    public  void initChildList() {
        if(children == null)
            children = new ArrayList<>();
    }
    public  void initOperatorList() {
        if(operatorList == null)
            operatorList = new ArrayList<>();
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        nodeId = nodeId;
    }

    public List<OperatorInput> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(List<OperatorInput> operatorList) {
        this.operatorList = operatorList;
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

    public void addNode(SetNode setNode){

        setNode.getChildren().add(setNode);
    }
    public void deleteNode(SetNode setNode){
        SetNode parentSetNode = setNode.getParentSetNode();
        int id = setNode.getNodeId();

        if (parentSetNode != null) {
            deleteChildNode(setNode);
        }
    }

    private void deleteChildNode(SetNode setNode) {
        List<SetNode> childList = setNode.getChildren();
        int childNumber = childList.size();
        for (int i = 0; i < childNumber; i++) {
            SetNode child = childList.get(i);
            if (child.getNodeId() == setNode.getNodeId()) {
                childList.remove(i);
                return;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetNode setNode = (SetNode) o;
        return nodeId == setNode.nodeId &&
                Objects.equals(operatorList, setNode.operatorList) &&
                Objects.equals(children, setNode.children) &&
                Objects.equals(parentSetNode, setNode.parentSetNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, operatorList, children, parentSetNode);
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
