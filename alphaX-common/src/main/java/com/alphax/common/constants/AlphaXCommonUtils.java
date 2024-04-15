/**
 * 
 */
package com.alphax.common.constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.ResponseMessage;
import com.alphax.common.rest.message.Message;
import com.alphax.common.rest.message.service.MessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@Component
@Slf4j
public class AlphaXCommonUtils {
	
	@Autowired
	private MessageService messageService;


	/**
	 * @param msg Error msg
	 * @param errorCode     errorcode of the result.
	 * @return ResponseEntity object containing AlphaXResponse object.
	 */
	public List<ResponseMessage> buildAlphaXResponseEntity(List<ResponseMessage> responseMessageList, String msg, String messageCode, String messageType, String messageLocale) {
		log.info("Message is " + msg);
		ResponseMessage message = new ResponseMessage();

		message.setServerMsgId(messageCode);
		message.setServerMsgType(messageType);
		message.setServerMsg(msg);
		message.setServerMsgLangCode(messageLocale);

		if (CollectionUtils.isEmpty(responseMessageList)) {
			responseMessageList = new ArrayList<ResponseMessage>();
		}
		responseMessageList.add(message);

		return responseMessageList;
	}

	/**
	 * @param msg Error msg
	 * @param errorCode     errorcode of the result.
	 * @return ResponseEntity object containing AlphaXResponse object.
	 */
	public List<ResponseMessage> buildAlphaXResponseEntity(List<ResponseMessage> responseMessageList, Message responseMessage) {
		log.info("Message is " + responseMessage.getMessageContent());
		ResponseMessage message = new ResponseMessage();

		message.setServerMsgId(responseMessage.getMessageNumber());
		message.setServerMsgType(responseMessage.getMessageType().toString());
		message.setServerMsg(responseMessage.getMessageContent());
		message.setServerMsgLangCode(responseMessage.getMessageLocale());

		if (CollectionUtils.isEmpty(responseMessageList)) {
			responseMessageList = new ArrayList<ResponseMessage>();
		}
		responseMessageList.add(message);

		return responseMessageList;
	}


	/**
	 * 
	 * @param responseMessageList
	 * @param locale
	 * @param key
	 * @return
	 */
	public List<ResponseMessage> buildResponseMessage(List<ResponseMessage> responseMessageList, Locale locale, String key, Object... parameters ) {

		return buildAlphaXResponseEntity(responseMessageList, messageService.createApiMessage(locale, key, parameters));
	}
	
	
	/**
	 * This method is used to format dd/MM/YYYY date to DDMMYY
	 * @param entityDate
	 * @return
	 */
	public String getDateInDDMMYY(String date) {

		if (date != null && !date.isEmpty()) {
			StringBuilder dateBuilder = new StringBuilder();
			String dateValues[] = date.split("/");
			dateBuilder.append(StringUtils.leftPad(dateValues[0], 2, "0")).append(StringUtils.leftPad(dateValues[1], 2, "0"));
			dateBuilder.append(StringUtils.right(dateValues[2], 2));
			date = dateBuilder.toString();
		} 
		return date;
	}
	
	
	/**
	 * This method is used to format dd/MM/YYYY date to DDMMYYYY
	 * @param entityDate
	 * @return
	 */
	public String getDateInDDMMYYYY(String date) {
		
		if (date != null && !date.isEmpty()) {
			StringBuilder dateBuilder = new StringBuilder();
			String dateValues[] = date.split("/");
			dateBuilder.append(StringUtils.leftPad(dateValues[0], 2, "0")).append(StringUtils.leftPad(dateValues[1], 2, "0"));
			dateBuilder.append(dateValues[2]);
			date = dateBuilder.toString();
		} 		
		return date;
	}
	
	
	/**
	 * This method is used to format dd/MM/YYYY date to YYYYMMDD
	 * @param entityDate
	 * @return
	 */
	public String getDateInYYYYMMDD(String date) {
		
		if (date != null && !date.isEmpty()) {
			StringBuilder dateBuilder = new StringBuilder();
			String dateValues[] = date.split("/");
			dateBuilder.append(dateValues[2]).append(StringUtils.leftPad(dateValues[1], 2, "0")).append(StringUtils.leftPad(dateValues[0], 2, "0"));
			date = dateBuilder.toString();
		} 		
		return date;
	}
	
	
	/**
	 * This method is used to format dd/MM/YYYY date to YYYYMM
	 * @param entityDate
	 * @return
	 */
	public String getDateInYYYYMM(String date) {
		
		if (date != null && !date.isEmpty()) {
			StringBuilder dateBuilder = new StringBuilder();
			String dateValues[] = date.split("/");
			dateBuilder.append(dateValues[2]).append(StringUtils.leftPad(dateValues[1], 2, "0"));
			date = dateBuilder.toString();
		} 		
		return date;
	}
	
	
	/**
	 * This method is used to check date format should be  dd/MM/YYYY
	 * @param entityDate
	 * @return
	 */
	public boolean checkDateFormat(String date , String format) {
		SimpleDateFormat sdfrmt = new SimpleDateFormat(format);
		sdfrmt.setLenient(false);
		boolean flag = true;
		try {
			sdfrmt.parse(date);
		} catch (Exception e) {
			log.info("Invalid Date format :"+date);
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.INVALID_DATE_FORMAT_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_DATE_FORMAT_MSG_KEY), exception);
			flag =  false;
			throw exception;
			
		}
		/* Return true if date format is valid */
		return flag;

	}
	
	
	/**
	 * This method is used to convert date string dMMYYYY and ddMMYYYY  to dd/MM/YYYY
	 * @param entityDate
	 * @return
	 */
	public String convertDateToString(String entityDate) {
	
		StringBuilder dateBuilder = new StringBuilder();
		
		if(entityDate.length() == 7) {
			dateBuilder.append("0").append(entityDate.substring(0, 1)).append("/");
			dateBuilder.append(entityDate.substring(1, 3)).append("/").append(entityDate.substring(3, 7));
			
		}
		if(entityDate.length() == 8) {
			dateBuilder.append(entityDate.substring(0, 2)).append("/");
			dateBuilder.append(entityDate.substring(2, 4)).append("/").append(entityDate.substring(4, 8));
		}
		
		return dateBuilder.toString();
	}
	
	/**
	 * This method is used to format dd/MM/YYYY date to YYMMDD
	 * @param entityDate
	 * @return
	 */
	public String getDateInYYMMDD(String date) {
		
		if (date != null && !date.isEmpty() && date.length()==10) {
			StringBuilder dateBuilder = new StringBuilder();
			String dateValues[] = date.split("/");
			dateBuilder.append(dateValues[2].substring(2, 4)).append(StringUtils.leftPad(dateValues[1], 2, "0")).append(StringUtils.leftPad(dateValues[0], 2, "0"));
			date = dateBuilder.toString();
		} 		
		return date;
	}
	
	/**
	 * This method is used to format DD.MM.YY date to DD.MM.YYYY
	 * @param entityDate
	 * @return
	 */
	public String convertDateInDDMMYYYY(String date) {
		
		if (date != null && !date.isEmpty()) {
			StringBuilder dateBuilder = new StringBuilder();
			String dateValues[] = date.split("\\.");
			dateBuilder.append(StringUtils.leftPad(dateValues[0],2,"0")).append("/").append(StringUtils.leftPad(dateValues[1], 2, "0")).append("/").append(StringUtils.leftPad(dateValues[2], 4, "20"));
			date = dateBuilder.toString();
		} 		
		return date;
	}
	

}