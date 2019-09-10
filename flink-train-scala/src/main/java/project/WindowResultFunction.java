package project;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.scala.function.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import scala.collection.Iterable;

public class WindowResultFunction implements WindowFunction<Long, Object, Tuple, TimeWindow> {


    @Override
    public void apply( Tuple tuple, TimeWindow window,  Iterable<Long> input, Collector<Object> out) {

    }
}
