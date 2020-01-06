package window.learning;

import java.util.Map;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import window.learning.constant.SystemParam;
import window.learning.entity.Indicator;
import window.learning.env.FlinkInitEnv;
import window.learning.flatmap.CheckTimeOut;
import window.learning.flatmap.JumpDataParser;
import window.learning.key.KeyByCheckId;
import window.learning.key.KeyByEnv_sys_node_operate;
import window.learning.marker.CompareWindowWatermark;
import window.learning.marker.IndicatorWindowWatermark;
import window.learning.window.CompareByCheckId;
import window.learning.window.IndicatorCount;


public class FlinkWindowMain {
	public static void main(String[] args) throws Exception {
		String envName=args[0];
		FlinkInitEnv.init(envName);
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
		env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));//逻辑失败后重新加载（4次，每秒间隔10000ms）
		//读取kafka消息

		DataStream<Map<String,String>> JumpStream=env.addSource(new FlinkKafkaConsumer010<String>(FlinkInitEnv.jumpTopic,new SimpleStringSchema(),FlinkInitEnv.kafkaProperties))
				.name("读取EMON数据1")
				.flatMap(new JumpDataParser())
				.name("解析EMON数据1");
//		DataStream<Map<String,String>> EMONStream2= env.socketTextStream(SystemParam.HOST_NAME,SystemParam.PORT)
//				.name("读取EMON数据2")
//				.flatMap(new JumpDataParser())
//				.name("解析EMON数据2");
//		EMONStream1.union(EMONStream2)
		DataStream<Map<String,String>> ComparedStream =JumpStream
			.assignTimestampsAndWatermarks(new CompareWindowWatermark())
			.name("提取时间戳")
			.keyBy(new KeyByCheckId())
			.timeWindow(Time.milliseconds(SystemParam.COMPARE_WINDOW_LENGTH),Time.milliseconds(SystemParam.COMPARE_WINDOW_STEP))
//			.trigger(new CompareWindowTrigger())
			.apply(new CompareByCheckId())
			.name("数据匹配");

//		ComparedStream
//			.flatMap(new MapToJsonString())
//			.name("数据JSON化")
//			.addSink(new FlinkKafkaProducer010<String>(FlinkInitEnv.indicatorTopic, new SimpleStringSchema(), FlinkInitEnv.kafkaProperties))
//			.name("保存数据到kafka");
		
		
		DataStream<Indicator> indicatorStream=ComparedStream
						.flatMap(new CheckTimeOut())
						.assignTimestampsAndWatermarks(new IndicatorWindowWatermark())
						.keyBy(new KeyByEnv_sys_node_operate())
						.timeWindow(Time.milliseconds(1000))
						.apply(new IndicatorCount())
						.name("指标统计");
		indicatorStream.print();
		env.execute(FlinkInitEnv.jobName);
	}
}
