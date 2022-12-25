package com.aritra.kafkastreamswithcdc.cdcaggregation.serdes;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDeserializer<T> implements Deserializer<T> {

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
        throw new SerializationException("Error deserializing JSON message", e1);
      } 
      return result;
    }

    @Override
    public void close() {
    }
    
}
