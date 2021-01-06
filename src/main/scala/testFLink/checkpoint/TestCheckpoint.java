package testFLink.checkpoint;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TestCheckpoint {
    public static StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args)throws Exception {
        // 开启 checkpoint， 参数为 checkpoint 的时间间隔, 状态一致性的设置
        env.enableCheckpointing(500L, CheckpointingMode.EXACTLY_ONCE);
        /* checkpoint 的设置 */

        // 两个 checkpoint 时间间隔
        env.getCheckpointConfig().setCheckpointInterval(500L);
        // 状态一致性
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 超时时间
        env.getCheckpointConfig().setCheckpointTimeout(60000L);
        // 整个 pipeline 最大同时进行的 checkpoint
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(10);
        // 上一次 checkpoint 完成到下一次 checkpoint 开始的间隔
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(100L);
        // 故障恢复的选择，true 表示只用 checkpoint 来做故障恢复，即使 savepoint 的时间比 checkpoint 更晚
        env.getCheckpointConfig().setPreferCheckpointForRecovery(false);
        // 允许 checkpoint 保存失败多少次， 默认是 0
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(3);

        /* 重启的策略设置 */
        // 固定延迟重启 尝试次数，延迟时间
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 10000L));
        // 失败率重启 在时间间隔内的最大失败次数，统计失败次数的时间间隔，每次重启的时间间隔
        // 10 分钟内失败 3 次就重启，每次重启的间隔是 1 分钟
        env.setRestartStrategy(RestartStrategies.failureRateRestart(3, Time.minutes(10), Time.minutes(1)));
    }
}
