package kafka;

import hbase.HBaseDao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import utils.PropertiesUtil;

import java.time.Duration;
import java.util.Arrays;

public class HBaseConsume {

    public static void main(String[] args) {
        KafkaConsumer consumer = new KafkaConsumer<>(PropertiesUtil.properties);

        consumer.subscribe(Arrays.asList(PropertiesUtil.getProperty("kafka.topics")));

        HBaseDao dao = new HBaseDao();
        while (true){
            ConsumerRecords<String,String> poll =  consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String,String> cr : poll){
                System.out.println(cr.value());
                dao.put(cr.value());

            }
        }



    }
}
