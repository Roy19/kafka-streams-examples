package com.aritra.kafkastreamswithcdc.joinsexample;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class JoinsExample {

    public static void main(String[] args) {
        Properties streamsConfigProperties = new Properties();
        streamsConfigProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "joins-example");
        streamsConfigProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamsConfigProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        streamsConfigProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JSONSerde.class);
        streamsConfigProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        streamsConfigProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JSONSerde.class);
                
        StreamsBuilder builder = new StreamsBuilder();
        
        KTable<Integer, User> usersTable = builder.table("users", Consumed.with(Serdes.Integer(), new JSONSerde()));
        KTable<Integer, Order> ordersTable = builder.table("orders", Consumed.with(Serdes.Integer(), new JSONSerde()));
        
        final KTable<Integer, UserOrder> userOrderTable = ordersTable.join(usersTable, Order::getUserid, (order, user) -> {
        	return new UserOrder(user.getId(), order.getOrderid(), user.getUsername(), order.getItemName());
        });
        
        userOrderTable.toStream().to("orders-users", Produced.with(Serdes.Integer(), new JSONSerde()));
        
        final KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), streamsConfigProperties);
        final CountDownLatch latch = new CountDownLatch(1);
        
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
			@Override
			public void run() {
				kafkaStreams.close();
				latch.countDown();
			}
		});
        
        try {
            TopicProducer producer = new TopicProducer();
            producer.produceRecords(streamsConfigProperties, "users", "orders", "orders-users");
        	
        	kafkaStreams.start();
        	latch.await();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }

}
