package com.alphax.common.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.common.constants.RestInputConstants;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.common.response.ResponseMessage;
import com.alphax.common.rest.message.Message;
import com.alphax.common.rest.message.service.MessageService;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@ControllerAdvice
@Slf4j
public class AlphaXRestExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	AlphaXCommonUtils utils;
	
	@Autowired
	private MessageService messageService;

	/**
	 * @param exc
	 * @param request
	 * @return ResponseEntity according to the exception type.
	 */
	@ExceptionHandler(value = { AlphaXException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException exc, WebRequest request) {

		AlphaXException exception = (AlphaXException) exc;

		switch (exception.getType()) {
		case AlphaXException.ENTITY_CAN_NOT_BE_ADDED:
			return handleApiException(exception, request, HttpStatus.BAD_REQUEST);
		case AlphaXException.ENTITY_DOES_NOT_EXIST:
			return handleApiException(exception, request, HttpStatus.NOT_FOUND);
		case AlphaXException.ENTITY_CAN_NOT_BE_DELETED:
			return handleApiException(exception, request, HttpStatus.BAD_REQUEST);
		case AlphaXException.ENTITY_CAN_NOT_BE_UPDATED:
			return handleApiException(exception, request, HttpStatus.BAD_REQUEST);
		case AlphaXException.ENTITY_CAN_NOT_BE_RETRIVED:
			return handleApiException(exception, request, HttpStatus.NOT_FOUND);
		case AlphaXException.ENTITY_LIST_CAN_NOT_BE_RETRIVED:
			return handleApiException(exception, request, HttpStatus.NOT_FOUND);
		case AlphaXException.LICENCE_UNAVAILABLE:
			return handleApiException(exception, request, HttpStatus.OK);
		case AlphaXException.COBOL_CALL_TIMEOUT:
			return handleApiException(exception, request, HttpStatus.GATEWAY_TIMEOUT);
		case AlphaXException.DAILY_CLOSING_PROCESS:
			return handleApiException(exception, request, HttpStatus.EXPECTATION_FAILED);
		case AlphaXException.INSTALLATIO_UPDATE_PROCESS:
			return handleApiException(exception, request, HttpStatus.EXPECTATION_FAILED);
		case AlphaXException.END_OF_YEAR_PROCESS:
			return handleApiException(exception, request, HttpStatus.EXPECTATION_FAILED);
		case AlphaXException.MAINTENANCE_WORK_PROCESS:
			return handleApiException(exception, request, HttpStatus.EXPECTATION_FAILED);
		default:
			return handleApiException(exception, request, HttpStatus.BAD_REQUEST);
		}

	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<AlphaXResponse> handleAllExceptions(Exception ex, WebRequest request) {

		AlphaXResponse<Object> response  = new AlphaXResponse<Object>();
		
		response.setResponseMessages(utils.buildResponseMessage(response.getResponseMessages(), LocaleContextHolder.getLocale(), "com.alphax.controller.exception"));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	private ResponseEntity<Object> handleApiException(AlphaXException exception, WebRequest request, HttpStatus httpStatus) {

		AlphaXResponse<Object> response  = new AlphaXResponse<Object>();
		response.setResponseMessages( utils.buildAlphaXResponseEntity(response.getResponseMessages(), exception.getMessage(), exception.getType(), exception.getMessageType(), exception.getMessageLocale())); 
		return new ResponseEntity<>(response, httpStatus);
	}


	@SuppressWarnings("rawtypes")
	@ExceptionHandler(AS400Exception.class)
	public final ResponseEntity<AlphaXResponse> handleAS400Exceptions(AS400Message[] msgList) {

		AlphaXResponse<Object> response  = new AlphaXResponse<Object>();

		for (AS400Message message : msgList) {
			log.info("Error Message for messageId: {} - {}", message.getID(), message.getText());
			response.setResponseMessages(utils.buildAlphaXResponseEntity(response.getResponseMessages(), message.getText(), 
					message.getID(), RestInputConstants.MSG_TYPE_ERROR, LocaleContextHolder.getLocale().getLanguage().toUpperCase()));
		}

		return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		AlphaXResponse<Object> response  = new AlphaXResponse<Object>();
		final List<ResponseMessage> errorMessages = new ArrayList<>();
		
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {

			ResponseMessage responseMessage =  new ResponseMessage();
			
			Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, fe.getDefaultMessage(), fe.getField());

			responseMessage.setServerMsg(message.getMessageContent());
			responseMessage.setServerMsgId(message.getMessageNumber());
			responseMessage.setServerMsgLangCode(message.getMessageLocale());
			responseMessage.setServerMsgType(message.getMessageType().toString());
			errorMessages.add(responseMessage);
		} 
	    
	    response.setResponseMessages( errorMessages);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    
	}
	
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		
		AlphaXResponse<Object> response  = new AlphaXResponse<Object>();
		final List<ResponseMessage> errorMessages = new ArrayList<>();
	    
	    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
	    	
	    	ResponseMessage responseMessage =  new ResponseMessage();
			
			Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CONSTRAINT_VIOLATION_MSG_KEY, violation.getPropertyPath(), violation.getMessage() );
			
			responseMessage.setServerMsg(message.getMessageContent());
			responseMessage.setServerMsgId(message.getMessageNumber());
			responseMessage.setServerMsgLangCode(message.getMessageLocale());
			responseMessage.setServerMsgType(message.getMessageType().toString());
			errorMessages.add(responseMessage);
			
	        //errors.add(violation.getRootBeanClass().getName() + " " + 
	         // violation.getPropertyPath() + ": " + violation.getMessage());
	    }
	    response.setResponseMessages( errorMessages);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
