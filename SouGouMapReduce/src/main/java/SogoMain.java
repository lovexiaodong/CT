import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class SogoMain {
    public static void main(String[] args) {
        args = new String[]{"E:\\潭州课堂\\电商大数据实战项目与试验环境\\数据\\SogouQ.txt","E:\\潭州课堂\\电商大数据实战项目与试验环境\\1"};
        try {
            Job job = Job.getInstance(new Configuration());
            job.setJarByClass(SogoMain.class);

            job.setMapperClass(SouGouMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(NullWritable.class);

            FileInputFormat.setInputPaths(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
