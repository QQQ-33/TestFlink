package testFlink.udf;


import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.IterationRuntimeContext;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import testFlink.beans.SensorReading;
import testFlink.source.SensorSource;

/**
 * Flink 暴露了所有的函数接口
 * 根据业务可以实现对应的 Function 接口
 * MapFunction FilterFunction ReduceFunction 等等
 */
public class FunctionTest {
    public static StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    public static void main(String[] args) {
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        // 实现类
        input.filter(new MyFilter("sensor"));
        // 匿名函数实现
        input.filter( s -> s.getId().contains("sensor"));
        // 富函数
    }

    /**
     * 实现 Function
     * FilterFunction<T> T为入参的类型
     */
    public static class MyFilter implements FilterFunction<SensorReading> {
        private String keyWord;

        MyFilter(String keyWord) { this.keyWord = keyWord; }
        @Override
        public boolean filter(SensorReading s) throws Exception {
            return s.getId().contains(this.keyWord);
        }
    }

    /**
     * 除了普通的 Function 接口还有 RichFunction 接口，
     * RichFunction 可以获取当前运行环境上下文，拥有生命周期方法，实现更加复杂的功能
     *
     * RichMapFunction RichFlatMapFunction RichFilterFunction
     * 注意这里是 extends
     * 通过 getRuntimeContext() 获取到上下文后可以获取 State 等
     */
    public static class MyMapFunction extends RichMapFunction<SensorReading, Tuple2<String, Integer>>{
        /**
         * 初始化，一般用于定义 State
         * 或者进行数据库，Redis等连接
         */
        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
        }

        /**
         * 关闭数据库，Redis等连接
         * 清空 State
         */
        @Override
        public void close() throws Exception {
            super.close();
        }

        @Override
        public Tuple2<String, Integer> map(SensorReading sensorReading) throws Exception {
            return null;
        }
    }
}
