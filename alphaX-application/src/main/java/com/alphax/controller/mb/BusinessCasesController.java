package com.alphax.controller.mb;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.BusinessCases;
import com.alphax.api.mb.BusinessCasesApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AccessesBA_DTO;
import com.alphax.vo.mb.BusinessCases25DTO;
import com.alphax.vo.mb.BusinessCases49DTO;
import com.alphax.vo.mb.BusinessCasesDTO;
import com.alphax.vo.mb.DeliveryNotes_BA_DTO;
import com.alphax.vo.mb.DeparturesBA_DTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.FinalizationsBA_DTO;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.LagerDetailsDTO;
import com.alphax.vo.mb.MasterDataBA_DTO;
import com.alphax.vo.mb.RebookingBundlesBA_DTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class BusinessCasesController implements BusinessCasesApi  {

	@Autowired
	BusinessCases businessCases;

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	DecryptTokenUtils decryptUtil;


	/**
	 * This method is used for update Finalizations BA (Finalisierungen )
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateFinalizationsBA( 
			@Valid @RequestBody FinalizationsBA_DTO finalizationsBA_obj) {		

		log.info("BusinessCasesController : Start updateFinalizationsBA");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> finalizationsBA = businessCases.updateFinalizationsBA(finalizationsBA_obj, decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		if (finalizationsBA != null && !finalizationsBA.isEmpty()) {

			Map.Entry<String,String> entry = finalizationsBA.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}else {
				alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, 
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Accesses BA (Zugänge BA)"));
		}

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for update Accesses BA (Zugänge )
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateAccessesBA(
			@Valid @RequestBody AccessesBA_DTO accessesBA_obj ) {

		log.info("BusinessCasesController : Start updateAccessesBA");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> accessesBA = businessCases.updateAccessesBA(accessesBA_obj, decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		if (accessesBA != null && !accessesBA.isEmpty()) {

			Map.Entry<String,String> entry = accessesBA.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}else {
				alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, 
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Accesses BA (Zugänge BA)"));
		}

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for update Departures BA (Abgänge) Business Cases:-[20/22/25/09]
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateDeparturesBA( 
			@Valid @RequestBody DeparturesBA_DTO  departuresBA_Obj ) {		

		log.info("BusinessCasesController : Start updateDeparturesBA");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> finalizationsBA = businessCases.updateDeparturesBA(departuresBA_Obj, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		if (finalizationsBA != null && !finalizationsBA.isEmpty()) {

			if (finalizationsBA.get("returnCode").equalsIgnoreCase("00000")) {
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}else {
				alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
						finalizationsBA.get("returnMsg"), finalizationsBA.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, 
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Departures BA (Abgänge BA)"));
		}

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for update MasterData BA (Stammdaten) Business Cases:-[40/44/45/49]
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateMasterDataBA( 
			@Valid @RequestBody MasterDataBA_DTO  masterDataBA_Obj ) {		

		log.info("BusinessCasesController : Start updateMasterDataBA");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> masterDataBA = businessCases.updateMasterDataBA(masterDataBA_Obj, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		if (masterDataBA != null && !masterDataBA.isEmpty()) {

			if (masterDataBA.get("returnCode").equalsIgnoreCase("00000")) {
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}else {
				alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
						masterDataBA.get("returnMsg"), masterDataBA.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, 
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "MasterData BA (Stammdaten BA)"));
		}

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Finalizations BA 50 and 67 details (Finalisierungen )
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getFinalizationsBADetails( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@NotBlank @RequestParam(value = "businessCase", required = true) String businessCase) {		

		log.info("BusinessCasesController : Start getFinalizationsBADetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		FinalizationsBA_DTO finalizationsBA = businessCases.getFinalizationsBADetails(decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), manufacturer, warehouseNo, partNumber, businessCase);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(finalizationsBA);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get getAccessesBADetails BA 01,02,05,17 details (Finalisierungen )
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAccessesBADetails( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@NotBlank @RequestParam(value = "businessCase", required = true) String businessCase) {		

		log.info("BusinessCasesController : Start getAccessesBADetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AccessesBA_DTO accessesBA_obj = businessCases.getAccessesBADetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), manufacturer, warehouseNo, partNumber, businessCase);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(accessesBA_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get delivery note list
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDeliveryNoteList( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "supplierNumber", required = true) String supplierNumber,
			@NotBlank @RequestParam(value = "searchString", required = true) String searchString,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {

		log.info("BusinessCasesController : Start getDeliveryNoteList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch deliveryNoteList = businessCases.getDeliveryNoteList(decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), manufacturer, warehouseNo, supplierNumber, searchString, pageSize, pageNumber );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(deliveryNoteList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is BusinessCasesController method used to get the Lager (Warehouse) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getLagerList() {

		log.info("BusinessCasesController : Start getLagerList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<LagerDetailsDTO> lagerList = businessCases.getLagerList(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				allowedApWarehouses,decryptUtil.getSchema());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(lagerList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is BusinessCasesController method used to get the Lieferant (Suppliers) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSupplierList() {

		log.info("BusinessCasesController : Start getSupplierList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch supplierList = businessCases.getSupplierList(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(supplierList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get getAccessesBA06Details BA 06 details (Finalisierungen )
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAccessesBA06Details( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@NotBlank @RequestParam(value = "businessCase", required = true) String businessCase,
			@NotBlank @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo){		

		log.info("BusinessCasesController : Start getAccessesBA06Details");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AccessesBA_DTO accessesBA_obj = businessCases.getAccessesBA06Details(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), manufacturer, warehouseNo, partNumber, businessCase, supplierNo, deliveryNoteNo);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(accessesBA_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Departures BA details (Abgänge BA)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDeparturesBADetails( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@NotBlank @RequestParam(value = "businessCase", required = true) String businessCase) {		

		log.info("BusinessCasesController : Start getDeparturesBADetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		DeparturesBA_DTO departuresBA_obj = businessCases.getDeparturesBADetails(decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), manufacturer, warehouseNo, partNumber, businessCase);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(departuresBA_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get MasterData BA details (Stammdaten BA)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getMasterDataBADetails( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@NotBlank @RequestParam(value = "businessCase", required = true) String businessCase) {		

		log.info("BusinessCasesController : Start getMasterDataBADetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		MasterDataBA_DTO masterDataBA_obj = businessCases.getMasterDataBADetails(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), manufacturer, warehouseNo, partNumber, businessCase);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(masterDataBA_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Departures BA 25 details (Abgänge BA 25)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDeparturesBA25Details( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@NotBlank @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@NotBlank @RequestParam(value = "processCode", required = true) String processCode,
			@NotBlank @RequestParam(value = "businessCase", required = true) String businessCase) {		

		log.info("BusinessCasesController : Start getDeparturesBA25Details");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		DeparturesBA_DTO departuresBA25_obj = businessCases.getDeparturesBA25Details(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), manufacturer, warehouseNo, partNumber, supplierNo, deliveryNoteNo, processCode, businessCase);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(departuresBA25_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Taxes (VAT Registration Number)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getVATRegistrationNumber(){		

		log.info("BusinessCasesController : Start getVATRegistrationNumber");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> taxesList = businessCases.getVATRegistrationNumber(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(taxesList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Abnehmergruppe (Customer Group) List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCustomerGroupList(
			@NotBlank @RequestParam(value = "businessCase", required = true) String businessCase) {

		log.info("BusinessCasesController : Start getCustomerGroupList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> customerGroupList = businessCases.getCustomerGroupList(businessCase);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(customerGroupList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to update Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA) using COBOL Program.
	 */

	@Override
	public ResponseEntity<AlphaXResponse> updateRebookingBundlesBA( 
			@Valid @RequestBody RebookingBundlesBA_DTO  rebookingBundlesBA_DTO ) {		

		log.info("BusinessCasesController : Start updateRebookingBundlesBA");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> rebookingBA = businessCases.updateRebookingBundlesBA(rebookingBundlesBA_DTO, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		if (rebookingBA != null && !rebookingBA.isEmpty()) {

			Map.Entry<String,String> entry = rebookingBA.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}else {
				alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, 
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA)"));
		}

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA).
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getRebookingBundleDetails( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber) {		

		log.info("BusinessCasesController : Start getRebookingBundleDetails()");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		DeparturesBA_DTO departuresBA25_obj = businessCases.getRebookingBundleDetails(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), manufacturer, warehouseNo, partNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(departuresBA25_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is BusinessCasesController method used to get the Lieferant (Suppliers) detail List for DDL based on search
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSupplierListUsingSearchString(		
			@NotBlank @RequestParam(value = "searchString", required = true) String searchString ) {

		log.info("BusinessCasesController : Start getSupplierList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch supplierList = businessCases.getSupplierListUsingSearchString(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), searchString);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(supplierList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**
	 * This is BusinessCasesController method used to get the list of active BAs from table O_BA
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getActiveBA() {

		log.info("BusinessCasesController : Start getActiveBA");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<BusinessCasesDTO> baList = businessCases.getActiveBA(decryptUtil.getSchema());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(baList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is BusinessCasesController method used to get the default printer details for selected warehouse from DB table.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDefaultPrinter(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotNull @RequestParam(value = "flag", required = true) Integer flag) {

		log.info("BusinessCasesController : Start getDefaultPrinter");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, String> printerDetails = businessCases.getDefaultPrinter(decryptUtil.getDataLibrary(), warehouseNo, flag);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(printerDetails);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This is BusinessCasesController method used to get the company related Lager (Warehouse) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getComapnyBasedLagerList() {

		log.info("BusinessCasesController : Start getComapnyBasedLagerList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<LagerDetailsDTO> lagerList = businessCases.getComapnyBasedLagerList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), decryptUtil.getAxCompanyId(),
				decryptUtil.getCompanyApWarehouse());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(lagerList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for update delivery note status
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateDeliveryNoteStatus( 
			@Valid @RequestBody DeliveryNotes_BA_DTO deliveryNotes_BA_DTO) {		

		log.info("BusinessCasesController : Start updateDeliveryNoteStatus");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, Boolean> updateFlag = businessCases.updateDeliveryNoteStatus(deliveryNotes_BA_DTO, decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updateFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to call BA 49
	 */
	@Override
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA49( 
			@Valid @RequestBody BusinessCases49DTO cases49_obj) {		

		log.info("BusinessCasesController : Start newJavaImplementation_BA49");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, String> ba49_response = businessCases.newJavaImplementation_BA49(cases49_obj, decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser() );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(ba49_response);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method is used to call BA 25
	 */
	@Override
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA25( 
			@Valid @RequestBody BusinessCases25DTO cases25_obj) {		

		log.info("BusinessCasesController : Start newJavaImplementation_BA25");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> ba49_response = businessCases.newJavaImplementation_BA25(cases25_obj, decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser(), null);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(ba49_response);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}
	
	
	/**
	 * This method is used to call BA 06 using Java implementation.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA06( @Valid @RequestBody BusinessCases25DTO cases25_obj) {		

		log.info("BusinessCasesController : Start newJavaImplementation_BA06");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, String> ba06_response = businessCases.newJavaImplementation_BA06(cases25_obj, decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser(),null );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(ba06_response);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}
	
	
	/**
	 * This method is used to call BA 50 using Java implementation.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA50( @Valid @RequestBody FinalizationsBA_DTO  ba50_obj) {		

		log.info("BusinessCasesController : Start newJavaImplementation_BA50");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> ba50_response = businessCases.newJavaImplementation_BA50(ba50_obj, decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser() );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(ba50_response);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}

	/**
	 * This method is used to call BA 17
	 */
	@Override
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA17( 
			@Valid @RequestBody AccessesBA_DTO cases17_obj) {		

		log.info("BusinessCasesController : Start newJavaImplementation_BA25");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> ba49_response = businessCases.newJavaImplementation_BA17(cases17_obj, decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(ba49_response);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to Clean up the Parts before specified period using BA 50.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> partsCleanupUsingBA50( 
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate) {

		log.info("BusinessCasesController : Start partsCleanupUsingBA50");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> cleanUpResponse = businessCases.partsCleanupUsingBA50(warehouseNo, toDate, decryptUtil.getDataLibrary(), 
				decryptUtil.getSchema(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser() );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(cleanUpResponse);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	

	/**
	 * This method is used for get BA 17 details 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDetailsFor_BA17( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber) {		

		log.info("BusinessCasesController : Start getDetailsFor_BA17");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AccessesBA_DTO accessesBA_obj = businessCases.getDetailsFor_BA17(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				 manufacturer, partNumber,decryptUtil.getAllowedApWarehouse() );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(accessesBA_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method is used to create part in multiple warehouse which is not available in ETSTAMM 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createPartInMultipleWarehouses( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@NotBlank @RequestParam(value = "warehouseNumbers", required = true) String warehouseNumbers,
			@NotBlank @RequestParam(value = "vatRegistrationNumber", required = true) String vatRegistrationNumber) {		

		log.info("BusinessCasesController : Start createPartInMultipleWarehouses");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> ba49_response = businessCases.createPartInMultipleWarehouses(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				partNumber, manufacturer, warehouseNumbers, vatRegistrationNumber,decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(),
				decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(ba49_response);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

			/**
		* this method is used for BA, BSN 475
		*/
		@Override
		public ResponseEntity<AlphaXResponse> newJavaImplementation_BSN475( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "marketingCode", required = true) String marketingCode,
			@RequestParam(value = "partNumber", required = false) String partNumber,
			@RequestParam(value = "discountGroup", required = false) String discountGroup)  {		
		
		log.info("BusinessCasesController : Start newJavaImplementation_BSN475");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		Map<String, String> bsn475_response = businessCases.newJavaImplementation_BSN475("",decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				manufacturer,marketingCode,"","");
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(bsn475_response);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}

	
}