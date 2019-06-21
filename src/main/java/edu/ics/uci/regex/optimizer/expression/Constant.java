package edu.ics.uci.regex.optimizer.expression;

import java.util.Objects;

public class Constant implements ExprOperand {

    private final int value;

    public static Constant of(int value) {
        return new Constant(value);
    }

    private Constant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constant constant = (Constant) o;
        return value == constant.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Constant{" +
                "value=" + value +
                '}';
    }
}
