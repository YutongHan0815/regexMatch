package edu.ics.uci.regex.runtime.regexMatcher.relation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import edu.ics.uci.optimizer.operator.schema.Field;
import edu.ics.uci.optimizer.operator.schema.FieldType;
import edu.ics.uci.optimizer.operator.schema.RowType;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class Tuple implements Serializable {
    private final RowType rowType;
    private final ImmutableList<Field> fields;

    public Tuple(RowType rowType, Field... fields) {
        this(rowType, Arrays.asList(fields));
    }

    public Tuple(RowType rowType, List<Field> fields) {
        Preconditions.checkNotNull(rowType);
        Preconditions.checkNotNull((fields));

        this.rowType = rowType;
        this.fields = ImmutableList.copyOf(fields);
    }

    public RowType getRowType() {
        return rowType;
    }

    public List<Field> getFields() {
        return fields;
    }

    public Field getField(String fieldName) {
        return getField(fieldName);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Objects.equals(rowType, tuple.rowType) &&
                Objects.equals(fields, tuple.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowType, fields);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "rowType=" + rowType +
                ", fields=" + fields +
                '}';
    }

    public static class Builder {
       private final RowType.Builder rowTypeBuilder;
       private final HashMap<String, Field> fieldNameMap;

       public Builder() {
           this.rowTypeBuilder = new RowType.Builder();
           this.fieldNameMap = new HashMap<>();
       }

       public Builder(Tuple tuple) {
           Preconditions.checkNotNull(tuple);
           Preconditions.checkNotNull(tuple.getFields());

           this.rowTypeBuilder = new RowType.Builder(tuple.getRowType());
           this.fieldNameMap = new HashMap<>();

           for (int i = 0; i < tuple.getFields().size(); i++) {
               this.fieldNameMap.put(
                       tuple.getRowType().getFields().get(i).getName().toLowerCase(),
                       tuple.fields.get(i));
           }
       }

       public Tuple build() {
           RowType rowType = rowTypeBuilder.build();
           ArrayList<Field> fields = new ArrayList<>();
           for (int i = 0; i < rowType.getFields().size(); i++) {
               fields.add(fieldNameMap.get(rowType.getFields().get(i).getName().toLowerCase()));
           }
           return new Tuple(rowType, fields);

       }
    }
}
