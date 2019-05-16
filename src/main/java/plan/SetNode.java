package plan;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SetNode implements Serializable {

    private final List<OperatorNode> operatorNodes;

    public static SetNode create(OperatorNode operatorNode) {
        return create(Collections.singletonList(operatorNode));
    }

    public static SetNode create(List<OperatorNode> operatorNodes) {
        return new SetNode(operatorNodes);
    }

    private SetNode(List<OperatorNode> operatorNodes) {
        this.operatorNodes = new ArrayList<>(operatorNodes);
    }

    public void addOperatorNode(OperatorNode operatorNode) {
        Preconditions.checkNotNull(operatorNode);
        this.operatorNodes.add(operatorNode);
    }

    public List<OperatorNode> getOperators() {
        return new ArrayList<>(operatorNodes);
    }
}
