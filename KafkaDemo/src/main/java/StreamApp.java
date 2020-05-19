import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Arrays;
import java.util.Properties;

public class StreamApp {
    public static void main(String[] args) {

        String firstTopic = "first";
        String secondTopic = "test";

        Properties pt = new Properties();
        pt.put(StreamsConfig.APPLICATION_ID_CONFIG, "LogProcessor");
        pt.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.47.132:9092");

//        Serdes.String().getClass()
//        Topology topology = new Topology();
//        topology.addSource("source",firstTopic)
//                .addProcessor("processor", () -> new LogProcessor(), "source")
//                .addSink("sink", secondTopic, "processor");
//
//        KafkaStreams streams = new KafkaStreams(topology, pt);
//        streams.start();


        pt.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        pt.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream(firstTopic);
        source.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            @Override
            public Iterable<String> apply(String s) {
                System.out.println("s = " + s);
                return Arrays.asList(s.split(" "));
            }
        })
                .groupBy(new KeyValueMapper<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) {
                        System.out.println("group by " + s2);
                        return s2;
                    }
                })
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
               .filter(new Predicate<String, Long>() {
                   @Override
                   public boolean test(String s, Long aLong) {

                       System.out.println("filter = " + s);
                       System.out.println("filter long = " + aLong);
                       return true;
                   }
               })
                .toStream()
                .filter(new Predicate<String, Long>() {
                    @Override
                    public boolean test(String s, Long aLong) {
                        System.out.println("filter 1 s = " + s);
                        System.out.println("filter 1 long =" + aLong);
                        return true;
                    }
                }).to(secondTopic,Produced.with(Serdes.String(),Serdes.Long()));
        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, pt);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        streams.start();
    }
}
