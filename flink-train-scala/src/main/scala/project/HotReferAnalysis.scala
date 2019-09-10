package project

import java.text.SimpleDateFormat
import java.util.Properties

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.runtime.executiongraph.Execution
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.util.Collector
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

object HotReferAnalysis {
  def main(args: Array[String]): Unit = {
    import org.apache.flink.api.scala._
    val env:StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //--设定时间特征
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)



    val topic = "pktest"
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "1.1.1.21:9092")
    properties.setProperty("group.id","test-pk-group")
    val consumer = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),properties)
    val data = env.addSource(consumer)
    //    getRuntimeContext.addAccumulator("ele-counter-scala",counter)
    //2.注册 计数器

    val logdata = data.map(
      x =>{
        val splits = x.split("\t")
        val cdn_count = splits(0)
        val level = splits(1)
        val trffic = splits(2)
        val timeStr = splits(3)
        val ips = splits(4)
        val domain = splits(5)

        (cdn_count,level,trffic,timeStr,ips,domain,1)
      })
    //    logdata.print()

    //访问次数：针对网址，从地区和运营商的角度看--；
    val view_count = logdata.map{x=>(x._6,x._7)}.keyBy(0)
      .timeWindow(Time.hours(24),Time.seconds(10))
      .sum(1)//(String,Long)

    view_count.print()
    //访问占比：     访问占比= 访问次数 / 全局总流量
    //--全局总流量
    val view_agg = logdata.map{x=>(x._1,x._7)}.keyBy(0)
      .timeWindow(Time.hours(24),Time.seconds(10))
      .sum(1) //--(String,Long)
//    view_agg.print()

    //--流量占比: 运营商
    val refer_ll_bl = logdata.map{x=>(x._3,x._7)}.keyBy(0)
      .timeWindow(Time.hours(24),Time.seconds(10))
      .sum(1)//--(String,Long)
    //    refer_ll_bl.print()

    val logData = view_count.union(view_agg).union(refer_ll_bl).print().setParallelism(1)
    //--水印和时间戳（周期性生成）
//    val resultdata = logdata.assignTimestampsAndWatermarks(new MyTimestampAndWatermarks)
//      .keyBy(4)//ips
//      .window(TumblingEventTimeWindows.of(Time.seconds(60)))
//      .apply(new WindowFunction[(String,String,String,String,String,String,Int),
//        (String,String,String,String,String,String,Int),Tuple,TimeWindow] {
//        override def apply(key: Tuple, window: TimeWindow,
//                           input: Iterable[(String, String, String, String, String, String, Int)],
//                           out: Collector[(String, String, String, String, String, String, Int)]): Unit = {
//
//          val domain = key.getField(4).toString
//          var sum = 0l
//          var resTime = ""
//          val iterator = input.iterator
//          while (iterator.hasNext) {
//            val next = iterator.next()
//            sum += next._7
//            //TODO... 是能拿到你这个window里面的时间的 next._1
//            resTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(next._4)
//          }
//        }
//      })

    //--elasticsearch部分 待改进
    val httpHosts = new java.util.ArrayList[HttpHost]
    httpHosts.add(new HttpHost("1.1.1.21", 9200, "http"))
    val esSinkBuilder_view_count = new ElasticsearchSink.Builder[(String,Long)](
      httpHosts,
      new ElasticsearchSinkFunction[(String,Long)] {
        def createIndexRequest(element: (String,Long)): IndexRequest = {
          val json = new java.util.HashMap[String, Any]
          json.put("domain", element._1)
          json.put("count", element._2)
          val id = element._1+"-"+element._2
          return Requests.indexRequest()
            .index("cdn")
            .`type`("")
            .id(id)
            .source(json)
        }

        override def process(t: (String,Long), runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createIndexRequest(t))
        }
      }
    )

    // configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
    esSinkBuilder_view_count.setBulkFlushMaxActions(1)


    val esSinkBuilder_view_agg = new ElasticsearchSink.Builder[(String,Long)](
      httpHosts,
      new ElasticsearchSinkFunction[(String,Long)] {
        def createIndexRequest(element: (String,Long)): IndexRequest = {
          val json = new java.util.HashMap[String, Any]
          json.put("CDN", element._1)
          json.put("count", element._2)
          val id = element._1+"-"+element._2
          return Requests.indexRequest()
            .index("cdn")
            .`type`("")
            .id(id)
            .source(json)
        }

        override def process(t: (String,Long), runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createIndexRequest(t))
        }
      }
    )

    esSinkBuilder_view_agg.setBulkFlushMaxActions(1)

    val esSinkBuilder_refer_ll_bl = new ElasticsearchSink.Builder[(String,Long)](
      httpHosts,
      new ElasticsearchSinkFunction[(String,Long)] {
        def createIndexRequest(element: (String,Long)): IndexRequest = {
          val json = new java.util.HashMap[String, Any]
          json.put("trffic", element._1)
          json.put("count", element._2)
          val id = element._1+"-"+element._2
          return Requests.indexRequest()
            .index("cdn")
            .`type`("")
            .id(id)
            .source(json)
        }

        override def process(t: (String,Long), runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createIndexRequest(t))
        }
      }
    )
    esSinkBuilder_refer_ll_bl.setBulkFlushMaxActions(1)

//    resultdata.addSink(esSinkBuilder_view_count.build)
//    resultdata.addSink(esSinkBuilder_view_agg.build)
//    resultdata.addSink(esSinkBuilder_refer_ll_bl.build)




    val result:JobExecutionResult = env.execute("HotReferAnalysis")
  }

}
