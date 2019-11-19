package com.lorettax.analy.test;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class KafkaEventSchema implements DeserializationSchema<KafkaEvent>, SerializationSchema<KafkaEvent> {

    private static final long serialVersionUID = 6154188370181669758L;

    @Override
    public byte[] serialize(KafkaEvent event){
        return event.toString().getBytes();
    }

    @Override
    public KafkaEvent deserialize(byte[] bytes) throws IOException {
        return KafkaEvent.fromString(new String(bytes));
    }

    @Override
    public boolean isEndOfStream(KafkaEvent kafkaEvent) {
        return false;
    }

    @Override
    public TypeInformation<KafkaEvent> getProducedType() {
        return TypeInformation.of(KafkaEvent.class);
    }
}
