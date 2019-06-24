package edu.ics.uci.optimizer.operator.schema;

public class SpanType implements FieldType {

    public static SpanType SPAN_TYPE = new SpanType();


    @Override
    public String getTypeName() {
        return "Span";
    }


}
