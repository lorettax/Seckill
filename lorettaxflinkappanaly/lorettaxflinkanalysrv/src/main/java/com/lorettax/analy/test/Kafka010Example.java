package com.lorettax.analy.test;

import com.lorettax.analy.WatermarkAndTimestamp.CustomWatermarkExtractor;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;

public class Kafka010Example {

    public static void main(String[] args) throws Exception {

        args = new String[]{
                "--input-topic","test1",
                "--output-topic","test2",
                "--bootstrap.servers","127.0.0.1:9092",
                "--zookeeper.connect","127.0.0.1:2181",
                "--group.id","myconsumer"
        };

        final ParameterTool parameterTool = ParameterTool.fromArgs(args);

        if (parameterTool.getNumberOfParameters()< 5) {
            System.out.println("Missing parameters!\n" +
                    "Usage: Kafka --input-topic <topic> --output-topic <topic> " +
                    "--bootstrap.servers <kafka brokers> " +
                    "--zookeeper.connect <zk quorum> --group.id <some id>");
            return;
        }

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().disableSysoutLogging();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
        env.enableCheckpointing(5000);
        env.getConfig().setGlobalJobParameters(parameterTool);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<KafkaEvent> input = env.addSource(
                new FlinkKafkaConsumer010<Object>(
                        parameterTool.getRequired("output-topic"),
                        new KafkaEventSchema(),
                        parameterTool.getProperties())
                .assignTimestampsAndWatermarks(new CustomWatermarkExtractor()))
                .keyBy("word")
                .map(new RollingAdditionMapper());

        input.addSink(new FlinkKafkaProducer010<KafkaEvent>(
                parameterTool.getRequired("output-topic"),
                new KafkaEventSchema(),
                parameterTool.getProperties()
        ));


        env.execute("Kafka010Example");
    }

    /**
     * A {@link RichMapFunction} that continuously outputs the current total frequency count of a key.
     * The current total count is keyed state managed by Flink.
     */
    private static class RollingAdditionMapper extends RichMapFunction<KafkaEvent, KafkaEvent> {

        private static final long serialVersionUID = 1180234853172462378L;

        private transient ValueState<Integer> currentTotalCount;

        @Override
        public KafkaEvent map(KafkaEvent event) throws Exception {
            Integer totalCount = currentTotalCount.value();
            System.out.println("哈哈--");
            if (totalCount == null) {
                totalCount = 0;
            }
            totalCount += event.getFrequency();

            currentTotalCount.update(totalCount);

            return new KafkaEvent(event.getWord(), totalCount, event.getTimestamp());
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            currentTotalCount = getRuntimeContext().getState(new ValueStateDescriptor<>("currentTotalCount", Integer.class));
        }
}
