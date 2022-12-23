package com.aritra.kafkastreamswithcdc.cdcaggregation.serdes;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class SerdeFactory {
    public static <T> Serde<T> createSerdeFromType(Class<T> clazz, boolean isKey) {
        Map<String, Object> serdeProps = new HashMap<>();
        serdeProps.put("serializedClass", clazz);

        Serializer<T> serializer = new JsonSerializer<>();
        serializer.configure(serdeProps, isKey);
        Deserializer<T> deserializer = new JsonDeserializer<>();
        deserializer.configure(serdeProps, isKey);
        
        return Serdes.serdeFrom(serializer, deserializer);
    }
}