package com.aritra.kafkastreamswithcdc.cdcaggregation.serdes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.aritra.kafkastreamswithcdc.cdcaggregation.enums.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDeserializer<T> implements Deserializer<T> {

    private static final String DBZ_CDC_EVENT_PAYLOAD_FIELD = "payload";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    @Override
    public void configure(Map<String, ?> props, boolean isKey) {
        clazz = (Class<T>)props.get("serializedClass");
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        T result;
        try {
            result = objectMapper.readValue(data, clazz);
        } catch (IOException e1) {
          try {
            // deserialize the payload field of the Debezium CDC event
            Map map = (Map)objectMapper.readValue(data, Map.class)
                                                .get(DBZ_CDC_EVENT_PAYLOAD_FIELD);
            
            if (map == null) {
                map = new HashMap<>();
                map.put("_eventType", EventType.DELETE);
            }

            result = objectMapper.readValue(objectMapper.writeValueAsBytes(map), clazz);
          } catch (IOException e2) {
            throw new SerializationException("Error deserializing JSON message", e2);
          }
        } 

        return result;
    }

    @Override
    public void close() {
    }
    
}
