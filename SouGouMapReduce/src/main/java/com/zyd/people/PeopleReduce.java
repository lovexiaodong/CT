package com.zyd.people;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class PeopleReduce extends Reducer<Text, PeopleModel, Text, IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<PeopleModel> values, Context context) throws IOException, InterruptedException {
        int maxHight = 0;
        int minHight = 100000;
        int count = 0;
        Iterator<PeopleModel> iterator = values.iterator();
        while (iterator.hasNext()){
            PeopleModel next = iterator.next();
            if(next.getHight() > maxHight){
                maxHight = next.getHight();
            }

            if(next.getHight() < minHight){
                minHight = next.getHight();
            }

            count ++;
        }

        context.write(new Text("maxHigh = "), new IntWritable(maxHight));
        context.write(new Text("minHigh ="), new IntWritable(minHight));
        context.write(new Text(key.toString() + "coun = " ), new IntWritable(count));
    }
}
