package window.learning.constant;

public interface SystemParam {
	public static final String ENV_DEV="DEV";
	public static final String ENV_SIT="SIT";
	public static final String ENV_PRD="PRD";
	
	public static final String ENV_DEV_DB_URL="jdbc:mysql://192.168.230.130:3306/emon?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true";
	public static final String ENV_SIT_DB_URL="jdbc:mysql://192.168.230.130:3306/EMON?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true";
	public static final String ENV_PRD_DB_URL="jdbc:mysql://192.168.230.130:3306/EMON?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true";

	public static final String DB_DRIVER="com.mysql.jdbc.Driver";//com.mysql.cj.jdbc.Driver   mysql 8X及以上用这个驱动
	public static final String DB_USER="root";
	public static final String DB_PWD="1qaz@WSX";
	
	//Kafka连接所需参数
	public static final String PROPERTY_GROUP_ID="group.id";
	public static final String PROPERTY_KAFKA_SERVER="bootstrap.servers";
	public static final String PROPERTY_ZOOKEEPER_SERVER="zookeeper.connect";
	public static final String PROPERTY_BUFFER_MEMORY="buffer.memory";
	public static final String PROPERTY_MAX_REQUEST_SIZE="max.request.size";
	public static final String PROPERTY_MESSAGE_MAX_BYTES="message.max.bytes";
	public static final String FIELD_PARAM_NAME = "param_name";
	public static final String FIELD_PARAM_VALUE = "param_value";
	public static final String HOST_NAME = "192.168.230.130";
	public static final int PORT = 9001;
	
	public static final int COMPARE_WINDOW_LENGTH = 5000;
	public static final int COMPARE_WINDOW_STEP = 500;
	
	public static final String VAL_UNKNOW = "UNKNOW";
	
	public static final long UPDATE_FREQUENCE = 30000;
	public static final String VAL_DEFAULT = "default";
}
