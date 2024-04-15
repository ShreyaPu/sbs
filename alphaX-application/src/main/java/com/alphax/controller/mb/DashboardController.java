package com.alphax.controller.mb;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.DashboardService;
import com.alphax.api.mb.DashboardApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AlphaXConfigurationKeysDetailsDTO;
import com.alphax.vo.mb.AlphaXConfigurationSetupDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class DashboardController implements DashboardApi {
	
	@Autowired
	DashboardService dashboardService;
	
	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This method is used to get the counts of Wareneingang screen.
	 *
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getGoodsReceipt() {
		
		log.info("DashboardController : Start getGoodsReceipt");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		Map<String,String> counts = dashboardService.getGoodsReceipt(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), allowedApWarehouses);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(counts);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the counts of Daily Update - Warehouse Movement.
	 *
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDailyUpdateCount() {
		
		log.info("DashboardController : Start getDailyUpdateCount");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		Map<String,String> counts = dashboardService.getDailyUpdateCount(decryptUtil.getDataLibrary(), allowedApWarehouses);  
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(counts);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

	/**
	 * This method is used to get the alphaX setup configuration.
	 *
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAlphaXConfigurationDetails() {

		log.info("DashboardController : Start getAlphaXConfigurationDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<AlphaXConfigurationSetupDTO> alphaXConfigurationList  = dashboardService.getAlphaXConfigurationDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alphaXConfigurationList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to update the alphaX setup configuration.
	 *
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateAlphaXConfigurationDetails(
			@Valid @RequestBody List<AlphaXConfigurationSetupDTO> alphaXConfigurationSetupList ){

		log.info("DashboardController : Start updateAlphaXConfigurationDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<AlphaXConfigurationSetupDTO>  alphaXConfiguration  =  dashboardService.updateAlphaXConfigurationDetails(alphaXConfigurationSetupList, 
				decryptUtil.getSchema(), decryptUtil.getDataLibrary());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alphaXConfiguration);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to create O_setup table.
	 *
	 */
	@Override
	public ResponseEntity<AlphaXResponse> create_OSetupTable() {

		log.info("DashboardController : Start create_OSetupTable");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, String> createTable_Obj = dashboardService.create_OSetupTable(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		if (createTable_Obj != null && !createTable_Obj.isEmpty()) {
			Map.Entry<String,String> entry = createTable_Obj.entrySet().iterator().next();
			
			if (entry.getKey().equalsIgnoreCase("00000")) {
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.CREATE_FAILED_MSG_KEY," O_Setup Table"));
		}
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the Automatic delivery note processing details (Automatische Lieferscheinverarbeitung) from DB table O_SETUP
	 * @return List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAutomaticDeliveryNoteProcessingDetails(
			@NotBlank  @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber ) {

		log.info("DashboardController : Start getAutomaticDeliveryNoteProcessingDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<AlphaXConfigurationKeysDetailsDTO> alphaXConfigurationList  = dashboardService.getAutomaticDeliveryNoteProcessingDetails(decryptUtil.getSchema(),
				decryptUtil.getDataLibrary(), warehouseNumber, decryptUtil.getApCompanyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alphaXConfigurationList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to update  the Automatic delivery note processing details (Automatische Lieferscheinverarbeitung) into DB table O_SETUP
	 * @return List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateAutomaticDeliveryNoteProcessingDetails(
			@Valid @RequestBody List<AlphaXConfigurationKeysDetailsDTO> automaticProcessingObjList){

		log.info("DashboardController : Start updateAutomaticDeliveryNoteProcessingDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<AlphaXConfigurationKeysDetailsDTO> alphaXConfigurationList  = dashboardService.updateAutomaticDeliveryNoteProcessingDetails(automaticProcessingObjList, 
				decryptUtil.getSchema(), decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alphaXConfigurationList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}	
	
	
	@Override
	public ResponseEntity<AlphaXResponse> getInventoryOverviewCounts() {
		
		log.info("DashboardController : Start getInventoryOverviewCounts");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		Map<String,String> counts = dashboardService.getInventoryOverviewCounts(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), allowedApWarehouses);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(counts);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
}