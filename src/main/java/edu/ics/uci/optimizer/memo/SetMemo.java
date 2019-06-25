package edu.ics.uci.optimizer.memo;

import edu.ics.uci.optimizer.operator.schema.RowType;

import java.io.Serializable;
import java.util.Optional;

public class SetMemo implements Serializable {

    private Integer rowCount;
    private RowType outputRowType;

    public static SetMemo create() {
        return new SetMemo();
    }

    private SetMemo() {}

    public Optional<Integer> getRowCount() {
        return Optional.ofNullable(rowCount);
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Optional<RowType> getOutputRowType() {
        return Optional.ofNullable(outputRowType);
    }

    public void setOutputRowType(RowType outputRowType) {
        this.outputRowType = outputRowType;
    }
}
