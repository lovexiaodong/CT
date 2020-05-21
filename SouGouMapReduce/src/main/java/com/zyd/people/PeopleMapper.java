package com.zyd.people;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PeopleMapper extends Mapper<LongWritable, Text, Text, PeopleModel> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String str = value.toString();
        String[] data = str.split(" ");
        PeopleModel model = new PeopleModel();
        model.setId(Integer.parseInt(data[0]));
        model.setGender(data[1]);
        model.setHight(Integer.parseInt(data[2]));
        context.write(new Text(data[1]), model);
    }
}
