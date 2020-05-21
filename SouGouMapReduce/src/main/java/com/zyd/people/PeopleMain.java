package com.zyd.people;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class PeopleMain {
    public static void main(String[] args) throws Exception {

        args = new String[]{"E:\\潭州课堂\\temp\\people_info.text","E:\\潭州课堂\\temp\\11"};
        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(PeopleMain.class);

        job.setMapperClass(PeopleMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(PeopleModel.class);

        job.setReducerClass(PeopleReduce.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}
