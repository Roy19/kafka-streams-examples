package com.aritra.kafkastreamswithcdc.joinsexample;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@SuppressWarnings("DefaultAnnotationParam") // being explicit for the example
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "_t")
@JsonSubTypes({
        @JsonSubTypes.Type(value = User.class, name = "u"),
        @JsonSubTypes.Type(value = Order.class, name = "o")
})
public interface JSONSerdeCompatible {
}
