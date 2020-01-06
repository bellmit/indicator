package window.learning.marker;

import java.util.Map;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import lombok.extern.slf4j.Slf4j;
import window.learning.constant.MetaField;


@Slf4j
public class CompareWindowWatermark implements AssignerWithPeriodicWatermarks<Map<String,String>> {
	private static final long serialVersionUID = 8092490295254552190L;

	public long extractTimestamp(Map<String, String> input, long time) {
		if(input!=null) {
			try {
//				System.err.println(System.currentTimeMillis()-Long.parseLong(""+input.get(MetaField.ITEM_TIMESTAMP)));
				return	Long.parseLong(""+input.get(MetaField.ITEM_TIMESTAMP));
			} catch (Exception e) {
				log.error("提取时间戳异常：{}",e);
			}
		}
		return 0;
	}

	public Watermark getCurrentWatermark() {
		
		return new Watermark(System.currentTimeMillis() - 20*1000);
	}

}
