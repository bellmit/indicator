package window.learning.entity;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Indicator {
	private long timeStamp;
	private String env;
	private String system;
	private String node;
	private String operate;
	private long minTime;
	private long maxTime;
	private long avgTime;
	private long succesCount;
	private long failCount;
	private long timeOutCount;
	private long total;
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
