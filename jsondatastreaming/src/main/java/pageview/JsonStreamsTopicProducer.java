package pageview;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class JsonStreamsTopicProducer {
	final Logger logger = Logger.getLogger(JsonStreamsTopicProducer.class.getName());
	
	public void produceRecords(Properties kafkaProperties, String pageViewTopicName, String userProfileTopicName, String outputTopicName) {
		try (Admin adminClient = Admin.create(kafkaProperties);
				KafkaProducer<String, PageView> producerPageView = new KafkaProducer<>(kafkaProperties);
				KafkaProducer<String, UserProfile> producerUserProfile = new KafkaProducer<>(kafkaProperties)) {
			
			List<NewTopic> topics = List.of(new NewTopic(pageViewTopicName, 1, (short) 1), 
					new NewTopic(userProfileTopicName, 1, (short) 1),
					new NewTopic(outputTopicName, 1, (short) 1));
			adminClient.createTopics(topics);
			
			Callback callback = (metadata, exception) -> {
				if (exception == null) {
					logger.log(Level.INFO, "Successfully pushed messages to Kafka. Offset:" + metadata.offset() + " Timestamp: " + metadata.timestamp());;
				} else {
					logger.log(Level.SEVERE, "Failed to produce message " + exception.getMessage());
				}
			};
			
			String[] pages = {"Home Page", 
				"Product catalog", 
				"About", 
				"Customer Support Contact",
				"FAQs"};
			String[] users = {
				"Deepak",
				"Suraj",
				"Rishabh",
				"Aritra"
			};
			String[] regions = {
					"Mumbai",
					"Delhi",
					"Kolkata",
					"Hyderabad"
			};
			
			Instant instant = Instant.now();
		
			for (int i = 0; i < 1000; i++) {
				PageView pageView = new PageView();
				UserProfile userProfile = new UserProfile();
				
				userProfile.region = regions[(int)(Math.random() * regions.length)];
				userProfile.timestamp = instant.toEpochMilli(); 
				
				pageView.page = pages[(int)(Math.random() * pages.length)];
				pageView.user = users[(int)(Math.random() * users.length)];
				pageView.timestamp = instant.toEpochMilli();
				
				producerPageView.send(new ProducerRecord<String, PageView>(pageViewTopicName, 
						pageView.user, pageView), callback);
				producerUserProfile.send(new ProducerRecord<String, UserProfile>(userProfileTopicName, 
						userProfile.region, userProfile), callback);
				
				instant = instant.plus(15L, ChronoUnit.MINUTES);
			}
		}
	}
}
