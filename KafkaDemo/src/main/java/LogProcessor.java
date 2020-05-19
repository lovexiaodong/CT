import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

public class LogProcessor implements Processor<byte[], byte[]> {
    ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public void process(byte[] bytes, byte[] bytes2) {
        String str = new String(bytes2);
        str = str + " -- stream";
        context.forward(bytes, str.getBytes());
    }


    @Override
    public void close() {

    }
}
