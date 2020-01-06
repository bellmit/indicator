package window.learning.trigger;

import java.util.Map;

import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class CompareWindowTrigger extends Trigger<Map<String,String>, TimeWindow>{

	private static final long serialVersionUID = 2285930001783607572L;

	public CompareWindowTrigger() {
	        super();
	    }

	    //每个元素被添加到窗口时都会调用该方法
	    @Override
	    public TriggerResult onElement(Map<String,String> element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
	       System.out.println("出发了");
	    	return TriggerResult.CONTINUE;
	    }

	    //当一个已注册的 ProcessingTime 计时器启动时调用
	    @Override
	    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
	    	return TriggerResult.CONTINUE;
	    }

	    //当一个已注册的 EventTime 计时器启动时调用
	    @Override
	    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
	        return TriggerResult.PURGE;
	    }

	    //与状态性触发器相关,当使用会话窗口时,两个触发器对应的窗口合并时,合并两个触发器的状态
	    @Override
	    public void onMerge(TimeWindow window, OnMergeContext ctx) throws Exception {
	        super.onMerge(window, ctx);
	    }

	    //执行任何需要清除的相应窗口
	    @Override
	    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {

	    }

}
