package window.learning.constant;

public class JumpFeild {
	public static final String tranceId			="tranceId";
	public static final String spanId			="spanId";
	public static final String requestId		="requestId";
	public static final String recordType		="recordType";
	public static final String timeStamp		="timeStamp";
	public static final String env				="env";
	public static final String system			="system";
	public static final String node				="node";
	public static final String operate			="operate";
	public static final String rtnCode			="rtnCode";
	public static final String message			="message";
	
	public static final String[][]  metaFeildReflact={
			{tranceId,	MetaField.ITEM_TRACE_ID},
			{spanId,	MetaField.ITEM_SPAN_ID},
			{requestId,	MetaField.ITEM_REQUEST_ID},
			{recordType,MetaField.ITEM_RECORD_TYPE},
			{timeStamp,	MetaField.ITEM_TIMESTAMP},
			{env,		MetaField.ITEM_ENV},
			{system,	MetaField.ITEM_SYS},
			{node,		MetaField.ITEM_NODE},
			{operate,	MetaField.ITEM_OPERATE},
			{rtnCode,	MetaField.ITEM_RTN_CODE},
			{message,	MetaField.ITEM_RTN_MSG},
	};
}
