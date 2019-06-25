package edu.ics.uci.regex.optimizer.expression;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Expression extends ExprOperand {

    ExprOperator getOperator();

    List<ExprOperand> getOperands();

    Expression copyWithNewOperands(List<ExprOperand> newOperands);

    @Override
    default void accept(Consumer<ExprOperand> visitor) {
        getOperands().forEach(operand -> operand.accept(visitor));
        visitor.accept(this);
    }

    @Override
    default ExprOperand transform(Function<ExprOperand, ExprOperand> visitor) {
        getOperands().stream().map(operand -> operand.transform(visitor)).collect(Collectors.toList());

        return visitor.apply(this);
    }

}
