package window.learning.window;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import window.learning.constant.BizModelField;
import window.learning.entity.Indicator;

public class IndicatorCount implements WindowFunction<Map<String, String>, Indicator,Integer, TimeWindow> {
	private static final long serialVersionUID = 4444636528638482616L;

	@Override
	public void apply(Integer key, TimeWindow window, Iterable<Map<String, String>> input, Collector<Indicator> out)
			throws Exception {
		if(input!=null) {
			Map<String,Indicator> indicators =new HashMap<String, Indicator>();
			
			for (Map<String, String> record : input) {
				String env=record.get(BizModelField.ITEM_SRV_ENV);
				String system=record.get(BizModelField.ITEM_SRV_SYS);
				String node=record.get(BizModelField.ITEM_SRV_NODE);
				String operate=record.get(BizModelField.ITEM_SRV_OPERATE);
				String indicatorKey=env+"_"+system+"_"+node+"_"+operate;
				Indicator  indicator=indicators.get(indicatorKey);
				long windowStartTime=window.getStart();
				boolean isSuccess=BizModelField.VAL_N.equals(record.get(BizModelField.ITEM_BIZ_RTN_CODE))?true:false;
				boolean isTimeOut=BizModelField.VAL_T.equals(record.get(BizModelField.ITEM_BIZ_IS_TIMEOUT))?true:false;
				Long takeTime=Long.parseLong(record.get(BizModelField.ITEM_BIZ_TAKE_TIME));
				if(indicator==null) {
					indicators.put(indicatorKey, createIndicator(env,system,node,operate,takeTime,isSuccess,isTimeOut,windowStartTime));
				}else {
					mergeIndicator(indicator, env,  system,  node,  operate,  takeTime, isSuccess,  isTimeOut,  windowStartTime);
				}
			}
			for(String keyName:indicators.keySet()) {
				out.collect(indicators.get(keyName));
			}
			
		}
		
	}


	private void mergeIndicator(Indicator indicator, String env, String system, String node, String operate,
			Long takeTime, boolean isSuccess, boolean isTimeOut, long windowStartTime) {
		if(isSuccess) {
			indicator.setSuccesCount(indicator.getSuccesCount()+1);
		}else {
			indicator.setFailCount(indicator.getFailCount()+1);
		}
		if(isTimeOut) {
			indicator.setTimeOutCount(indicator.getTimeOutCount()+1);
		}
		long total=indicator.getTotal();
		if(takeTime>=0) {
			long avgTime =indicator.getAvgTime();
			long minTime =indicator.getMinTime();
			long maxTime =indicator.getMaxTime();
			avgTime =(avgTime*total+takeTime)/(total+1);
			if(takeTime>maxTime) {
				maxTime=takeTime;
			}
			if(takeTime<minTime) {
				minTime=takeTime;
			}
			indicator.setAvgTime(avgTime);
			indicator.setMinTime(minTime);
			indicator.setMaxTime(maxTime);
		}
		indicator.setTotal(indicator.getTotal()+1);
	}


	private Indicator createIndicator(String env, String system, String node, String operate, Long takeTime, boolean isSuccess, boolean isTimeOut, long windowStartTime) {
		Indicator indicator=new Indicator();
		indicator.setTimeStamp(windowStartTime);
		indicator.setEnv(env);
		indicator.setSystem(system);
		indicator.setNode(node);
		indicator.setOperate(operate);
		if(isSuccess) {
			indicator.setSuccesCount(1);
			indicator.setFailCount(0);
		}else {
			indicator.setSuccesCount(0);
			indicator.setFailCount(1);
		}
		if(isTimeOut) {
			indicator.setTimeOutCount(1);
		}else {
			indicator.setTimeOutCount(0);
		}
		if(takeTime>0) {
			indicator.setMinTime(takeTime);
			indicator.setMaxTime(takeTime);
			indicator.setAvgTime(takeTime);
		}else {
			indicator.setMinTime(0);
			indicator.setMaxTime(0);
			indicator.setAvgTime(0);
		}
		indicator.setTotal(1);
		return indicator;
	}
}
