package plan;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AndOrTree implements Serializable {
    public SetNode root;

    public AndOrTree() {
        this.root = new SetNode();
    }

    public SetNode getRoot() {
        return root;
    }

    public void setRoot(SetNode root) {
        this.root = root;
    }


}
