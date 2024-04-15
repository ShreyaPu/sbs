package com.alphax.common.response;

/**
 * @author A106744104
 *
 */
public class ResponseMessage {

	private String serverMsgId;
	private String serverMsgLangCode;// EN/DE;
	private String serverMsgType; //Success/Information/Error/Warning
	private String serverMsg;
	
	
	/**
	 * @return the serverMsgId
	 */
	public String getServerMsgId() {
		return serverMsgId;
	}
	/**
	 * @param serverMsgId the serverMsgId to set
	 */
	public void setServerMsgId(String serverMsgId) {
		this.serverMsgId = serverMsgId;
	}
	/**
	 * @return the serverMsgLangCode
	 */
	public String getServerMsgLangCode() {
		return serverMsgLangCode;
	}
	/**
	 * @param serverMsgLangCode the serverMsgLangCode to set
	 */
	public void setServerMsgLangCode(String serverMsgLangCode) {
		this.serverMsgLangCode = serverMsgLangCode;
	}
	/**
	 * @return the serverMsgType
	 */
	public String getServerMsgType() {
		return serverMsgType;
	}
	/**
	 * @param serverMsgType the serverMsgType to set
	 */
	public void setServerMsgType(String serverMsgType) {
		this.serverMsgType = serverMsgType;
	}
	/**
	 * @return the serverMsg
	 */
	public String getServerMsg() {
		return serverMsg;
	}
	/**
	 * @param serverMsg the serverMsg to set
	 */
	public void setServerMsg(String serverMsg) {
		this.serverMsg = serverMsg;
	}

}
