package window.learning.marker;

import java.util.Map;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import window.learning.constant.BizModelField;

@Slf4j
public class IndicatorWindowWatermark implements AssignerWithPeriodicWatermarks<Map<String,String>> {
	private static final long serialVersionUID = 8092490295254552190L;

	public long extractTimestamp(Map<String, String> input, long time) {
		if(input!=null) {
			try {
				long timeStamp=Long.parseLong(""+input.get(BizModelField.ITEM_BIZ_TIMESTAMP));
				return	timeStamp;
			} catch (Exception e) {
				log.error("提取时间戳异常：{},该记录为{}",e,JSON.toJSONString(input));
				return 0;
			}
		}
		return 0;
	}

	public Watermark getCurrentWatermark() {
		
		return new Watermark(System.currentTimeMillis() - 20*1000-2000);
	}

}
