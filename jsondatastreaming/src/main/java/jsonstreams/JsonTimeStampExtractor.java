package jsonstreams;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonTimeStampExtractor implements TimestampExtractor {
	@Override
    public long extract(final ConsumerRecord<Object, Object> record, final long partitionTime) {
        if (record.value() instanceof PageView) {
            return ((PageView) record.value()).timestamp;
        }

        if (record.value() instanceof UserProfile) {
            return ((UserProfile) record.value()).timestamp;
        }

        if (record.value() instanceof JsonNode) {
            return ((JsonNode) record.value()).get("timestamp").longValue();
        }

        throw new IllegalArgumentException("JsonTimestampExtractor cannot recognize the record value " + record.value());
    }
}
