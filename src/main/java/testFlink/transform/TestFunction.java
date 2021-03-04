package testFlink.transform;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class TestFunction implements FlatMapFunction<String, String> {
    @Override
    public void flatMap(String s, Collector<String> collector) throws Exception {
        String[] fields = s.split(",");
        for (String f: fields){
            collector.collect(f);
        }
    }
}
