package window.learning.constant;

public interface BizModelField {
	//公共字段
	public static final String ITEM_BIZ_TRACE_ID		="MOD_BIZ_TRACE_ID";
	public static final String ITEM_BIZ_SPAN_ID			="MOD_BIZ_SPAN_ID";
	public static final String ITEM_BIZ_TIMESTAMP		="MOD_BIZ_TIMESTAMP";
	public static final String ITEM_BIZ_RTN_CODE		="MOD_BIZ_RTN_CODE";
	public static final String ITEM_BIZ_OPERATE			="MOD_BIZ_OPERATE";
	public static final String ITEM_BIZ_REQ_MSG			="MOD_BIZ_REQ_MSG";
	public static final String ITEM_BIZ_RSP_MSG			="MOD_BIZ_RSP_MSG";
	public static final String ITEM_BIZ_IS_TIMEOUT 		="MOD_BIZ_IS_TIMEOUT";
	public static final String ITEM_BIZ_IS_SUCCESS 		="MOD_BIZ_IS_SUCCESS";
	public static final String ITEM_BIZ_TAKE_TIME 		="MOD_BIZ_TAKE_TIME";
	
	//发起方
	public static final String ITEM_CAL_REQUEST_ID		="MOD_CAL_REQUEST_ID";
	public static final String ITEM_CAL_ENV		    	="MOD_CAL_ENV";
	public static final String ITEM_CAL_SYS		    	="MOD_CAL_SYS";
	public static final String ITEM_CAL_NODE		    ="MOD_CAL_NODE";
	public static final String ITEM_CAL_OPERATE		    ="MOD_CAL_OPERATE";
	public static final String ITEM_CAL_REQ_TIME		="MOD_CAL_REQ_TIME";
	public static final String ITEM_CAL_RSP_TIME		="MOD_CAL_RSP_TIME";
	
	//服务方
	public static final String ITEM_SRV_REQUEST_ID		="MOD_SRV_REQUEST_ID";
	public static final String ITEM_SRV_ENV		    	="MOD_SRV_ENV";
	public static final String ITEM_SRV_SYS		    	="MOD_SRV_SYS";
	public static final String ITEM_SRV_NODE		    ="MOD_SRV_NODE";
	public static final String ITEM_SRV_OPERATE		    ="MOD_SRV_OPERATE";
	public static final String ITEM_SRV_REQ_TIME		="MOD_SRV_REQ_TIME";
	public static final String ITEM_SRV_RSP_TIME		="MOD_SRV_RSP_TIME";
	
	public static final String VAL_T ="T";
	public static final String VAL_F ="F";
	public static final String VAL_N ="N";
	public static final String VAL_E ="E";
	
	public static final String [] ITEMS= {
			ITEM_BIZ_TRACE_ID,
			ITEM_BIZ_SPAN_ID,
			ITEM_BIZ_TIMESTAMP,
			ITEM_BIZ_RTN_CODE,
			ITEM_BIZ_OPERATE,
			ITEM_BIZ_REQ_MSG,
			ITEM_BIZ_RSP_MSG,
			ITEM_CAL_REQUEST_ID,
			ITEM_CAL_ENV,
			ITEM_CAL_SYS,
			ITEM_CAL_NODE,
			ITEM_CAL_OPERATE,
			ITEM_CAL_REQ_TIME,
			ITEM_CAL_RSP_TIME,
			ITEM_SRV_REQUEST_ID,
			ITEM_SRV_ENV,
			ITEM_SRV_SYS,
			ITEM_SRV_NODE,
			ITEM_SRV_OPERATE,
			ITEM_SRV_REQ_TIME,
			ITEM_SRV_RSP_TIME
	};
	
	
	public static final String [][] META_FEILED_TO_CAL_REQ_MODEL= {
			{ITEM_BIZ_TRACE_ID,MetaField.ITEM_TRACE_ID},
			{ITEM_BIZ_SPAN_ID, MetaField.ITEM_SPAN_ID},
			{ITEM_BIZ_REQ_MSG, MetaField.ITEM_RTN_MSG},
			{ITEM_CAL_REQ_TIME, MetaField.ITEM_TIMESTAMP},
			{ITEM_CAL_ENV, MetaField.ITEM_ENV},
			{ITEM_CAL_SYS, MetaField.ITEM_SYS},
			{ITEM_CAL_NODE, MetaField.ITEM_NODE}
	};
	public static final String [][] META_FEILED_TO_SRV_REQ_MODEL= {
			{ITEM_BIZ_TRACE_ID,MetaField.ITEM_TRACE_ID},
			{ITEM_BIZ_SPAN_ID, MetaField.ITEM_SPAN_ID},
			{ITEM_BIZ_REQ_MSG, MetaField.ITEM_RTN_MSG},
			{ITEM_BIZ_TIMESTAMP, MetaField.ITEM_TIMESTAMP},
			{ITEM_BIZ_OPERATE, MetaField.ITEM_OPERATE},
			{ITEM_SRV_REQ_TIME, MetaField.ITEM_TIMESTAMP},
			{ITEM_SRV_ENV, MetaField.ITEM_ENV},
			{ITEM_SRV_SYS, MetaField.ITEM_SYS},
			{ITEM_SRV_NODE, MetaField.ITEM_NODE}
	};
	public static final String [][] META_FEILED_TO_SRV_RSP_MODEL= {
			{ITEM_BIZ_TRACE_ID,MetaField.ITEM_TRACE_ID},
			{ITEM_BIZ_SPAN_ID, MetaField.ITEM_SPAN_ID},
			{ITEM_BIZ_RSP_MSG, MetaField.ITEM_RTN_MSG},
			{ITEM_SRV_RSP_TIME, MetaField.ITEM_TIMESTAMP},
			{ITEM_SRV_ENV, MetaField.ITEM_ENV},
			{ITEM_SRV_SYS, MetaField.ITEM_SYS},
			{ITEM_SRV_NODE, MetaField.ITEM_NODE}
	};
	public static final String [][] META_FEILED_TO_CAL_RSP_MODEL= {
			{ITEM_BIZ_TRACE_ID,MetaField.ITEM_TRACE_ID},
			{ITEM_BIZ_SPAN_ID, MetaField.ITEM_SPAN_ID},
			{ITEM_BIZ_RSP_MSG, MetaField.ITEM_RTN_MSG},
			{ITEM_CAL_RSP_TIME, MetaField.ITEM_TIMESTAMP},
			{ITEM_CAL_ENV, MetaField.ITEM_ENV},
			{ITEM_CAL_SYS, MetaField.ITEM_SYS},
			{ITEM_CAL_NODE, MetaField.ITEM_NODE}
	};
}
