package com.lorettax.analy.tasks;

import com.lorettax.analy.Mockcommon.ErrorInfo;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.WatermarkAndTimestamp.CustomWatermarkExtractor;
import com.lorettax.analy.maps.ErrorMap;
import com.lorettax.analy.maps.StartupMap;
import com.lorettax.analy.reduces.ErrorReduce;
import com.lorettax.analy.reduces.StartupReduce;
import com.lorettax.analy.sinks.ErrorInfoSink;
import com.lorettax.analy.sinks.StartupSink;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.scala.DataStream;
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

public class ErrorInfoAnaly {
    public static void main(String[] args) {
        args = new String[]{"--input-topic","errorinfo","--bootstrap.servers","127.0.0.1:9092","--zookeeper.connect","127.0.0.1:2181","--group.id","myconsumer"};
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);
        //		if (parameterTool.getNumberOfParameters() < 5) {
//			System.out.println("Missing parameters!\n" +
//					"Usage: Kafka --input-topic <topic> --output-topic <topic> " +
//					"--bootstrap.servers <kafka brokers> " +
//					"--zookeeper.connect <zk quorum> --group.id <some id>");
//			return;
//		}

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().disableSysoutLogging();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4,10000));
        env.enableCheckpointing(5000);// create a checkpoint every 5 seconds
        env.getConfig().setGlobalJobParameters(parameterTool);// make parameters available in the web interface
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);


        DataStream<KafkaEvent> input = env.addSource(new FlinkKafkaConsumer010<KafkaEvent>(parameterTool.getRequired("input_topic"),
                (DeserializationSchema<KafkaEvent>) new KafkaEventSchema(),
                parameterTool.getProperties())
                .assignTimestampsAndWatermarks(new CustomWatermarkExtractor()));

        DataStream<ErrorInfo> errorInfoMap = input.map(new ErrorMap());

        DataStream<ErrorInfo> errorInfoReduce = errorInfoMap
                .keyBy("groupbyfield")
                .timeWindowAll(Time.seconds(2))
                .reduce(new ErrorReduce());

        errorInfoReduce.addSink(new ErrorInfoSink());

        env.execute("errorinfo analy");
    }
}
