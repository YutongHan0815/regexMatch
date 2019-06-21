package edu.ics.uci.optimizer.operator.schema;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RowType {

    private final ImmutableList<Field> fields;

    public static RowType of(Field... fields) {
        return new RowType(ImmutableList.copyOf(fields));
    }

    public static RowType of(List<Field> fields) {
        return new RowType(ImmutableList.copyOf(fields));
    }

    private RowType(ImmutableList<Field> fields) {
        this.fields = fields;
    }

    public RowType append(Field field) {
        return new RowType(ImmutableList.<Field>builder().addAll(this.fields).add(field).build());
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<String> getFieldNames() {
        return this.fields.stream().map(f -> f.getName()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RowType rowType = (RowType) o;
        return Objects.equals(fields, rowType.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields);
    }
}
