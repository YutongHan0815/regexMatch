package plan;



import java.util.ArrayList;
import java.util.List;

public class AndOrTree {
    SetNode root;



    public  AndOrTree(){
        root = new SetNode();
    }

    public SetNode buildAndOrTree(){

        return root;
    }

    public void addNode(SetNode setNode){
        if(setNode.children == null){
            setNode.children = new ArrayList<>();
        }
        setNode.children.add(setNode);
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


}
