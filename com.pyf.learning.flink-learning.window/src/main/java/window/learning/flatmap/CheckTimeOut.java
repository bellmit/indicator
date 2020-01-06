package window.learning.flatmap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.IterationRuntimeContext;
import org.apache.flink.api.common.functions.RichFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import lombok.extern.slf4j.Slf4j;
import window.learning.constant.BizModelField;
import window.learning.constant.SystemParam;

@Slf4j
public class CheckTimeOut extends Thread implements RichFunction,FlatMapFunction<Map<String,String>, Map<String,String>>{

	private static final long serialVersionUID = -7222272574166722150L;
	private final static String SQL_QUERY_TIMEOUT="SELECT * FROM biz_timeout;";
	private Map<String, Integer> timeOutRule;
	private boolean working;
	private RuntimeContext runtimeContext;
	@Override
	public void flatMap(Map<String, String> input, Collector<Map<String, String>> output) throws Exception {
		try {
			long srv_req_time=-1;
			long srv_rsp_time=-1;
			long srv_time=-1;
			String  envName			=input.get(BizModelField.ITEM_SRV_ENV);
			String  systemName		=input.get(BizModelField.ITEM_SRV_SYS);
			String  nodeName		=input.get(BizModelField.ITEM_SRV_NODE);
			String  operateName		=input.get(BizModelField.ITEM_SRV_OPERATE);
			try {
				srv_req_time=Long.parseLong(input.get(BizModelField.ITEM_SRV_REQ_TIME));
			} catch (Exception e) {}
			try {
				srv_rsp_time=Long.parseLong(input.get(BizModelField.ITEM_SRV_RSP_TIME));
			} catch (Exception e) {}
			
			if(srv_req_time>0&&srv_rsp_time>0) {
				srv_time=srv_rsp_time-srv_req_time;
			}
			input.put(BizModelField.ITEM_BIZ_IS_TIMEOUT, isTimeOut(envName,systemName,nodeName,operateName,srv_time)?BizModelField.VAL_T:BizModelField.VAL_F);
			input.put(BizModelField.ITEM_BIZ_TAKE_TIME, ""+srv_time);
			if(srv_req_time>0) {
				output.collect(input);
			}
		} catch (Exception e) {
			log.error("超时校验异常：{}",e);
		}
	}



	private boolean isTimeOut(String envName, String systemName, String nodeName, String operateName, long srv_time) {
		boolean isTimeOut=true;
		if(srv_time<0) isTimeOut=true;
		
		Integer timeOutLimit_operateName= this.timeOutRule.get(envName+"_"+systemName+"_"+nodeName+"_"+operateName);
		if(timeOutLimit_operateName!=null) {
			return  srv_time>timeOutLimit_operateName;
		}
		timeOutLimit_operateName= this.timeOutRule.get(envName+"_"+systemName+"_"+nodeName+"_"+SystemParam.VAL_DEFAULT);
		if(timeOutLimit_operateName!=null) {
			return  srv_time>timeOutLimit_operateName;
		}
		
		
		Integer timeOutLimit_nodeName= this.timeOutRule.get(envName+"_"+systemName+"_"+nodeName);
		if(timeOutLimit_nodeName!=null) {
			return  srv_time>timeOutLimit_nodeName;
		}
		timeOutLimit_nodeName= this.timeOutRule.get(envName+"_"+systemName+"_"+SystemParam.VAL_DEFAULT);
		if(timeOutLimit_nodeName!=null) {
			return  srv_time>timeOutLimit_nodeName;
		}
		
		Integer timeOutLimit_systemName= this.timeOutRule.get(envName+"_"+systemName);
		if(timeOutLimit_systemName!=null) {
			return  srv_time>timeOutLimit_systemName;
		}
		timeOutLimit_systemName= this.timeOutRule.get(envName+"_"+SystemParam.VAL_DEFAULT);
		if(timeOutLimit_systemName!=null) {
			return  srv_time>timeOutLimit_systemName;
		}
		
		Integer timeOutLimit_envName= this.timeOutRule.get(envName);
		if(timeOutLimit_envName!=null) {
			return  srv_time>timeOutLimit_envName;
		}
		timeOutLimit_envName= this.timeOutRule.get(SystemParam.VAL_DEFAULT);
		if(timeOutLimit_envName!=null) {
			return  srv_time>timeOutLimit_envName;
		}
		
		isTimeOut=false;
		return isTimeOut;
	}



	@Override
	public void open(Configuration parameters) throws Exception {
		this.working=true;
		this.timeOutRule=new HashMap<String,Integer>();
		updateTimeOutRule();
		this.start();
	}
	
	@Override
	public void run() {
		while (working) {
			try {
				//休眠
				Thread.sleep(SystemParam.UPDATE_FREQUENCE);
//				if(!this.working) return;
				//更新超时规则表
				updateTimeOutRule();
			} catch (Exception e) {
				log.error("更新超时规则异常：{}",e);
			}
			
		}
	}
	
	private void updateTimeOutRule() throws SQLException {
		Connection con =null;
		Statement st=null;
		ResultSet rs=null;
		try {
			Class.forName(SystemParam.DB_DRIVER);
			con = DriverManager.getConnection(SystemParam.ENV_DEV_DB_URL,SystemParam.DB_USER,SystemParam.DB_PWD);
			st =con.createStatement();
			rs=st.executeQuery(SQL_QUERY_TIMEOUT);
			Map<String,Integer> tempRule =new HashMap<String, Integer>();
			while (rs.next()) {
				String  envName			=rs.getString("envName");
				String  systemName		=rs.getString("systemName");
				String  nodeName		=rs.getString("nodeName");
				String  operateName		=rs.getString("operateName");
				Integer timeOutLimit	=rs.getInt("timeOutLimit");
				if(StringUtils.isBlank(envName)) {
					envName=SystemParam.VAL_DEFAULT;
					tempRule.put(envName, timeOutLimit);
					continue;
				}
				
				if(StringUtils.isBlank(systemName)) {
					systemName=SystemParam.VAL_DEFAULT;
					tempRule.put(envName+"_"+systemName, timeOutLimit);
					continue;
				}
				
				if(StringUtils.isBlank(nodeName)) {
					nodeName=SystemParam.VAL_DEFAULT;
					tempRule.put(envName+"_"+systemName+"_"+nodeName, timeOutLimit);
					continue;
				}
				
				if(StringUtils.isBlank(operateName)) {
					operateName=SystemParam.VAL_DEFAULT;
					tempRule.put(envName+"_"+systemName+"_"+nodeName+"_"+operateName, timeOutLimit);
					continue;
				}
			}
			this.timeOutRule=tempRule;
		} catch (Exception e) {
			
		}finally {
			if(con!=null)con.close();
		}
		
	}

	@Override
	public void close() throws Exception {
		this.working=false;
	}


	@Override
	public IterationRuntimeContext getIterationRuntimeContext() {
		return null;
	}

	@Override
	public RuntimeContext getRuntimeContext() {
		return this.runtimeContext;
	}
	
	@Override
	public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext=runtimeContext;
	}

}
