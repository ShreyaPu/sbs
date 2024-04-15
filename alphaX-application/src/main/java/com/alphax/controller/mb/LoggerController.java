/**
 * 
 */
package com.alphax.controller.mb;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.LoggerService;
import com.alphax.api.mb.LoggerApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class LoggerController implements LoggerApi {

	@Autowired
	LoggerService loggerService;

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	DecryptTokenUtils decryptUtil;


	/**
	 * This is LoggerController method used to get the logger details of alphaX.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getLogsInformation() {

		log.info("LoggerController : Start getLogsInformation");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String logs  = loggerService.getLogsInformation(decryptUtil.getAX_DataCenter_Lib());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(logs);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is LoggerController method used to download the logger file of alphaX.
	 */
	@Override
	public <T> ResponseEntity downloadLogFile() {

		log.info("LoggerController : Start downloadLogFile");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String fileName = "AlphaxServices.txt";

		ByteArrayInputStream resource = loggerService.downloadLogFile(decryptUtil.getAX_DataCenter_Lib());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);		

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/octet-stream; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}
	}


}