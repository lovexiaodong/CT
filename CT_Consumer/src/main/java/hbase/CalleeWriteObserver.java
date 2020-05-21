package hbase;

import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.wal.WALEdit;
import utils.HBaseUtil;
import utils.PropertiesUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalleeWriteObserver implements RegionObserver {
    private SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddhhmmss");
    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> c, Put put, WALEdit edit, Durability durability) {
        String tableName = PropertiesUtil.getProperty("hbase.calllog.tableName");
        String name = c.getEnvironment().getRegionInfo().getTable().getNameAsString();

        System.out.println("=================" + tableName );
        System.out.println("--------------" + name );
        if(!tableName.equals(name)) return;

        String rowKey = Bytes.toString(put.getRow());

        //region_caller_time_callee_flag_duration
        String[] split = rowKey.split("_");
        String oldFlag = split[4];
        if("0".equals(oldFlag)) return;

        int region = Integer.parseInt(PropertiesUtil.getProperty("hbase.calllog.regions"));

        String caller = split[1];
        String time = split[2];
        String callee = split[3];
        String falg = "0";
        String duration = split[5];

        String regionCode = HBaseUtil.genRegionCode(callee, time, region);

        String rowKey2 = HBaseUtil.genRowKey(regionCode, callee, time, caller, 0, duration);
        try {
            Date parse = df2.parse(time);
            Put put2  = new Put(Bytes.toBytes(rowKey2));
            put2.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("caller"), Bytes.toBytes(callee));
            put2.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callee"), Bytes.toBytes(caller));
            put2.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("buildTimeReplace"), Bytes.toBytes(parse.getTime()));
            put2.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("time"), Bytes.toBytes(time));
            put2.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("flag"), Bytes.toBytes(0));
            put2.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("duration"), Bytes.toBytes(duration));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
