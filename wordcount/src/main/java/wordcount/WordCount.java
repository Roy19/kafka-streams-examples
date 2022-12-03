package wordcount;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

public class WordCount {
	
	static final Logger logger = Logger.getLogger(WordCount.class.getName());
	
	public static void main(String[] args) throws InterruptedException {
		Properties streamsConfigProperties = new Properties();
		streamsConfigProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-example");
		streamsConfigProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		streamsConfigProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, StringSerde.class);
		streamsConfigProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StringSerde.class);
		streamsConfigProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        streamsConfigProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, String> kStreams = builder.stream("wordcount-input", 
				Consumed.with(Serdes.String(), Serdes.String()));
		
		final Pattern spacesInBetweenPattern = Pattern.compile("\\W+");
		
		kStreams.flatMapValues(value -> Arrays.asList(spacesInBetweenPattern.split(value.toLowerCase())))
			.map((key, value) -> new KeyValue<Object, Object>(value, value))
			.groupByKey()
			.count(Materialized.as("CountStore"))
			.mapValues(value -> Long.toString(value))
			.toStream()
			.map((key, value) -> new KeyValue<String, String>((String) key, value))
			.peek((key, value) -> logger.log(Level.INFO, "Got key: " + key, " , value: " + value))
			.to("wordcount-output", Produced.with(Serdes.String(), Serdes.String()));
		
		KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), streamsConfigProperties);
		CountDownLatch latch = new CountDownLatch(1);
		WordCountTopicProducer producer = new WordCountTopicProducer();
		producer.produceRecords(streamsConfigProperties, "wordcount-input", "wordcount-output");
		
		Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
			@Override
			public void run() {
				kafkaStreams.close();
				latch.countDown();
			}
		});
		
		kafkaStreams.start();
		latch.await();
	}
}
