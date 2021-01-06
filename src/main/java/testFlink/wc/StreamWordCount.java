package testFlink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class StreamWordCount {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 外部参数解析
        ParameterTool tool = ParameterTool.fromArgs(args);// --host localhost --port 7777
        String host = tool.get("host");
        Integer port = tool.getInt("port");

        // 接收 socket 文本流
        DataStream<String> inputDS = env.socketTextStream(host, port);

        inputDS.flatMap(new LineToWords())
                .keyBy(t -> t.f0)
                .sum(1)
                .print();
        env.execute("wcStream");
    }

    private static class LineToWords implements FlatMapFunction<String, Tuple2<String,Integer>> {

        @Override
        public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
            Arrays.stream(line.split(" "))
                    .forEach( w -> collector.collect(Tuple2.of(w, 1)));
        }
    }
}
