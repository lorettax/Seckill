package com.lorettax.analy.tasks;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Table;
import com.lorettax.analy.UserFrequencyInfo;
import com.lorettax.analy.UserFrequencyResultInfo;
import com.lorettax.analy.maps.UserFrequencyMap;
import com.lorettax.utils.HBaseUtil;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;

import java.util.ArrayList;

public class UserFrequencyTask {
    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir","E:\\soft\\hadoop-2.6.0-cdh5.5.1\\hadoop-2.6.0-cdh5.5.1");
        args = new String[]{"--input","hdfs://http://192.168.80.134:9000//data/kafka/appStartup"};
        final ParameterTool params = ParameterTool.fromArgs(args);

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        BatchTableEnvironment tEnv = TableEnvironment.getTableEnvironment(env);
        env.getConfig().setGlobalJobParameters(params);

        DataSet<String> text = env.readTextFile(params.get("input"));
        DataSet<UserFrequencyInfo> map = text.flatMap(new UserFrequencyMap());
        tEnv.registerDataSet("UserFrequencyInfo",map,"appid,deviceid,appplatform,appversion,appchannel,timeValue");
        Table table = tEnv.sqlQuery("select appid,appplatform,appversion,appchannel,timeValue,number,count(1) as devicenums from (select appid,deviceid,appplatform,appversion,appchannel,timeValue,count(1) as number UserFrequencyInfo group by appid,deviceid,appplatform,appversion,appchannel,timeValue) aa group by appid,appplatform,appversion,appchannel,timeValue,number");

        DataSet<UserFrequencyResultInfo> result = tEnv.toDataSet(table,UserFrequencyResultInfo.class);

        try{
            List<UserFrequencyResultInfo> list = result.collect();
            for (UserFrequencyResultInfo userFrequeencyResultInfo:list) {
                String appid = userFrequeencyResultInfo.getAppid();
                String timevalue = userFrequeencyResultInfo.getTimeValue();
                String  rowkey = appid +"=="+timevalue;
                String tablename = "userfrequencyinfo";
                String clounmfamily = "listinfo";
                String column = "list";
                String datastring = HBaseUtil.getdata(tablename,rowkey,clounmfamily,column);
                List<UserFrequencyResultInfo> listdata = JSONObject.parseArray(datastring,UserFrequencyResultInfo.class);
                if (listdata ==null){
                    listdata= new ArrayList<UserFrequencyInfo>();
                }
                listdata.add(userFrequeencyResultInfo);
                String listjson = JSONObject.toJSONString(listdata);
                HBaseUtil.putdata(tablename,rowkey,clounmfamily,column,listjson);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
