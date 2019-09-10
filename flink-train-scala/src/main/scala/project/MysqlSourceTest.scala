package project

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._

object MysqlSourceTest {
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val data = env.addSource(new MysqlSource)

    data.print()

    env.execute("MysqlSourceTest")
  }

}