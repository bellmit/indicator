package window.learning.flatmap;

import java.util.Map;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MapToJsonString implements FlatMapFunction<Map<String,String>, String> {

	private static final long serialVersionUID = -2156465624437205039L;

	@Override
	public void flatMap(Map<String, String> input, Collector<String> out) throws Exception {
		try {
			if(input!=null) out.collect(JSON.toJSONString(input));
			System.out.println(JSON.toJSONString(input));
		} catch (Exception e) {
			log.error("转换成JSON异常：{}",e);
		}
		
	}

}
