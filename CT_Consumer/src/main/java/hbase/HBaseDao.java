package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import scala.util.control.Exception;
import utils.ConnectionInstance;
import utils.HBaseUtil;
import utils.PropertiesUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HBaseDao {

    private int regions;
    private String namespace;
    private String tableName;

    private  static Configuration conf = null;

    private HTable hTable;
    private Connection connection;

    private SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddhhmmss");
//    private static final String ZKConnect="115.159.190.55:2181,118.25.83.45:2181,129.211.4.158:2181";
    private static final String ZKConnect="hadoop11:2181,hadoop12:2181,hadoop13:2181";
    static {
       
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum",ZKConnect);
    }

    public HBaseDao(){
        regions = Integer.parseInt(PropertiesUtil.getProperty("hbase.calllog.regions"));
        namespace = PropertiesUtil.getProperty("hbase.calllog.namespace");
        tableName = PropertiesUtil.getProperty("hbase.calllog.namespace");

        if(!HBaseUtil.isExistTable(conf, tableName)){
            HBaseUtil.initNameSpace(conf, namespace);
            HBaseUtil.createTable(conf, tableName, regions , "f1", "f2");
        }

    }

    private List<Put> cacheList = new ArrayList<>();
    private HTable table;

    public void put(String value){
        try {
            if(cacheList.isEmpty()){
                connection = ConnectionInstance.getConnection(conf);
                table = (HTable) connection.getTable(TableName.valueOf(tableName));
//                table.setAutoFlushTo(false);
//                table.setWriteBufferSize(2 * 1024 * 1024);

            }


            String[] split = value.split(",");

            String caller = split[0];
            String callee = split[1];
            String buildTime = split[2];
            String duration = split[3];


            String buildTimeReplace = df2.format(df1.parse(buildTime));
            String time = String.valueOf(df1.parse(buildTime).getTime());
            String regionCode = HBaseUtil.genRegionCode(caller, buildTime, regions);

            String rowKey = HBaseUtil.genRowKey(regionCode, caller, buildTimeReplace, callee, 1, duration );


            Put put  = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("caller"), Bytes.toBytes(caller));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callee"), Bytes.toBytes(callee));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("buildTimeReplace"), Bytes.toBytes(buildTimeReplace));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("time"), Bytes.toBytes(time));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("flag"), Bytes.toBytes(1));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("duration"), Bytes.toBytes(duration));
            cacheList.add(put);


            if(cacheList.size() >= 30){
                table.put(cacheList);
                table.close();
                cacheList.clear();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
