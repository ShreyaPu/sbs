package com.alphax.controller.mb;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.AdminOneapiService;
import com.alphax.api.mb.AdminOneapiApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.AdminApiurlDTO;
import com.alphax.vo.mb.AdminOneapiDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class AdminOneapiController implements AdminOneapiApi {
	
	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	AdminOneapiService oneApi;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This method is used for get OneApi customer details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getOneapiCustomerDetails(
			@NotBlank @Pattern(regexp = "[0-9]+")  @PathVariable(value = "customerId", required = true) String customerId,
			@NotBlank @RequestParam(value = "apiKey", required = true) String apiKey) {

		log.info("OneApiController : Start getOneApiCustomerDetails ");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		AdminOneapiDTO oneapiCustomerDetails = oneApi.getOneapiCustomerDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), customerId, apiKey);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(oneapiCustomerDetails);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}
	
	
	/**
	 * This method is used for get OneApi List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getOneapiList(
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "isEVOBus", required = true) Boolean isEVOBus) {

		log.info("OneApiController : Start getOneapiList ");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		GlobalSearch oneapiList = oneApi.getOneapiList(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), pageSize, pageNumber, isEVOBus );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(oneapiList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}
	
	
	/**
	 * This method is used to create new OneApi detail in DB - O_ONEAPI 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createNewOneapi(@Valid @RequestBody AdminOneapiDTO oneapiDTO_obj) {
		
		log.info("OneApiController : Start createNewOneapi ");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		AdminOneapiDTO adminOneapiDTO = oneApi.createOneapi(oneapiDTO_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getLogedInUser(), decryptUtil.getMandent());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(adminOneapiDTO);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}
	

	/**
	 * This method is used to get VF-Number (Customer Number) from DB  
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getVFNumber(
			@RequestParam(value = "isEVOBus", required = true) Boolean isEVOBus) {	
		
		log.info("OneApiController : Start getVFNumber ");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		List<DropdownObject> vfNumberList  = oneApi.getVFNumber(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), isEVOBus );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(vfNumberList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}


	/**
	 * This method is used to delete the Customer (VF-Number) from DB
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteOneapiCustomerDetails(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "customerId", required = true) String customerId ) {
		
		log.info("OneApiController : Start deleteOneapiCustomerDetails ");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Map<String, Boolean> deleteFlag  = oneApi.deleteOneapiCustomerDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), customerId );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(deleteFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}
	
	
	/**
	 * This method is used to delete the Configuration details by VF-Number and API key from DB
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteOneapiConfigurationDetails(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "customerId", required = true) String customerId,
			@NotBlank @RequestParam(value = "apiKey", required = true) String apiKey) {
		
		log.info("OneApiController : Start deleteOneapiConfigurationDetails ");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Map<String, Boolean> deleteFlag  = oneApi.deleteOneapiConfigurationDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), customerId, apiKey);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(deleteFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}
	

	/**
	 * This method is used for get ApiURL list
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getApiurl(
			@RequestParam(value = "isEVOBus", required = true) Boolean isEVOBus) {

		log.info("OneApiController : Start getApiurl ");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		List<AdminApiurlDTO> apiurlList = oneApi.getApiurlList(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), isEVOBus);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(apiurlList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}
	
	
	/**
	 * This method is used to update OneApi detail in DB - O_ONEAPI 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateOneapiDetails(@Valid @RequestBody AdminOneapiDTO oneapiDTO_obj) {
		
		log.info("OneApiController : Start updateOneapiDetails ");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		AdminOneapiDTO adminOneapiDTO = oneApi.updateOneapi(oneapiDTO_obj, decryptUtil.getSchema(),	decryptUtil.getDataLibrary(), 
				decryptUtil.getLogedInUser() );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(adminOneapiDTO);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}
	
	
	/**
	 * This method is used to create new EVOBUS detail in DB - O_ONEAPI
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createEVOBusApi(@Valid @RequestBody AdminOneapiDTO oneapiDTO_obj) {
		
		log.info("OneApiController : Start createEVOBusApi ");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		AdminOneapiDTO adminOneapiDTO = oneApi.createEVOBusApi(oneapiDTO_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getLogedInUser(), decryptUtil.getMandent());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(adminOneapiDTO);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}

	
}