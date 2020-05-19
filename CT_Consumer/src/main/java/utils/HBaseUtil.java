package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.TreeSet;

public class HBaseUtil {

    /**
     * 判断表是否存在
     * @param conf
     * @param tableName
     * @return
     */
    public static boolean isExistTable(Configuration conf, String tableName){
        Connection con = null;
        Admin admin = null;
        boolean res = false;
        try {
             con = ConnectionFactory.createConnection(conf);
             admin = con.getAdmin();
            System.out.println(admin.toString());
             res = admin.tableExists(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(admin, con);
        }
        return res;
    }

    /**
     * 关闭资源文件
     * @param admin
     * @param con
     */
    private static void close(Admin admin, Connection con) {
        if(admin != null){
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(con != null){
            try {
                con.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *  初始化命名空间 命名空间相当于库
     * @param con
     * @param namespace
     */

    public static void initNameSpace(Configuration con, String namespace){
        Connection connection = null;
        Admin admin = null;
        try {
            connection = ConnectionFactory.createConnection(con);
            admin = connection.getAdmin();
            NamespaceDescriptor builder = NamespaceDescriptor.create(namespace )
                    .addConfiguration("CREATE_TIME", System.currentTimeMillis() + "")
                    .addConfiguration("AUTHOR", "xiaodong")
                    .build();
            admin.createNamespace(builder);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(admin, connection);
        }
    }

    /**
     * 创建表
     * @param conf
     * @param tableName
     * @param regions
     * @param columnFamily
     */

    public static void createTable(Configuration conf, String tableName, int regions, String... columnFamily){
        Connection con = null;
        Admin admin = null;
        try {
            con = ConnectionFactory.createConnection(conf);
            admin = con.getAdmin();

            if(isExistTable(conf, tableName)) return;

            HTableDescriptor ht = new HTableDescriptor(TableName.valueOf(tableName));
            if(columnFamily != null){
                for (String cf : columnFamily){
                    ht.addFamily(new HColumnDescriptor(cf));

                }
            }
            admin.createTable(ht, genSplitKey(regions));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(admin, con);
        }
    }

    private static byte[][] genSplitKey(int regions) {

        String[]  keys = new String[regions];

        DecimalFormat df = new DecimalFormat("00");
        for (int i = 0; i < regions; i++){
            keys[i] = df.format(i) + "|";
        }


        byte[][] splitKeys = new byte[regions][];

        TreeSet<byte[]> treeSet = new TreeSet<>(Bytes.BYTES_COMPARATOR);

        for (int i = 0; i < regions; i++){
            treeSet.add(Bytes.toBytes(keys[i]));
        }

        Iterator<byte[]> iterator = treeSet.iterator();

        int index = 0;
        while (iterator.hasNext()){
            splitKeys[index++] = iterator.next();
        }

        return splitKeys;

    }


    public static String genRegionCode(String caller,String time, int regions){
        //取出电话号码后四位

        String lastPhone = caller.substring(caller.length() - 4);

        //取出年月 2019-08-09 19:32:12
        String y = time.replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll(" ","")
                .substring(0, 6);

        int x = Integer.parseInt(lastPhone) ^ Integer.parseInt(y);
        int regionCode = x % regions;

        DecimalFormat format = new DecimalFormat("00");

        return format.format(regionCode);
    }

    public static String genRowKey(String regionCode, String caller,String time, String call,int flag, String duration){
        StringBuilder sb = new StringBuilder();
        sb.append(regionCode).append("_")
                .append(caller).append("_")
                .append(time).append("_")
                .append(call).append("_")
                .append(flag).append("_")
                .append(duration);
        return sb.toString();
    }

}
