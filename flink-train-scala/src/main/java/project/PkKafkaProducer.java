package project;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class PkKafkaProducer {

    public static void main(String[] args) throws Exception {


        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","1.1.1.21:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        String topic ="pktest";
        //通过死循环不断的发送数据
        while(true){

            StringBuilder builder = new StringBuilder();

            //--一组数据
            builder.append("CDN").append("\t") //来源
                    .append(getLevels()).append("\t") //地区
                    .append(getTraffic()).append("\t") //运营商
                    .append(new SimpleDateFormat("yyyy-MM--dd HH:mm:ss").format(new Date())).append("\t") //时间
                    .append(getIps()).append("\t")  //ip地址
                    .append(getDomains()).append("\t");//域名

            System.out.println(builder.toString());
            producer.send(new ProducerRecord<String,String>(topic,builder.toString()));

            Thread.sleep(2000);

        }
    }

    //--运营商
    private static String getTraffic() {
        String[] trffics = new String[]{"中国移动","中国电信","中国联通","国外运营商"};
        return trffics[new Random().nextInt(trffics.length)];
    }

    //自动more网址
    private static String getDomains() {

        String[] domains = new String[]{
                "v1.go2yd.com",
                "v2.go2yd.com",
                "v3.go2yd.com",
                "v4.go2yd.com",
                "v5.go2yd.com",
                "v6.go2yd.com",
                "v7.go2yd.com",
                "v8.go2yd.com",
                "vmi.go2yd.com"
        };

        return domains[new Random().nextInt(domains.length)];
    }

    //--自动more的ip地址
    private static String getIps() {

        String[] ips = new String[]{
                "223.104.18.110",
                "113.101.75.194",
                "27.17.127.135",
                "183.225.139.16",
                "112.1.66.34",
                "175.148.211.190",
                "183.227.58.21",
                "59.83.198.84",
                "117.28.38.28",
                "117.59.39.169"
        };

        return  ips[new Random().nextInt(ips.length)];

    }

    //--生产level数据
    public static String getLevels(){
        String[] levels = new String[]{"北京市","上海市","广州省","天津市","重庆市",
                "河北省","陕西省","山西省","辽宁省","甘肃省",
                "浙江省","河南省","湖北省","香港特别行政区","澳门特别行政区",
                "四川省","贵州省","云南省","海南省","台湾省","国外"};
        return levels[new Random().nextInt(levels.length)];
    }
}
