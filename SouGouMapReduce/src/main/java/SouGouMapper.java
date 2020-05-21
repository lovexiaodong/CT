import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SouGouMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String log = value.toString();
        String[] words = log.split("\t");
        if(words.length != 6) return;

        try {

            if(Integer.parseInt(words[3]) == 1 && Integer.parseInt(words[4]) == 2){
                System.out.println(value);
                context.write(value, NullWritable.get());
            }
        }catch (Exception e){e.printStackTrace();}

    }
}
