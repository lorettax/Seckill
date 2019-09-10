package project



import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

import scala.collection.mutable

class MysqlSource extends RichParallelSourceFunction[mutable.HashMap[String,String]]{

  var connection:Connection = null
  var ps:PreparedStatement = null

  //-- open 建立连接
  override def open(parameters: Configuration): Unit = {
    val driver= "com.mysql.jdbc.Driver"

    val url = "jdbc:mysql://1.1.1.21:3306/flink"
    val username = "root"
    val password = "root"

    Class.forName(driver)
    connection = DriverManager.getConnection(url,username,password)

    val sql = "select user_id,domain from user_domain_config"
    ps = connection.prepareStatement(sql)
  }

  //--释放资源
  override def close(): Unit = {
    if(ps != null){
      ps.close()
    }
    if(connection!=null){}
    connection.close()
  }

  //--此处是代码的关键，要从mysql把数据读出来转成map进行数据的封装
  override def run(ctx: SourceFunction.SourceContext[mutable.HashMap[String, String]]): Unit = {

    //TODO...

  }

  override def cancel(): Unit = ???
}
