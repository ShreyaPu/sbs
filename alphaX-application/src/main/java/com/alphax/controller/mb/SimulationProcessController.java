package com.alphax.controller.mb;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.SimulationProcessService;
import com.alphax.api.mb.SimulationProcessApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.util.RestURIConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class SimulationProcessController implements SimulationProcessApi {

	@Autowired
	SimulationProcessService simulationService;

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	DecryptTokenUtils decryptUtil;
	

	/**
	 * This method is used to Create the random list of delivery Notes using COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> generateDeliveryNotes() {

		log.info("SimulationProcessController : Start generateDeliveryNotes");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> generateDelivryNotesReceipt = simulationService.generateDeliveryNotes( decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId() );

		if (generateDelivryNotesReceipt != null && !generateDelivryNotesReceipt.isEmpty()) {

			if (generateDelivryNotesReceipt.get("returnCode").equalsIgnoreCase("00000")) {
				
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, 
						ExceptionMessages.SIMULATION_PROCESS_SUCCESS_MSG_KEY, generateDelivryNotesReceipt.get("insertCount")));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						generateDelivryNotesReceipt.get("returnMsg"), generateDelivryNotesReceipt.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SIMULATION_PROCESS_FAILED_MSG_KEY));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}


	/**
	 * This method is used to Trigger a event to automatically process the delivery notes to BSN and ET using COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> autoProcessDiveryNotes() {

		log.info("SimulationProcessController : Start autoProcessDiveryNotes");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> autoProcessReceipt = simulationService.autoProcessDiveryNotes( decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId() );

		if (autoProcessReceipt != null && !autoProcessReceipt.isEmpty()) {

			Map.Entry<String,String> entry = autoProcessReceipt.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.AUTO_PROCESS_FAILED_MSG_KEY));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	
	/**
	 * This method is used to Create the random list of Conflict delivery Notes using COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> generateConflictDeliveryNotes() {

		log.info("SimulationProcessController : Start generateConflictDeliveryNotes");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> generateConflictDelivryReceipt = simulationService.generateConflictDeliveryNotes( decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId() );

		if (generateConflictDelivryReceipt != null && !generateConflictDelivryReceipt.isEmpty()) {

			if (generateConflictDelivryReceipt.get("returnCode").equalsIgnoreCase("00000")) {
				
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, 
						ExceptionMessages.SIMULATION_PROCESS_SUCCESS_MSG_KEY, generateConflictDelivryReceipt.get("insertCount")));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						generateConflictDelivryReceipt.get("returnMsg"), generateConflictDelivryReceipt.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SIMULATION_CONFLICT_PROCESS_FAILED_MSG_KEY));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	
}