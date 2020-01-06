package window.learning.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Record {
	private String tranceId;
	private String spanId;
	private String requestId;
	private String recordType;
	private String timeStamp;
	private String env;
	private String system;
	private String node;
	private String operate;
	private String rtnCode;
	private String message;
}
