package plan;

import operators.Operator;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class OperatorInput implements Serializable {

    public Operator operator;
    public List<Operator> inputOperatorList;

    public OperatorInput(Operator operator, List<Operator> inputOperatorList) {
        this.operator = operator;
        this.inputOperatorList = inputOperatorList;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public List<Operator> getInputOperatorList() {
        return inputOperatorList;
    }

    public void setInputOperatorList(List<Operator> inputOperatorList) {
        this.inputOperatorList = inputOperatorList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperatorInput that = (OperatorInput) o;
        return Objects.equals(operator, that.operator) &&
                Objects.equals(inputOperatorList, that.inputOperatorList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, inputOperatorList);
    }
}
