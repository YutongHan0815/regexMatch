package plan;

import java.io.Serializable;

public class LogicalVerifyOperator implements Operator, Serializable {

    @Override
    public int hashOperator() {
        return hashCode();
    }

}
