package window.learning.env;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import window.learning.constant.EnvParamDefination;
import window.learning.constant.SystemParam;

public class FlinkInitEnv {
	private final static Logger logger=LoggerFactory.getLogger(FlinkInitEnv.class);
	public final static String SQL_QUERY_ENVPARAM="SELECT * FROM Env_Parameter";
	public static String dbUrl;
	public static String jobName;
	public static String zkServers;
	public static String kafkaServers;
	public static String jumpTopic;
	public static String indicatorTopic;
	public static Properties kafkaProperties=new Properties();
	public  FlinkInitEnv(String envName) throws Exception{
		if(SystemParam.ENV_DEV.equals(envName)) {
			dbUrl=SystemParam.ENV_DEV_DB_URL;
		}else if(SystemParam.ENV_SIT.equals(envName)) {
			dbUrl=SystemParam.ENV_SIT_DB_URL;
		}else if(SystemParam.ENV_PRD.equals(envName)) {
			dbUrl=SystemParam.ENV_PRD_DB_URL;
		}else {
			throw new Exception("请指定环境，或者环境"+envName+"不存在！");
		}
		Connection con =null;
		Statement st=null;
		ResultSet rs=null;
		try {
			Class.forName(SystemParam.DB_DRIVER);
			con=DriverManager.getConnection(dbUrl, SystemParam.DB_USER, SystemParam.DB_PWD);
			st=con.createStatement();
			rs=st.executeQuery(SQL_QUERY_ENVPARAM);
			while (rs.next()) {
				String paramName=rs.getString(SystemParam.FIELD_PARAM_NAME);
				String paramValue=rs.getString(SystemParam.FIELD_PARAM_VALUE);
				if(EnvParamDefination.PARAM_ZOOKEEPER_SERVERS.equals(paramName)) {
					zkServers=paramValue;
					kafkaProperties.put(SystemParam.PROPERTY_ZOOKEEPER_SERVER, paramValue);
				}else if(EnvParamDefination.PARAM_KAFKA_SERVERS.equals(paramName)) {
					kafkaServers=paramValue;
					kafkaProperties.put(SystemParam.PROPERTY_KAFKA_SERVER, paramValue);
				}else if(EnvParamDefination.PARAM_GROUP_ID.equals(paramName)) {
					kafkaProperties.put(SystemParam.PROPERTY_GROUP_ID, paramValue);
				}else if(EnvParamDefination.PARAM_JUMP_TOPIC.equals(paramName)) {
					jumpTopic=paramValue;
				}else if(EnvParamDefination.PARAM_INDICATOR_TOPIC.equals(paramName)) {
					indicatorTopic=paramValue;
				}else if(EnvParamDefination.PARAM_JOB_NAME.equals(paramName)) {
					jobName=paramValue;
				}
			}
		} catch (Exception e) {
			logger.info("加载环境参数异常：{}",e);
		}finally {
			//关闭数据库连接
			if(con!=null) con.close();
			if(st!=null) st.close();
			if(rs!=null) rs.close();
		}
	}
	public static void init(String env){
		try {
			new FlinkInitEnv(env);
		} catch (Exception e) {
			logger.error("初始化异常：{}",e);
		}
	}
}
