package jsonstreams;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;

public class JsonStreams {

	static final Logger LOGGER = Logger.getLogger(JsonStreams.class.getName());
	
	public static void main(String[] args) {
		final Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "json-streams");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, JsonTimeStampExtractor.class);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, JSONSerde.class);
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JSONSerde.class);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JSONSerde.class);
		
		
		final StreamsBuilder builder = new StreamsBuilder();
		
		final KStream<String, PageView> viewStream = builder.stream("stream-pageview-input", Consumed.with(Serdes.String(), new JSONSerde<>()));
		final KTable<String, UserProfile> userProfileTable = builder.table("streams-userprofile-input", Consumed.with(Serdes.String(), new JSONSerde<>()));
		
		final Duration duration = Duration.ofHours(24);
		
		final KStream<WindowedPageViewByRegion, RegionCount> regionCountStream = viewStream
				.leftJoin(userProfileTable , (pageview, userprofile) -> {
					final PageViewByRegion pageViewByRegion = new PageViewByRegion();
					pageViewByRegion.user = pageview.user;
					pageViewByRegion.page = pageview.page;
					
					if (userprofile != null) {
						pageViewByRegion.region = ((UserProfile)userprofile).region;
					} else {
						pageViewByRegion.region = "UNKNOWN";
					}
					
					return pageViewByRegion;
				}).map((user, viewByRegion) -> new KeyValue<>(viewByRegion.region, viewByRegion))
				.groupByKey(Grouped.with(Serdes.String(), new JSONSerde<>()))
				.windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofDays(7), duration).advanceBy(Duration.ofSeconds(1)))
	            .count()
	            .toStream()
	            .map((key, value) -> {
	                final WindowedPageViewByRegion wViewByRegion = new WindowedPageViewByRegion();
	                wViewByRegion.windowStart = key.window().start();
	                wViewByRegion.region = key.key();

	                final RegionCount rCount = new RegionCount();
	                rCount.region = key.key();
	                rCount.count = value;

	                return new KeyValue<>(wViewByRegion, rCount);
	            });
		regionCountStream.to("streams-pageviewstats-typed-output", Produced.with(new JSONSerde<>(), new JSONSerde<>()));
		
		final KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
		final CountDownLatch latch = new CountDownLatch(1);
		
		Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
			@Override
			public void run() {
				kafkaStreams.close();
				latch.countDown();
			}
		});
		
		try {
			JsonStreamsTopicProducer jsonStreamsTopicProducer = new JsonStreamsTopicProducer();
			jsonStreamsTopicProducer.produceRecords(props, "stream-pageview-input", "streams-userprofile-input", "streams-pageviewstats-typed-output");
			kafkaStreams.start();
			latch.await();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to start streams, exception: "+ e.getMessage());
		}
	}
	
}
