package com.aritra.kafkastreamswithcdc.joinsexample;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@SuppressWarnings("DefaultAnnotationParam") // being explicit for the example
@JsonSubTypes({
        @JsonSubTypes.Type(value = User.class, name = "user"),
        @JsonSubTypes.Type(value = Order.class, name = "order")
})
public interface JSONSerdeCompatible {
}
