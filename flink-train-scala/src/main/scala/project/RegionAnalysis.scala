package project

import java.util.Properties

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.windowing.WindowFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.{GlobalWindows, SlidingProcessingTimeWindows, TumblingProcessingTimeWindows, WindowAssigner}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

object RegionAnalysis {
  def main(args: Array[String]): Unit = {

    import org.apache.flink.api.scala._
    val env = StreamExecutionEnvironment.getExecutionEnvironment
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
        logdata.print()
    //排名，区域，总流量，流量占比，访问次数，访问占比

    //排名：区域流量排名
    val level_sort = logdata.map(x=>{
      (x._2,x._7)
    }).keyBy(0)
      .timeWindow(Time.seconds(100),Time.seconds(5))
      .sum(1)//排序未实现

//    level_sort.print()

    //区域,总流量：区域内的总流量
    val level_agg = logdata.map(x=>{
      (x._2,x._7)
    }).keyBy(0).timeWindow(Time.hours(24),Time.seconds(10))
      .sum(1)
//    level_agg.print()

    //流量占比: 运营商
    val region_ll_bl = logdata.map{x=>(x._3,x._7)}.keyBy(0)
      .timeWindow(Time.hours(24),Time.seconds(10))
      .sum(1)
//    region_ll_bl.print()

    //访问次数：针对地区，
    val view_count = logdata.map{x=>(x._2,x._7)}.keyBy(0)
      .timeWindow(Time.hours(24),Time.seconds(10))
      .sum(1)

    view_count.print()
    //访问占比：     访问占比= 访问次数 / 全局总流量
    //--全局总流量
    val view_agg = logdata.map{x=>(x._1,x._7)}.keyBy(0)
      .timeWindow(Time.hours(24),Time.seconds(10))
      .sum(1)
//    view_agg.print()


        //访问占比：每个地区的访问占比（地区UV/总UV）

    //--水印和时间戳（周期性）
//    val resultdata = logdata.assignTimestampsAndWatermarks(new MyTimestampAndWatermarks)

    //--elasticsearch部分 待改进
    val httpHosts = new java.util.ArrayList[HttpHost]
    httpHosts.add(new HttpHost("1.1.1.21", 9200, "http"))
    //排名：区域流量排名---->elasticsearch
    val esSinkBuilder_level_sort = new ElasticsearchSink.Builder[(String,Long)](
      httpHosts,
      new ElasticsearchSinkFunction[(String,Long)] {
        def createIndexRequest(element: (String,Long)): IndexRequest = {
          val json = new java.util.HashMap[String, Any]
          json.put("level", element._1)
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

    esSinkBuilder_level_sort.setBulkFlushMaxActions(1)

    //区域,总流量：------>elasticsearch
    val esSinkBuilder_level_agg = new ElasticsearchSink.Builder[(String,Long)](
      httpHosts,
      new ElasticsearchSinkFunction[(String,Long)] {
        def createIndexRequest(element: (String,Long)): IndexRequest = {
          val json = new java.util.HashMap[String, Any]
          json.put("level", element._1)
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

    esSinkBuilder_level_agg.setBulkFlushMaxActions(1)
    //流量占比: 运营商-----elasticsearch
    val esSinkBuilder_region_ll_bl = new ElasticsearchSink.Builder[(String,Long)](
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
    esSinkBuilder_region_ll_bl.setBulkFlushMaxActions(1)

    //访问次数：针对地区----elasticsearch
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



    //    resultdata.addSink(esSinkBuilder_level_sort.build)
    //    resultdata.addSink(esSinkBuilder_level_agg.build)
    //    resultdata.addSink(esSinkBuilder_region_ll_bl.build)
    //    resultdata.addSink(esSinkBuilder_view_count.build)
    //    resultdata.addSink(esSinkBuilder_view_agg.build)






    val result:JobExecutionResult =env.execute("RegionAnalysis")
  }

}

