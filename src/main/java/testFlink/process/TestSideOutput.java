package testFlink.process;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import testFlink.beans.SensorReading;
import testFlink.source.SensorSource;

/**
 * 侧输出流进行分流
 * SideOutput
 */
public class TestSideOutput {
    public static StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args)throws Exception {
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        // OutputTag 用来表示侧输出流
        // 由于有类型擦除， 注意写法是匿名内部类的写法(实现类或接口的同时构造实例)
        OutputTag<SensorReading> lowTempTag = new OutputTag<SensorReading>("low-temp"){};
        // 利用温度进行分组输出高温流和低温流
        // 注意这里是 SingleOutputStreamOperator 不是 DataStream
        SingleOutputStreamOperator<SensorReading> highTempStream = input.process(new ProcessFunction<SensorReading, SensorReading>() {
            @Override
            public void processElement(SensorReading sensorReading, Context context, Collector<SensorReading> collector) throws Exception {
                if(sensorReading.getTemperature() > 50){
                    collector.collect(sensorReading);
                } else {
                    context.output(lowTempTag, sensorReading);
                }
            }
        });
        highTempStream.print("high-temp");
        highTempStream.getSideOutput(lowTempTag).print("low-temp");

        env.execute();
    }



}
