package window.learning.flatmap;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import window.learning.constant.JumpFeild;
import window.learning.constant.MetaField;


public class JumpDataParser implements FlatMapFunction<String, Map<String,String>>{
	private static final long serialVersionUID = 115855010990509833L;
	private final static Logger logger=LoggerFactory.getLogger(JumpDataParser.class);
	@SuppressWarnings("unchecked")
	public void flatMap(String input, Collector<Map<String, String>> out) throws Exception {
		try {
			if(input!=null) {
				Map<String, String> record=(Map<String, String>)JSON.parse(input);
				Map<String, String> output=new HashMap<String, String>();
				for(String[] item :JumpFeild.metaFeildReflact) {
					String jumpFeild=item[0];
					String metaFeild=item[1];
					output.put(metaFeild, record.getOrDefault(jumpFeild, ""));
				}
				output.put(MetaField.ITEM_DATA_TYPE, MetaField.VAL_DATA_TYPE_META);
				output.put(MetaField.ITEM_CHECK_ID, record.getOrDefault(JumpFeild.spanId, ""));
				out.collect(output);
			}
		} catch (Exception e) {
			logger.error("JUMP数据解析异常：{}",e);
		}
	}
}
