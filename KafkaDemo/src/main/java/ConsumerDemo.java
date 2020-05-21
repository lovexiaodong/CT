import com.zyd.utils.PropertiesUitls;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","192.168.1.111:9092");
//        properties.setProperty("metadata.broker.list","192.168.47.132:9092");
        //配置消费组
//        properties.setProperty("group.id","g1");
        //是否自动确认offset
        properties.put("enable.auto.commit","true");

        properties.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        consumer.subscribe(Arrays.asList(PropertiesUitls.getProperty("kafka.topics")));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            System.out.println("释放资源");
        }));
        while (true){
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : poll){
                System.out.println("consumer = " + record.value());
                System.out.println(record.key());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
