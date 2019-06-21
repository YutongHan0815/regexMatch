package edu.ics.uci.regex.optimizer.expression;

import java.util.List;

public interface Expression extends ExprOperand {

    ExprOperator getOperator();

    List<ExprOperand> getOperands();

}
