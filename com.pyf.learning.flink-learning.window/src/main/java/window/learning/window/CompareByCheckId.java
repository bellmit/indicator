package window.learning.window;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import window.learning.constant.BizModelField;
import window.learning.constant.MetaField;
import window.learning.constant.SystemParam;

public class CompareByCheckId implements WindowFunction<Map<String,String> ,Map<String,String> , Integer, TimeWindow>{
	private static final long serialVersionUID = -160006173871275618L;
	private final static Logger logger=LoggerFactory.getLogger(CompareByCheckId.class);
	@Override
	public void apply(Integer key, TimeWindow window, Iterable<Map<String, String>> input,
			Collector<Map<String, String>> out) throws Exception {
		if(input!=null) {
			try {
				Iterator<Map<String, String>> iterator=input.iterator();
				while (iterator.hasNext()) {
					Map<String, String> data=iterator.next();
					if(MetaField.VAL_DATA_TYPE_IGNORE.equals(data.get(MetaField.ITEM_DATA_TYPE))) iterator.remove();
				}
				//缓存
				Map<String ,Map<String,String>> cache =new HashMap<String, Map<String,String>>();
				for(Map<String,String> record:input) {
					if(record.get(MetaField.ITEM_FIRST_TIME_INPUT_WINDOW)==null) {
						record.put(MetaField.ITEM_FIRST_TIME_INPUT_WINDOW, ""+(window.getEnd()-SystemParam.COMPARE_WINDOW_STEP));
					}
					if(MetaField.VAL_DATA_TYPE_IGNORE.equals(record.get(MetaField.ITEM_DATA_TYPE))) continue;
					String checkId=record.get(MetaField.ITEM_CHECK_ID);
					Map<String, String> existRecord=cache.get(checkId);
					if(existRecord!=null) {
						merge(existRecord,record);	//将existRecord合并到record,并更改record
					}
					cache.put(checkId, record);		//将record保存到缓存中。
				}
				for(Map<String,String> record:input) {
					if(MetaField.VAL_DATA_TYPE_IGNORE.equals(record.get(MetaField.ITEM_DATA_TYPE)))continue;
					Long firstInputWindowTime=Long.parseLong(""+record.get(MetaField.ITEM_FIRST_TIME_INPUT_WINDOW));
					if(window.getStart()==firstInputWindowTime) {
						if(MetaField.VAL_DATA_TYPE_META.equals(record.get(MetaField.ITEM_DATA_TYPE))) {
							metaFeildToBiz(record);
						}
						out.collect(record);
					}
				}
			} catch (Exception e) {
				logger.error("匹配过程异常：{}",e);
			}
			
			
			
		}
		
	}
	private void merge(Map<String, String> existRecord, Map<String, String> inputRecord) {
		if(MetaField.VAL_DATA_TYPE_META.equals(inputRecord.get(MetaField.ITEM_DATA_TYPE))) {
			//转换
			metaFeildToBiz(inputRecord);
		}
		if(MetaField.VAL_DATA_TYPE_META.equals(existRecord.get(MetaField.ITEM_DATA_TYPE))) {
			//转换
			metaFeildToBiz(existRecord);
		}
		mergeModel(existRecord, inputRecord);//将existRecord合并到inputRecord
	}
	
	private void mergeModel(Map<String, String> existRecord, Map<String, String> inputRecord) {
		for(String bizFeild:BizModelField.ITEMS) {
			String value=existRecord.get(bizFeild);
			if(StringUtils.isBlank(value)) continue;
			if(StringUtils.isBlank(inputRecord.get(bizFeild))) {
				inputRecord.put(bizFeild, value);
			}
		}
//		existRecord.clear();
	}
	private void metaFeildToBiz(Map<String, String> inputRecord) {
		Map<String, String> copyData=new HashMap<String, String>(inputRecord);
		inputRecord.clear();
		inputRecord.put(MetaField.ITEM_CHECK_ID, copyData.get(MetaField.ITEM_CHECK_ID));
		inputRecord.put(MetaField.ITEM_TIMESTAMP, copyData.get(MetaField.ITEM_TIMESTAMP));
		inputRecord.put(MetaField.ITEM_FIRST_TIME_INPUT_WINDOW, copyData.get(MetaField.ITEM_FIRST_TIME_INPUT_WINDOW));
		
		String [][] bizModel=null;
		String recordType=copyData.get(MetaField.ITEM_RECORD_TYPE);
		switch (recordType) {
			case MetaField.VAL_RECORD_TYPE_CAL_REQ:bizModel=BizModelField.META_FEILED_TO_CAL_REQ_MODEL;break;
			case MetaField.VAL_RECORD_TYPE_SRV_REQ:bizModel=BizModelField.META_FEILED_TO_SRV_REQ_MODEL;break;
			case MetaField.VAL_RECORD_TYPE_SRV_RSP:bizModel=BizModelField.META_FEILED_TO_SRV_RSP_MODEL;break;
			case MetaField.VAL_RECORD_TYPE_CAL_RSP:bizModel=BizModelField.META_FEILED_TO_CAL_RSP_MODEL;break;
		}
		if(bizModel!=null) {
			for(String[] item:bizModel) {
				String metaFeild=item[1];
				String bizModelFeild=item[0];
				String value=copyData.get(metaFeild);
				if(StringUtils.isNotBlank(value)) inputRecord.put(bizModelFeild, value);
			}
		}
		inputRecord.put(MetaField.ITEM_DATA_TYPE, MetaField.VAL_DATA_TYPE_MODEL);
	}

}
