package testFlink.state;

import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TestFaultTolerance {
    private static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args) {

        env.setStateBackend(new MemoryStateBackend());
        env.setStateBackend(new FsStateBackend(""));

    }
}
