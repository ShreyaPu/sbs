package com.alphax.common.exception;

import com.alphax.common.rest.message.Message;

/**
 * @author A106744104
 *
 */
public class AlphaXException extends RuntimeException {
	
	private static final long serialVersionUID = 5963155784444332893L;
	
	// application specific error codes
	
	public static final String INVALID_INPUT_PARAMETER_FORMAT = "1112";
	public static final String PARAMETER_MISMATCH =  "1113";
	public static final String REQUIRED_PARAMETER_MISSING = "1114";
	public static final String COBOL_CALL_FAILED = "1016";
	
	public static final String DB_CALL_FAILED = "1018";
	public static final String POPUP_ERROR_CODE = "1019";
	public static final String DUPLICATE_ENTITY = "1020";
	
	public static final String ENTITY_CAN_NOT_BE_ADDED = "20103";
	public static final String ENTITY_CAN_NOT_BE_UPDATED = "1003";
	public static final String ENTITY_CAN_NOT_BE_DELETED = "1004";
	public static final String ENTITY_CAN_NOT_BE_RETRIVED = "20111";
	public static final String ENTITY_LIST_CAN_NOT_BE_RETRIVED = "20102";
	public static final String ENTITY_DOES_NOT_EXIST = "20112";
	public static final String SERVER_ERROR = "20002";
	public static final String COBOL_CALL_TIMEOUT = "20105";
	public static final String LICENCE_UNAVAILABLE = "20113";
	public static final String DAILY_CLOSING_PROCESS = "20181";
	public static final String INSTALLATIO_UPDATE_PROCESS = "20182";
	public static final String END_OF_YEAR_PROCESS = "20183";
	public static final String MAINTENANCE_WORK_PROCESS = "20194";
	
	public static final String AUTHENTICATION_FAILED = "401";
	
		
	private final String type;
	
	private final String messageType;
	
	private final String messageLocale;
	
	public AlphaXException(String message, String type) {
		super(message);
		this.type = type;
		this.messageType = "";
		this.messageLocale = "";
	}
	
	public AlphaXException(String message, String key,String type,String locale) {
		super(message);
		this.type = key;
		this.messageType = type;
		this.messageLocale = locale;
	}

	public AlphaXException(Throwable cause, String message, String type) {
		super(message, cause);
		this.type = type;
		this.messageType = "";
		this.messageLocale = "";
	}
	
	public AlphaXException(Message message) {
		super(message.getMessageContent());
		this.type = message.getMessageNumber();
		this.messageType = message.getMessageType().toString();
		this.messageLocale = message.getMessageLocale();
	}
	
	public AlphaXException(Throwable cause, Message message) {
		super(message.getMessageContent(), cause);
		this.type = message.getMessageNumber();
		this.messageType = message.getMessageType().toString();
		this.messageLocale = message.getMessageLocale();
	}

	public String getType() {
		return type;
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        if (!type.equalsIgnoreCase("0")) {
            builder.append("\n");
            builder.append("Exception type = ".concat(String.valueOf(type)));
        }
        if (null != super.getCause()) {
            builder.append("\n");
            builder.append("Cause = ".concat(super.getCause().toString()));
        }
        return builder.toString();
    }

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @return the messageLocale
	 */
	public String getMessageLocale() {
		return messageLocale;
	}

}
