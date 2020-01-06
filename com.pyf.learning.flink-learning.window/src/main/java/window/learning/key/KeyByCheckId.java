package window.learning.key;

import java.util.Map;

import org.apache.flink.api.java.functions.KeySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import window.learning.constant.MetaField;


public class KeyByCheckId implements KeySelector< Map<String,String>,Integer>{
	private static final long serialVersionUID = -9145627704567568960L;
	private final static Logger logger=LoggerFactory.getLogger(KeyByCheckId.class);

	public Integer getKey(Map<String, String> input) throws Exception {
		int IntegerKey=-1;
		try {
			if(input!=null) {
				String key=input.getOrDefault(MetaField.ITEM_CHECK_ID, "");
				IntegerKey=Math.abs(key.hashCode())%10000;
				return IntegerKey;
			}
		} catch (Exception e) {
			logger.error("数据分片异常：{}" ,e);
		}
		return IntegerKey;
	}

}
