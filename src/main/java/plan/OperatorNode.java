package plan;

import com.google.common.collect.ImmutableList;
import operators.Operator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OperatorNode implements Serializable {

    private final Operator operator;
    private final List<SetNode> inputs;

    public static OperatorNode create(Operator operator) {
        return create(operator, new ArrayList<>());
    }

    public static OperatorNode create(Operator operator, SetNode input) {
        return create(operator, Collections.singletonList(input));
    }

    public static OperatorNode create(Operator operators, List<SetNode> inputs) {
        return new OperatorNode(operators, inputs);
    }

    public OperatorNode( Operator operator, List<SetNode> inputs) {
        this.operator = operator;
        this.inputs = ImmutableList.copyOf(inputs);
    }


    public <T extends Operator> T getOperator() {
        return (T) operator;
    }

    public List<SetNode> getInputs() {
        return inputs;
    }

}
