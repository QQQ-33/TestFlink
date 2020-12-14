package com.tom.yang.project.akka.masterandworker

class MasterAndWorkerDemo {

}

/**
 * 使用 Akka  模仿 Spark 的 Master 和 Worker 的通讯
 * 1. Worker 注册到 Master，Master 完成注册后回复 Worker注册完成
 * 2. Worker 向 Master 定时发送心跳
 * 3. Master 接收 Worker 的心跳后，更新Worker最后一个心跳的时间
 * 4. Master 定时任务检测有哪些Worker没有更新心跳时间，并从 HashMap中删除
 * 5. Master 和 Worker 分布式部署到Linux
 *
 */
