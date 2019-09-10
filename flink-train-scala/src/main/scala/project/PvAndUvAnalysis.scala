package project

import java.text.SimpleDateFormat
import java.util.Properties

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.accumulators.LongCounter
import org.apache.flink.api.common.functions.{RichMapFunction, RuntimeContext}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
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

object PvAndUvAnalysis {
  def main(args: Array[String]): Unit = {
    import org.apache.flink.api.scala._
    val env:StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

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
//      new RichMapFunction[String,String] {
//          val counter = new LongCounter()
//          override def open(parameters: Configuration): Unit = {
//            getRuntimeContext.addAccumulator("ele-counter-scala",counter)
//          }
//          counter.add(1)
////          println(counter)
//          override def map(value: String): String = ???
//        }

      (cdn_count,level,trffic,timeStr,ips,domain,1)
    })
//    logdata.print()

    //uv计算
    val trffic_parallelism = logdata.map(x=>{
      (x._6,x._7)
    }).keyBy(0)
      .sum(1)
//    trffic_parallelism.print()

    // 运营商分布
    val cdn_genernal=logdata.map(x=>{
      (x._3,x._5,x._6,x._7)
    }).keyBy(0)
      .sum(3)
//    cdn_genernal.print()

    //用户区域分布
    val fb_level = logdata.map(x=>{
      (x._2,x._3,x._5,x._6,x._7)
    }).keyBy(0).sum(4)

//    fb_level.print()


    //--水印和时间戳（周期性生成）
//    val resultdata = logdata.assignTimestampsAndWatermarks(new MyTimestampAndWatermarks)


    //--elasticsearch部分 待改进
    val httpHosts = new java.util.ArrayList[HttpHost]
    httpHosts.add(new HttpHost("1.1.1.21", 9200, "http"))

    //uv-elasticsearch
    val esSinkBuilder_trffic_parallelism = new ElasticsearchSink.Builder[(String,Long)](
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

    esSinkBuilder_trffic_parallelism.setBulkFlushMaxActions(1)

    // 运营商分布--elasticsearch
    val esSinkBuilder_cdn_genernal = new ElasticsearchSink.Builder[(String,String,String,Long)](
      httpHosts,
      new ElasticsearchSinkFunction[(String,String,String,Long)] {
        def createIndexRequest(element: (String,String,String,Long)): IndexRequest = {
          val json = new java.util.HashMap[String,Any]
          json.put("trffic", element._1)
          json.put("ips", element._2)
          json.put("domain",element._3)
//          json.put("count",element._4)
          val id = element._1+"-"+element._2+element._3+"-"+element._4
          return Requests.indexRequest()
            .index("cdn")
            .`type`("")
            .id(id)
            .source(json)
        }

        override def process(t: (String,String,String,Long), runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createIndexRequest(t))
        }
      }
    )

    esSinkBuilder_cdn_genernal.setBulkFlushMaxActions(1)

    //--用户区域分布--elasticsearch
    val esSinkBuilder_fb_level = new ElasticsearchSink.Builder[(String,String,String,String,Long)](
      httpHosts,
      new ElasticsearchSinkFunction[(String,String,String,String,Long)] {
        def createIndexRequest(element: (String,String,String,String,Long)): IndexRequest = {
          val json = new java.util.HashMap[String, Any]
          json.put("level",element._1)
          json.put("trffic", element._2)
          json.put("count", element._3)
          json.put("trffic", element._4)
//          json.put("count", element._5)
          val id = element._1+"-"+element._2+element._3+"-"+element._4+element._5
          return Requests.indexRequest()
            .index("cdn")
            .`type`("")
            .id(id)
            .source(json)
        }

        override def process(t: (String,String,String,String,Long), runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createIndexRequest(t))
        }
      }
    )
    esSinkBuilder_fb_level.setBulkFlushMaxActions(1)

    //    resultdata.addSink(esSinkBuilder_trffic_parallelism.build)
    //    resultdata.addSink(esSinkBuilder_cdn_genernal.build)
    //    resultdata.addSink(esSinkBuilder_fb_level.build)



    val result:JobExecutionResult = env.execute("PvAndUvAnalysis")


  }

}
