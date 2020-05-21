import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseDemo {

    public static void main(String[] args) {
        HBaseDemo demo = new HBaseDemo();
        demo.createTable();
    }

    public   void createTable(){
        //zookeeper
        Configuration con = HBaseConfiguration.create();
        con.set("hbase.zookeeper.quorum","hadoop112,hadoop113,hadoop114");

        // Hbase 客户端

        try {
            HBaseAdmin admin = (HBaseAdmin) ConnectionFactory.createConnection(con).getAdmin();
            //指定表的描述
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf("myTable"));
            //添加family
            hTableDescriptor.addFamily(new HColumnDescriptor("info"));
            hTableDescriptor.addFamily(new HColumnDescriptor("grade"));
            admin.createTable(hTableDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putData(){
        //zookeeper
//        Configuration con =  new Configuration();
//        con.set("hbase.zookeeper.quorum","hadoop112");
//
//        try {
//
//
//            HTable client = new HTable(con, "myTable");
//            Put put = new Put(Bytes.toBytes("id001"));
//            //列族  列的名字， 列值
//            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("tom"));
//            client.put(put);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void scanData(){

    }


}
