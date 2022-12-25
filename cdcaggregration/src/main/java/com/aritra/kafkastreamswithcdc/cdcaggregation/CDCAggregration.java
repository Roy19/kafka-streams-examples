package com.aritra.kafkastreamswithcdc.cdcaggregation;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import com.aritra.kafkastreamswithcdc.cdcaggregation.models.Order;
import com.aritra.kafkastreamswithcdc.cdcaggregation.models.OrderAndUser;
import com.aritra.kafkastreamswithcdc.cdcaggregation.models.User;
import com.aritra.kafkastreamswithcdc.cdcaggregation.models.UserOrders;
import com.aritra.kafkastreamswithcdc.cdcaggregation.serdes.SerdeFactory;

import io.debezium.serde.DebeziumSerdes;

public class CDCAggregration {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "cdc-aggregation");
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 20*1024);
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
        properties.put(CommonClientConfigs.METADATA_MAX_AGE_CONFIG, 500);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final Serde<Integer> defaultKeySerde = DebeziumSerdes.payloadJson(Integer.class);
        defaultKeySerde.configure(Collections.emptyMap(), true);
        final Serde<User> userSerde = DebeziumSerdes.payloadJson(User.class);
        userSerde.configure(Collections.singletonMap("from.field", "payload"), false);
        final Serde<Order> orderSerde = DebeziumSerdes.payloadJson(Order.class);
        orderSerde.configure(Collections.singletonMap("from.field", "payload"), false);
        final Serde<OrderAndUser> orderAndUserSerde = SerdeFactory.createSerdeFromType(
            OrderAndUser.class,
            false);
        final Serde<UserOrders> userOrdersSerde = SerdeFactory.createSerdeFromType(
            UserOrders.class, 
            false);

        StreamsBuilder builder = new StreamsBuilder();

        final KTable<Integer, Order> ordersTable = builder.table("db_users_orders.public.orders", 
                            Consumed.with(defaultKeySerde, orderSerde));
        final KTable<Integer, User> usersTable = builder.table("db_users_orders.public.users", 
                            Consumed.with(defaultKeySerde, userSerde));
        
        final KTable<Integer, UserOrders> userOrdersTable = ordersTable.join(usersTable, 
                (order) -> order.user_id, OrderAndUser::new, 
                Materialized.with(Serdes.Integer(), orderAndUserSerde))
            .groupBy((orderId, orderAndUser) -> 
                KeyValue.pair(orderAndUser.user.id, orderAndUser),
                Grouped.with(Serdes.Integer(), orderAndUserSerde))
            .aggregate(UserOrders::new,
                (userId, orderAndUser, aggregate) -> aggregate.addOrder(orderAndUser), 
                (userId, orderAndUser, aggregate) -> aggregate.removeOrder(orderAndUser), 
                Materialized.with(Serdes.Integer(), userOrdersSerde));

        userOrdersTable.toStream().to("user-orders-aggregate", 
            Produced.with(Serdes.Integer(), userOrdersSerde));

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), properties);
        CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("cdc-aggregation-shutdown-hook") {
            @Override
            public void run() {
                kafkaStreams.close();
                latch.countDown();
            }
        });

        try {
            kafkaStreams.start();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
