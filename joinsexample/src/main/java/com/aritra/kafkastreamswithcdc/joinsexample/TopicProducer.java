package com.aritra.kafkastreamswithcdc.joinsexample;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class TopicProducer {
	
	final Logger logger = Logger.getLogger(TopicProducer.class.getName());
	
	public void produceRecords(Properties properties, String usersTopic, String ordersTopic, String combinedOutputTopic) {
	
		try(Admin adminClient = Admin.create(properties); 
				KafkaProducer<Integer, User> userProducer = new KafkaProducer<>(properties);
				KafkaProducer<Integer, Order> orderProducer = new KafkaProducer<>(properties)) {
			List<NewTopic> topics = List.of(new NewTopic(combinedOutputTopic, 1, (short) 1),
					new NewTopic(usersTopic, 1, (short) 1),
					new NewTopic(ordersTopic, 1, (short) 1));
			
			adminClient.createTopics(topics);
			
			Callback callback = (metadata, exception) -> {
				if (exception == null) {
					logger.log(Level.INFO, "Successfully pushed messages to Kafka. Offset:" + metadata.offset() + " Timestamp: " + metadata.timestamp());;
				} else {
					logger.log(Level.SEVERE, "Failed to produce message " + exception.getMessage());
				}
			};
			
			
			List<User> users = List.of(
					new User(1, "Amit"), new User(2, "Aritra"), new User(3, "Deepak"), new User(4, "Rehant"));
			List<Order> orders = List.of(
					new Order(10, 1, "iPhone"), new Order(11, 1, "Apple Care"), new Order(12, 2, "Android"), 
					new Order(13, 3, "Backcover"), new Order(14, 4, "Charger"));
			
			for (User user : users) {
				userProducer.send(new ProducerRecord<Integer, User>(usersTopic, user.getId(), user), callback);
			}
			
			for (Order order : orders) {
				orderProducer.send(new ProducerRecord<Integer, Order>(ordersTopic, order.getOrderid(), order), callback);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to produce messages. Error " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}
