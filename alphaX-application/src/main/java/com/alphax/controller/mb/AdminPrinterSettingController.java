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
import com.alphax.service.mb.AdminPrinterSettingService;
import com.alphax.api.mb.AdminPrinterSettingApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.AdminPrinterDTO;
import com.alphax.vo.mb.DropdownObject;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class AdminPrinterSettingController implements AdminPrinterSettingApi {

	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	AdminPrinterSettingService printerService;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This method is used to create new Printer Details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createNewPrinter(	@Valid @RequestBody AdminPrinterDTO adminPrinter_obj ) {
		
		log.info("AdminPrinterSettingsController : Start createNewPrinter");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminPrinterDTO createPrinter = printerService.createNewPrinter(adminPrinter_obj, decryptUtil.getSchema(), decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createPrinter);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

	/**
	 * This method is used to return the list Printer List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPrinterList() {
		
		log.info("AdminPrinterSettingsController : Start getPrinterList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
	
		List<DropdownObject> printerList = printerService.getPrinterList(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(printerList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		
	}


	/**
	 * This method is used for delete printer Details from DB
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deletePrinterDetails(@PathVariable(value = "id", required = true) String printerId ) {
		
		log.info("AdminPrinterSettingsController : Start deletePrinterDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> printer_obj = printerService.deletePrinterDetails(decryptUtil.getSchema(), printerId, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(printer_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for update agency and printer mapping
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> updateAgencyPrinterMapping(
			@Valid @RequestBody List<AdminPrinterDTO> admin_PrinterMappingDTO,
			@NotBlank @RequestParam(value = "agencyId", required = true) String agencyId) {	 
		
		log.info("AdminPrinterSettingsController : Start updateAgencyPrinterMapping");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedMapping = printerService.updateAgencyPrinterMapping(admin_PrinterMappingDTO, decryptUtil.getSchema(), 
				agencyId, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedMapping);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get Printers list that are not assigned to current Agency.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPrinters_NotAssignedToCrntAgency(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String agencyId) {
		
		log.info("AdminPrinterSettingsController : Start getPrinters_NotAssignedToCrntAgency");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AdminPrinterDTO> notAssignePrinterList = printerService.getPrinters_NotAssignedToCrntAgency( decryptUtil.getSchema(), agencyId );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(notAssignePrinterList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get Printers list that are assigned to current Agency.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPrinters_AssignedToCrntAgency(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String agencyId) {
		
		log.info("AdminPrinterSettingsController : Start getPrinters_AssignedToCrntAgency");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AdminPrinterDTO> assignedPrinterList = printerService.getPrinters_AssignedToCrntAgency( decryptUtil.getSchema(), agencyId );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(assignedPrinterList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**
	 * This method is used for Return the printer Details from DB
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPrinterDetails(@PathVariable(value = "id", required = true) String printerId) {
		// TODO Auto-generated method stub
		log.info("AdminPrinterSettingsController : Start getPrinterDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		AdminPrinterDTO printerDetails = printerService.getPrinterDetails(decryptUtil.getSchema(),decryptUtil.getDataLibrary() ,printerId);
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(printerDetails);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	/**
	 * This method is used for Update Printer Details Object in DB  
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateAdminPrinterDetails(
			@Valid AdminPrinterDTO adminPrinterDetails) {
		// TODO Auto-generated method stub
		log.info("AdminPrinterSettingsController : Start updateAdminPrinterDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		  Map<String, Boolean>  updatePrinterDetails  =  printerService.updateAdminPrinterDetails(adminPrinterDetails, 
				decryptUtil.getSchema(), decryptUtil.getDataLibrary(),decryptUtil.getLogedInUser());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatePrinterDetails);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Printer Type List Object in DB  
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPrinterTypeList() {
		log.info("AdminPrinterSettingsController : Start getPrinterTypeList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
	
		List<DropdownObject> printerTypeList = printerService.getPrinterTypeList(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(printerTypeList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	

	
	
}