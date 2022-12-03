package wordcount;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WordCountTopicProducer {
	
	final Logger logger = Logger.getLogger(WordCountTopicProducer.class.getName());
	
	public void produceRecords(Properties kafkaProperties, String inputTopicName, String outputTopicName) {
		try (Admin adminClient = Admin.create(kafkaProperties);
				KafkaProducer<Integer, String> producer = new KafkaProducer<>(kafkaProperties)) {
			
			List<NewTopic> topics = List.of(new NewTopic(inputTopicName, 1, (short) 1), 
					new NewTopic(outputTopicName, 1, (short) 1));
			adminClient.createTopics(topics);
			
			Callback callback = (metadata, exception) -> {
				if (exception == null) {
					logger.log(Level.INFO, "Successfully pushed messages to Kafka. Offset:" + metadata.offset() + " Timestamp: " + metadata.timestamp());;
				} else {
					logger.log(Level.SEVERE, "Failed to produce message " + exception.getMessage());
				}
			};
			
			String[] sentenceStrings = {"Hello world", 
				"Hello world new", 
				"sentence", 
				"what a lovely day today",
				"how nice to see you",
				"how are you doing today"};
		
			for (int i = 0; i < 1000; i++) {
				String sentenceString = sentenceStrings[(int)(Math.random() * sentenceStrings.length)];
				producer.send(new ProducerRecord<Integer, String>(inputTopicName, 
				i, sentenceString), callback);
			}
		}
	}
	
}
