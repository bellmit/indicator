package window.learning.window;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import window.learning.constant.MetaField;
import window.learning.constant.SystemParam;

public class EnvCount implements WindowFunction<Map<String,String> ,Map<String,String> , Integer, TimeWindow>{
	private static final long serialVersionUID = -733770171674935125L;

	public void apply(Integer key, TimeWindow window, Iterable<Map<String, String>> input,
			Collector<Map<String, String>> out) throws Exception {
		if(input!=null) {
			Map<String, String> cache=new HashMap<String, String>();
			try {
				for(Map<String, String> record:input) {
					String envName=record.getOrDefault(MetaField.ITEM_ENV, SystemParam.VAL_UNKNOW);
					if(cache.get(envName)!=null) {
						int count=Integer.parseInt(cache.get(envName));
						count++;
						cache.put(envName, count+"");
					}else {
						cache.put(envName, "1");
					}
				}
				out.collect(cache);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			
		}
		
	}

}
