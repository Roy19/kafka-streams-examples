package com.aritra.kafkastreamswithcdc.joinsexample;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class JoinsExample {

    public static void main(String[] args) {
        Properties streamsConfigProperties = new Properties();
        streamsConfigProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "joins-example");
        streamsConfigProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamsConfigProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        streamsConfigProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JSONSerde.class);
        streamsConfigProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        streamsConfigProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JSONSerde.class);
        
        TopicProducer producer = new TopicProducer();
        producer.produceRecords(streamsConfigProperties, "users", "orders", "orders-users");
    }

}
