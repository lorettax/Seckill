//package project
//
//import java.text.SimpleDateFormat
//
//import org.apache.flink.runtime.executiongraph.Execution
//import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
//import org.apache.flink.streaming.api.watermark.Watermark
//
//class MyTimestampAndWatermarks
//  extends AssignerWithPeriodicWatermarks[(String,String,String,String,String,String,Int)]{
//
//  val maxOutOfOrderness = 3500L
//  var currentMaxTimestamp: Long = _
//
//  override def getCurrentWatermark: Watermark ={
//    new Watermark(currentMaxTimestamp-maxOutOfOrderness)
//  }
//
//  override def extractTimestamp(element: (String,String,String,String,String,String,Int),
//                                previousElementTimestamp: Long): Unit = {
//    val timeStr = element._4
//    var time = 0l
//    try{
//      val sourceFormat = new SimpleDateFormat("yyyy-MM--dd HH:mm:ss")
//      time = sourceFormat.parse(timeStr).getTime
//    }catch {
//      case e : Execution =>{
//        println(s"time parse error : $timeStr",e.getMessage)
//      }
//    val timestamp = time
//    currentMaxTimestamp = Math.max(timestamp,currentMaxTimestamp)
//    timestamp
//    }
//  }
//}
