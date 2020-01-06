package window.learning.key;

import java.util.Map;

import org.apache.flink.api.java.functions.KeySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import window.learning.constant.BizModelField;


public class KeyByEnv_sys_node_operate implements KeySelector< Map<String,String>,Integer>{
	private static final long serialVersionUID = -7022366655129646505L;
	private final static Logger logger=LoggerFactory.getLogger(KeyByEnv_sys_node_operate.class);

	public Integer getKey(Map<String, String> input) throws Exception {
		int IntegerKey=-1;
		try {
			if(input!=null) {
				String key=input.get(BizModelField.ITEM_SRV_ENV)
						+input.get(BizModelField.ITEM_SRV_SYS)
						+input.get(BizModelField.ITEM_SRV_NODE)
						+input.get(BizModelField.ITEM_SRV_OPERATE);
				IntegerKey=Math.abs(key.hashCode())%4;
				return IntegerKey;
			}
		} catch (Exception e) {
			logger.error("数据分片异常：{}" ,e);
		}
		return IntegerKey;
	}

}
