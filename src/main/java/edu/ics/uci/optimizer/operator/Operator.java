package edu.ics.uci.optimizer.operator;


import edu.ics.uci.optimizer.operator.schema.RowType;

import java.util.List;

public interface Operator {

    RowType deriveRowType(List<RowType> inputRowTypeList);

}
