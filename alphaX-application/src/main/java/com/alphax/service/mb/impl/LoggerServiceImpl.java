package com.alphax.service.mb.impl;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.service.mb.LoggerService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoggerServiceImpl extends BaseService implements LoggerService {

	@Autowired
	private MessageService messageService;

	@Value("${logging.file.name}")
	private String logFileName;
	
	
	/**
	 * This is method is used to get Log Detail for application.
	 */
	@Override
	public String getLogsInformation(String dataCenterLibValue) {

		log.info("Inside getLogsInformation method of LoggerServiceImpl");
		
		String logs = "";

		try {
			if(dataCenterLibValue != null && dataCenterLibValue.equals("0")) {
				Path logFile = Paths.get(logFileName);
		        logs = new String(Files.readAllBytes(logFile));
			}
			else {
				log.info("Error Message: {}", "logfile can be only retrieved for local installations." );
				AlphaXException exception = new AlphaXException(messageService.createApiMessage( LocaleContextHolder.getLocale(), ExceptionMessages.LOGFILE_INVALID_ACCESS));
				log.error(messageService.getReadableMessage(ExceptionMessages.LOGFILE_INVALID_ACCESS));
				throw exception;
			}				
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Logs"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Logs"), exception);
			throw exception;
		}

		return logs;
	}
	
	
	/**
	 * This is method is used to download the Log file of application.
	 */
	@Override
	public ByteArrayInputStream downloadLogFile(String dataCenterLibValue) {

		log.info("Inside downloadLogFile method of LoggerServiceImpl");
		
		ByteArrayInputStream  byteArrayInputStream = null;

		try {
			if(dataCenterLibValue != null && dataCenterLibValue.equals("0")) {
				
				Path logFile = Paths.get(logFileName);
				
				// file exists and it is not a directory
				if(Files.exists(logFile) && !Files.isDirectory(logFile)) {
					
					byte[] logs = Files.readAllBytes(logFile);
					
					byteArrayInputStream = new ByteArrayInputStream(logs);			 
				}		       
			}
			else {
				log.info("Error Message: {}", "logfile can be only retrieved for local installations." );
				AlphaXException exception = new AlphaXException(messageService.createApiMessage( LocaleContextHolder.getLocale(), ExceptionMessages.LOGFILE_INVALID_ACCESS));
				log.error(messageService.getReadableMessage(ExceptionMessages.LOGFILE_INVALID_ACCESS));
				throw exception;
			}
				
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Download Logs"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Download Logs"), exception);
			throw exception;
		}

		return byteArrayInputStream;
	}

	
}