package testFlink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.Arrays;


public class WordCount {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        final String inputPath = "C:/Users/qk965/TomWorkspace/TestFlink/src/main/resources/testData/hello.txt";
        DataSet<String> inputDS = env.readTextFile(inputPath);
        DataSet<Tuple2<String, Integer>> res = inputDS.flatMap(new LineToWords())
        .groupBy(0)
        .sum(1);

        res.print();

//        env.execute();
    }
    private static class LineToWords implements FlatMapFunction<String, Tuple2<String,Integer>>{
        @Override
        public void flatMap(String line, Collector<Tuple2<String,Integer>> collector) throws Exception {
            Arrays.stream(line.split(" "))
                    .forEach( w -> collector.collect(Tuple2.of(w, 1)));
        }
    }

}
