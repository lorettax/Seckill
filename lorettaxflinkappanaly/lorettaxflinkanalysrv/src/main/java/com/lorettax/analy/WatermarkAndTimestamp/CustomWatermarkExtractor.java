package com.lorettax.analy.WatermarkAndTimestamp;

//import com.lorettax.analy.test.KafkaEvent;
import com.lorettax.analy.tasks.KafkaEvent;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

public class CustomWatermarkExtractor implements AssignerWithPeriodicWatermarks<KafkaEvent> {

    private static final long serialVersionUID = -742759155861320823L;
    private long currentTimestamp = Long.MIN_VALUE;

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        return new Watermark(currentTimestamp == Long.MIN_VALUE?Long.MIN_VALUE : currentTimestamp -1);
    }

    @Override
    public long extractTimestamp(KafkaEvent kafkaEvent, long l) {
        this.currentTimestamp = kafkaEvent.getTimestamp();
        return kafkaEvent.getTimestamp();
    }
}
