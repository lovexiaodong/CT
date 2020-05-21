import org.apache.kafka.clients.producer.*;

import java.util.ArrayList;
import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","192.168.47.132:9092");
//        properties.setProperty(" broker.list","192.168.47.132:9092");
        //发送消息是否等待应答
        properties.setProperty("acks","all");
        //失败时重试的次数
        properties.put("retries","0");
//        ProducerConfig.RETRIES_CONFIG
        //批处理消息大小
        properties.setProperty("batch.size","10241");
        //批处理数据延迟
        properties.setProperty("linger.ms","5");
        //配置内存缓冲区
        properties.setProperty("buffer.memory","12345");

//        properties.setProperty("partitioner.class", "MyPatition");
        //消息在发送前必须序列化
        properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        ArrayList<String> list = new ArrayList<>();
        list.add("MyInterceptor");
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, list );

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        for (int i = 0; i <= 100; i ++){
            System.out.println(i);
//            producer.send( new ProducerRecord<String, String>("test","test = " + i));

            producer.send(new ProducerRecord<String, String>("test", "test = " + i), new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                    if(recordMetadata != null){
                        System.out.println("分区是：" + recordMetadata.topic() + "偏移量 = " + recordMetadata.offset() +
                                "  分区 = " + recordMetadata.partition());
                    }
                }
            });
        }

        producer.close();
    }
}
