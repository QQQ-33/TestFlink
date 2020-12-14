https://confucianzuoyuan.github.io/flink-tutorial/book/print.html

1. 任务(task)是什么：
    <br/>代码中定义的每一步操作（算子， operator），就是一个 task
    <br/>算子可以设置并行度，所以每一步操作可以有多个并行的 task
    <br/>Flink 可以将无需 shuffle 的任务(类似spark窄依赖的任务)合并成一个
<br/>
2. slot 是什么：
    <br/>slot 是 TaskManager 拥有的计算资源的子集
    <br/>一个 task 只能运行在 一个 slot 上
    <br/>同一个 task 的多个并行任务，不能共用一个 slot
    <br/>不同 task 可以共享一个 slot
    <br>如果不想 task 共享 slot，也可以对每个算子设置 slotSharingGroup 来区分不同 slot，从设置了 slotSharingGroup 开始都算作一个组
    <br>使用 slotSharingGroup 设置之后需要的 slot 数，要根据实际情况计算 
<br/>
3. 并行度和 task 的关系：
    <br/>并行度就是每一个算子拥有的并行任务的数量
    <br/>slot的数量只跟 TaskManager的配置有关，是静态的
    <br/>一个 Job 所需的 slot 数量按照所有算子的最大并行度计算
<br/>
4. 什么样的任务能合并：
    <br/>前后任务是 one-to-one 操作，且并行度相同
    <br>如果不想合并 operator，可以设置 slotSharingGroup() ，或者 disableChaining() 来拒绝合并算子链


并行度的优先级：code(代码) -> submit(提交命令) -> flink-conf.yaml    

StreamGraph
<br>根据用户通过StreamApi编写的代码生成的图，用来表示程序的拓扑

JobGraph
<br>StreamGraph经过优化后生成的，提交给JobManager的数据结构，主要优化了多个可以合并的任务节点chain一起作为一个节点

ExecutionGraph
<br>JobGraph的并行化版本，是调度层最核心的数结构

keyBy:
<br>
基于key的hashCode重分区，同一个key只能在一个分区内处理，一个分区内可以有不同的key数据
<br>
keyBy之后的所有操作仅针对当前key

滚动聚合操作：
<br> 
DataStream 没有聚合操作，所有聚合操作都是针对 keyedStream的

多流转化算子:
<br>
split -> select, collect -> coMap/coFlatMap

富函数：
<br>
比普通的Function多了 open，stop等流程控制，且可以获取到 runtimeContext，来获取state进行有状态的流式计算

####Flink支持的数据类型
1. Java和Scala的基础类型
<br>
2. Java 和 Scala 的元组
<br>
3. Scala 样例类
<br>
4. Java 简单对象 (POJOs)
<br>
5. 其他复杂类型

####Window
1. window 操作的两个主要步骤：
<br>窗口分配器(.window()), 窗口函数(reduce, aggregate, apply, process)
<br>
2. window 类型
<br>通过窗口分配器决定，TimeWindow(时间窗口) GlobalWindow(计数窗口)
<br>按照窗口起止点的定义，TumblingWindow(滚动窗口) SlidingWindow(滑动窗口)
<br>(滑动窗口中数据可能同时属于多个窗口，属于 size/slide 个窗口)
<br>会话窗口，窗口长度不固定，需要指定会话间隔
<br>
3. 窗口函数
<br>窗口函数是基于当前窗口内的数据的，是有界数据集的计算，通常只在窗口关闭时输出一次
<br>增量聚合函数：ReduceFunction AggregateFunction 接近流式处理的过程，来一条就处理一条
<br>全窗口函数：WindowFunction ProcessWindowFunction 接近批处理的过程，对window的所有数据进行处理
<br>
4. 程序默认的时间语义是 Processing Time

####Watermark
1. watermark 就是 EventTime 代表当前处理的进展
<br>
2. 主要用来处理乱序数据，一般定义一个延迟时间，延迟触发窗口的操作
<br>这个延迟指的是，当前的 EventTime 相对于窗口 end timestamp 的延迟
<br>
3. watermark 延迟时间的设置需要根据乱序的情况来定，通常设置为最大的乱序程度
<br>
4. 关窗操作必须是 EventTime 进展到窗口关闭的时间，也就是 watermark 达到了窗口关闭时间
<br> 当前 Event 的 timestamp - 延迟时间 = watermark
<br>
5. watermark 表示，watermark 之后不会再来比 watermark 时间早的数据了
<br>上游的不同的分区会有各自的 watermark，当前任务的 EventTime 是所有分区中最小的 watermark
<br>
6. Flink 对于乱序数据的三种处理
<br>watermark 设置延迟时间，设定watermark后，窗口的关闭只看watermark的时间
<br>window 的 allowedLateness 设置窗口允许处理迟到数据的时间，只对 event-time的窗口有效，设定的是窗口到达关闭时间后继续保持开启多久
<br>window 的 sideOutputLateData 将迟到的数据写入侧输出流

#### 状态管理
Flink中的状态
1. 算子状态(Operator State)
<br>
2. 键控状态(Keyed State)
<br>
3. 状态后端(State Backends) 

由一个任务(Task)维护的，用于计算某个结果的数据，都属于这个任务的状态
<br>可以认为状态就是一个本地变量，可以被任务的业务逻辑访问
<br>Flink 会进行状态的管理，包括状态一致性，故障处理，高效存储和访问

算子状态作用于当前算子任务，由同一并行任务所处理的数据可以访问相同的状态，
状态对同一子任务而言是共享的，算子状态不能由相同或不同算子的另一子任务访问(算子状态保存在当前 task 的内存，所以并行任务也不能互相访问到算子状态)
<br>键控状态作用于当前 key，根据输入流中定义的 key 来维护和访问，
每一个key维护一个状态实例，相同key的数据分发至同一个算子任务，由这个算子任务维护状态
