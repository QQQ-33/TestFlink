package testFlink.udf;


import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.table.functions.TableFunction;
import testFlink.source.SensorSource;


/**
 * UDF
 * 自定义函数
 */
public class TableAndSQLFunctionTest {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new SensorSource());
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        tableEnv.registerFunction("myhashcode", new MyHashCode(23));

    }
    /**
     * 标量函数，将 0 或 1 个标量值映射到新的标量值，
     * 需要继承 ScalarFunction， 实现一个或多个 eval 方法。
     * 将 UDF 注册到 tableEnv
     * tableEnv.registerFunction("myhashcode",new MyHashCode(23))
     * 注册好后，在 SQL中当成普通函数使用
     * select myhashcode(id) from sensor;
     */
    public static class MyHashCode extends ScalarFunction {
        private int factor = 13;
        public MyHashCode (int factor){
            this.factor = factor;
        }
        // 必须重新 eval 函数
        public int eval(String s){
            return s.hashCode() * factor;
        }
    }

    /**
     * 表函数, 将 0 或 1个标量值作为输入，返回任意数量的输出
     * 需要继承 TableFunction<T>
     * 实现一个或多个 eval 方法
     * 一行转多行
     */
    public static class Split extends TableFunction<Tuple2<String, Integer>> {
        private String separator = ",";
        public Split(String separator) {
            this.separator = separator;
        }
        public void eval(String str) {
            for(String s: str.split(separator)) {
                collect(new Tuple2<>(s, s.length()));
            }
        }
    }

    /**
     * 聚合函数，把表中的数据聚合成一个标量
     * 继承 AggregateFunction<T, ACC>
     * T 输出类型 ACC 中间状态类型
     * 多行转一行
     */

    public static class AvgTemp extends AggregateFunction<Double, Tuple2<Double, Integer>> {

        // 获取最终结果
        @Override
        public Double getValue(Tuple2<Double, Integer> accumulator) {
            return accumulator.f0 / accumulator.f1;
        }
        // 初始化累加器
        @Override
        public Tuple2<Double, Integer> createAccumulator() {
            return new Tuple2<>(0.0, 0);
        }
        // 最关键的，需要手动写出 accumulate 方法，注意方法名和入参类型
        // 此方法定义如何进行聚合
        public void accumulate(Tuple2<Double, Integer> accumulator, Double temp){
            accumulator.f0 += temp;
            accumulator.f1 += 1;
        }
    }
    /**
     * 表聚合函数
     * 把表中数据聚合为多行多列的结果集
     */

}
