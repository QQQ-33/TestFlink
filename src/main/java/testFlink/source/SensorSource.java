package testFlink.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import testFlink.beans.SensorReading;

import java.util.HashMap;
import java.util.Random;

/**
 * 自定义数据源
 */
public class SensorSource implements SourceFunction<SensorReading> {
    // 定义一个标志位，用于控制数据的产生
    private boolean running = true;

    @Override
    public void run(SourceContext<SensorReading> sourceContext) throws Exception {
        // 随机产生传感器数值
        Random random = new Random();
        HashMap<String, Double> sensorTempMap = new HashMap<>();
        for(int i = 0; i < 10; i++){
            // 这里用了高斯随机数(正态分布)
            sensorTempMap.put("sensor_" + (i + 1), 60 + random.nextGaussian() * 20);
        }
        while (running){
            for(String key: sensorTempMap.keySet()){
                Double newTemp = sensorTempMap.get(key) + random.nextGaussian();
                sensorTempMap.put(key, newTemp);
                sourceContext.collect(new SensorReading(key, newTemp, System.currentTimeMillis()));
            }
            Thread.sleep(2000);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
