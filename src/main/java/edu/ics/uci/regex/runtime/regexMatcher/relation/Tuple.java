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
    private final ImmutableList<Span> spans;

    public Tuple(RowType rowType, Span... spans) {
        this(rowType, Arrays.asList(spans));
    }

    public Tuple(RowType rowType, List<Span> spans) {
        Preconditions.checkNotNull(rowType);
        Preconditions.checkNotNull((spans));

        this.rowType = rowType;
        this.spans = ImmutableList.copyOf(spans);
    }

    public RowType getRowType() {
        return rowType;
    }

    public List<Span> getFields() {
        return spans;
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
                Objects.equals(spans, tuple.spans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowType, spans);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "rowType=" + rowType +
                ", fields=" + spans +
                '}';
    }

    public static class Builder {
       private final RowType.Builder rowTypeBuilder;
       private final HashMap<String, Span> spanNameMap;

       public Builder() {
           this.rowTypeBuilder = new RowType.Builder();
           this.spanNameMap = new HashMap<>();
       }

       public Builder(Tuple tuple) {
           Preconditions.checkNotNull(tuple);
           Preconditions.checkNotNull(tuple.getFields());

           this.rowTypeBuilder = new RowType.Builder(tuple.getRowType());
           this.spanNameMap = new HashMap<>();

           for (int i = 0; i < tuple.getFields().size(); i++) {
               this.spanNameMap.put(
                       tuple.getRowType().getFields().get(i).getName().toLowerCase(),
                       tuple.spans.get(i));
           }
       }

       public Tuple build() {
           RowType rowType = rowTypeBuilder.build();
           ArrayList<Span> spans = new ArrayList<>();
           for (int i = 0; i < rowType.getFields().size(); i++) {
               spans.add(spanNameMap.get(rowType.getFields().get(i).getName().toLowerCase()));
           }
           return new Tuple(rowType, spans);

       }
    }
}
