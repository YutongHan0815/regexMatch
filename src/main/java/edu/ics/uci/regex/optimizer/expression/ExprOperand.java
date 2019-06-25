package edu.ics.uci.regex.optimizer.expression;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ExprOperand {

    default void accept(Consumer<ExprOperand> visitor) {
        visitor.accept(this);
    }

    default ExprOperand transform(Function<ExprOperand, ExprOperand> visitor) {
        return visitor.apply(this);
    }

}
