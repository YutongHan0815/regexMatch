package edu.ics.uci.optimizer.operator.schema;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;

public class Field implements Serializable {

    private final String name;
    private final FieldType type;

    public static Field of(String name, FieldType type) {
        return new Field(name, type);
    }

    private Field(String name, FieldType type) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name) &&
                Objects.equals(type, field.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

}
