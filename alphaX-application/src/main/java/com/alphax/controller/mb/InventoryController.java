package com.alphax.controller.mb;

import java.util.List;
import java.util.Locale;
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
import com.alphax.service.mb.InventoryService;
import com.alphax.api.mb.InventoryApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.ActivatedCountingListDTO;
import com.alphax.vo.mb.AddCountedPartDTO;
import com.alphax.vo.mb.CountingGroupDTO;
import com.alphax.vo.mb.CreateNewCountingListDTO;
import com.alphax.vo.mb.CreatedCountingDetailListDTO;
import com.alphax.vo.mb.CreatedCountingListDTO;
import com.alphax.vo.mb.CreatedCountingListValues;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.InventoryDTO;
import com.alphax.vo.mb.InventoryDiferentialListDTO;
import com.alphax.vo.mb.PartsBrandDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class InventoryController implements InventoryApi {

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	InventoryService inventoryService;

	@Autowired
	DecryptTokenUtils decryptUtil;


	/**
	 * This method is used to get inventory overview details 
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> getInventoryOverviewCounts(
			@NotBlank  @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber,
			@NotBlank  @RequestParam(value = "manufacturer", required = true) String manufacturer){

		log.info("InventoryController : Start getInventoryOverview");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		Map<String,String> counts = inventoryService.getInventoryOverviewCounts(decryptUtil.getDataLibrary(),decryptUtil.getSchema(),
				warehouseNumber, allowedApWarehouses, manufacturer);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(counts);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get display list for started inventory  
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDisplayListForStartedInventory() {

		log.info("InventoryController : Start getDisplayListForStartedInventory");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> mehrwertsteuerList = inventoryService.getDisplayListForStartedInventory();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(mehrwertsteuerList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is method used to start inventory for an existing counting list from COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> startInventoryForExistingCountingList(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo) {

		log.info("InventoryController : Start startInventoryForExistingCountingList");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> warehouseDetails = inventoryService.startInventoryForExistingCountingList( warehouseNo);

		if (warehouseDetails != null && !warehouseDetails.isEmpty()) {

			if (warehouseDetails.get("returnCode").equalsIgnoreCase("00000")) {

				//set the new order number in result set
				response.setResultSet(warehouseDetails);
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						warehouseDetails.get("returnMsg"), warehouseDetails.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}			
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.COBOL_PROGRAM_FAIL_MSG_KEY));
		}
		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This is method used to get warehouse list with status for inventory.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getWarehouseListWithStatus() {

		log.info("InventoryController : Start getWarehouseListWithStatus");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		List<DropdownObject> warehouseList = inventoryService.getWarehouseListWithStatus( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), allowedApWarehouses );

		// set the new order number in result set
		response.setResultSet(warehouseList);
		response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale,ExceptionMessages.SUCCESS_MESSAGE_KEY));

		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This is method used to Submit the Inventory count from COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> submitInventoryCount(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@NotBlank @RequestParam(value = "pageNo", required = true) String pageNo,
			@NotBlank @RequestParam(value = "countValue", required = true) String countValue,
			@RequestParam(value = "buffer", required = true) String buffer) {

		log.info("InventoryController : Start submitInventoryCount");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> cobolReturnDetails = inventoryService.submitInventoryCount( warehouseNo, manufacturer, partNo, pageNo, countValue, buffer);

		if (cobolReturnDetails != null && !cobolReturnDetails.isEmpty()) {

			if (cobolReturnDetails.get("returnCode").equalsIgnoreCase("00000")) {
				response.setResultSet(cobolReturnDetails);
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						cobolReturnDetails.get("returnMsg"), cobolReturnDetails.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}			
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.COBOL_PROGRAM_FAIL_MSG_KEY));
		}
		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This is method used to close inventory for an existing counting list from COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> closeInventory(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo) {

		log.info("InventoryController : Start closeInventory");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> cobolReturnDetails = inventoryService.closeInventory( warehouseNo);

		if (cobolReturnDetails != null && !cobolReturnDetails.isEmpty()) {

			if (cobolReturnDetails.get("returnCode").equalsIgnoreCase("00000")) {
				response.setResultSet(cobolReturnDetails);
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						cobolReturnDetails.get("returnMsg"), cobolReturnDetails.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}			
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.COBOL_PROGRAM_FAIL_MSG_KEY));
		}
		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This method is used to get all activated counting inventory List. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getActivatedCountingList() {

		log.info("InventoryController : Start getActivatedCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		List<ActivatedCountingListDTO> activeCountingList = inventoryService.getActivatedCountingList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(),
				allowedApWarehouses);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(activeCountingList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get selected activated counting inventory List details. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getActivatedCountingDetailList(
			@NotBlank @PathVariable(value = "warehousId", required = true) String warehouseNo,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "sortingBy", required = false) String sortingBy,
			@RequestParam(value = "sortingType", required = false) String sortingType,
			@RequestParam(value = "invStatusIds", required = false)  List<String> invStatusIds) {

		log.info("InventoryController : Start getActivatedCountingDetailList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch activeCountingDetailList = inventoryService.getActivatedCountingDetailList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				warehouseNo, pageSize, pageNumber, sortingBy, sortingType, invStatusIds);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(activeCountingDetailList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get created counting inventory List. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCreatedCountingList(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@RequestParam(value = "invTypeIds", required = false) List<String> invTypeIds) {

		log.info("InventoryController : Start getCreatedCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<CreatedCountingListValues> createdCountingList = inventoryService.getCreatedCountingList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				warehouseNo, invTypeIds);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createdCountingList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	/**
	 * This method is used to get created counting inventory detail List. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCreatedCountingDetailList(
			@NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber){

		log.info("InventoryController : Start getCreatedCountingDetailList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch createdCountingList = inventoryService.getCreatedCountingDetailList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), inventoryListId,
				sortingBy, pageSize, pageNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createdCountingList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get the LOPA (Storage Location) based on provided manufacturer.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getStorageLocationList(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "searchString", required = true) String searchString){

		log.info("InventoryController : Start getStorageLocationList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch lopaList = inventoryService.getStorageLocationList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), manufacturer, 
				pageSize, pageNumber, searchString);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(lopaList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}
	

	/**
	 * This method is used the Counting Group list (Zählgruppe) based on provided manufacturer and warehouse.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCountingGroupList(
			@NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer){

		log.info("InventoryController : Start getCountingGroupList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<CountingGroupDTO> countingGroupList = inventoryService.getCountingGroupList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), manufacturer, warehouseId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(countingGroupList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used the parts brand list (Teilemarke) based on provided manufacturer.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPartsBrandList(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer){

		log.info("InventoryController : Start getPartsBrandList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<PartsBrandDTO> partsBrandList = inventoryService.getPartsBrandList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), manufacturer);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(partsBrandList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}


	/**
	 * This method is used for get the Sorting and print selection (Sortierung und Druckauswahl) list from Stub
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSortingAndPrintSelectionList() {

		log.info("InventoryController : Start getSortingAndPrintSelectionList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> sortingAndPrintSelection = inventoryService.getSortingAndPrintSelectionList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(sortingAndPrintSelection);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used the activate the inventory counting list based on provided inventory List id.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> activateCountingList(
			@NotBlank @RequestParam(value = "inventoryListId", required = true) String inventoryListId){

		log.info("InventoryController : Start activateCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> activateInventory = inventoryService.activateCountingList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), inventoryListId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(activateInventory);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used the Update the inventory Status based on provided inventory List id.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> genericUpdateInventoryStatus(
			@NotBlank @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "newStatusId", required = true) String newStatusId){

		log.info("InventoryController : Start genericUpdateInventoryStatus");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedInventoryStatus = inventoryService.genericUpdateInventoryStatus(decryptUtil.getSchema(), inventoryListId, newStatusId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedInventoryStatus);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}


	/**
	 * This method is used for get the Ansicht (Opinion list) list from Stub
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAnsichtList() {

		log.info("InventoryController : Start getAnsichtList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> ansichtList = inventoryService.getAnsichtList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(ansichtList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get Differential List from DB. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDifferentialList(
			@NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@NotBlank @RequestParam(value = "opinionValue", required = true) String opinionValue,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy,
			@NotBlank @RequestParam(value = "sortingType", required = true) String sortingType){

		log.info("InventoryController : Start getDifferentialList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch differentialList = inventoryService.getDifferentialList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				inventoryListId, opinionValue, pageSize, pageNumber, sortingBy, sortingType);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(differentialList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to create new Counted Part Manuelly into DB
	 * @return Object
	 */
	@Override
	public ResponseEntity<AlphaXResponse> addCountedPartManuelly(
			@Valid @RequestBody AddCountedPartDTO countedPart_obj) {

		log.info("InventoryController : Start addCountedPartManuelly");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AddCountedPartDTO countedPart = inventoryService.addCountedPartManuelly(countedPart_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary() );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(countedPart);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get created counting List value from active counting. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCountingListValueFromActiveCounting(
			@NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId ) {

		log.info("InventoryController : Start getCreatedCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<InventoryDTO> CountingListValue = inventoryService.getCountingListValueFromActiveCounting( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				inventoryListId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(CountingListValue);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get the Printing PageBreak Dropdown list from Stub
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPrintingPageBreakList() {

		log.info("InventoryController : Start getPrintingPageBreakList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> printingPBList = inventoryService.getPrintingPageBreakList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(printingPBList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to update counting list value into O_INVPART table.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateCountingListValueFromActiveCounting(
			@Valid @RequestBody List<CreatedCountingDetailListDTO> createdCountingDetailListDTOs){


		log.info("InventoryController : Start updateCountingListValueFromActiveCounting");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedFlag = inventoryService.updateCountingListValueFromActiveCounting(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				createdCountingDetailListDTOs);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get counting list items. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCountingListItemsFromActiveCounting(
			@NotBlank @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@Pattern(regexp = "[0-9]+") @RequestParam(value = "searchCount", required = false) String searchCount,
			@RequestParam(value = "displayType", required = false) String displayType){


		log.info("InventoryController : Start getCountingListItemsFromActiveCounting");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch createdCountingList = inventoryService.getCountingListItemsFromActiveCounting( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				inventoryListId, pageSize, pageNumber, displayType, searchCount);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createdCountingList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to create new counting list into DB
	 * @return Object
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createNewCountingList(
			@Valid @RequestBody CreateNewCountingListDTO countingList_obj,
			@RequestParam(value = "selectionType", required = false) String selectionType) {

		log.info("InventoryController : Start createNewCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedFlag = inventoryService.createNewCountingList(countingList_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(),
				decryptUtil.getLogedInUser(), selectionType);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to add parts in existing counting list into DB
	 * @return Object
	 */
	@Override
	public ResponseEntity<AlphaXResponse> addPartInExistingCountingList(
			@Valid @RequestBody CreateNewCountingListDTO countingList_obj,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "invListId", required = true) String invListId,
			@RequestParam(value = "selectionType", required = false) String selectionType) {

		log.info("InventoryController : Start addPartInExistingCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedFlag = inventoryService.addPartInExistingCountingList(countingList_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(),
				decryptUtil.getLogedInUser(), selectionType, invListId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to Save / Update Differential list into O_INVPART table.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> saveDifferentialList (
			@Valid @RequestBody List<InventoryDiferentialListDTO> differntialDTOList,
			@NotBlank @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@RequestParam(value = "selectAllFlag", required = true) boolean selectAllFlag,
			@NotBlank @RequestParam(value = "opinionValue", required = true) String opinionValue ){

		log.info("InventoryController : Start saveDifferentialList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedFlag = inventoryService.saveDifferentialList(decryptUtil.getSchema(), differntialDTOList, 
				inventoryListId, selectAllFlag, opinionValue);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used the process the counting list after it is fully counted based on provided inventory List id.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> processCountingListAsFullyCounted(
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "inventoryListId", required = true) String inventoryListId){

		log.info("InventoryController : Start processCountingListAsFullyCounted");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> processInventory = inventoryService.processCountingListAsFullyCounted(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getLogedInUser(), inventoryListId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(processInventory);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to delete a part from counting list based on part no and OEM.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deletePartsFromCountingList(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "invPartId", required = true) String invPartId,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber, 
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "numberOfPlace", required = true) String numberOfPlace){

		log.info("InventoryController : Start deletePartsFromCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> deletePartStatus = inventoryService.deletePartsFromCountingList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				partNumber, manufacturer, numberOfPlace, invPartId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(deletePartStatus);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get all closed/completed counting inventory List. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getClosedCountingList() {

		log.info("InventoryController : Start getClosedCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")) {
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		List<ActivatedCountingListDTO> closedCountingList = inventoryService.getClosedCountingList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				allowedApWarehouses );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(closedCountingList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get selected closed/completed counting inventory List details. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getClosedCountingDetailList(
			@NotBlank @PathVariable(value = "warehousId", required = true) String warehouseNo,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {

		log.info("InventoryController : Start getClosedCountingDetailList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch closedCountingDetailList = inventoryService.getClosedCountingDetailList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				warehouseNo, pageSize, pageNumber );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(closedCountingDetailList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used the mark the differential list As fully processed based on provided inventory List id and warehouseNo.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> markDifferentialListAsFullyProcessed(
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNo", required = true) String warehouseNo){

		log.info("InventoryController : Start markDifferentialListAsFullyProcessed");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> fullyProcessedFlag = inventoryService.markDifferentialListAsFullyProcessed(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getLogedInUser(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), warehouseNo, inventoryListId, decryptUtil.getSavLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(fullyProcessedFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to Copy INVLIST/INVPART into alphaplus history table based on provided inventory List id. and warehouseNo.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> copyInventoryDtlsIntoAlphaplusHistory(
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNo", required = true) String warehouseNo){

		log.info("InventoryController : Start copyInventoryDtlsIntoAlphaplusHistory");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> copyInApHistoryFlag = inventoryService.copyInventoryDtlsIntoAlphaplusHistory(decryptUtil.getDataLibrary(), decryptUtil.getSavLibrary(), 
				decryptUtil.getSchema(), inventoryListId, warehouseNo );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(copyInApHistoryFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used the Update the inventory Status from 2 to 3 in DB based on provided inventory List id.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateInventoryStatus(
			@NotBlank  @Pattern(regexp = "[0-9]+") @RequestParam(value = "inventoryListId", required = true) String inventoryListId){

		log.info("InventoryController : Start updateInventoryStatus");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedInventoryStatus = inventoryService.updateInventoryStatus(decryptUtil.getSchema(), inventoryListId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedInventoryStatus);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get Sum of Delta Stuck and Delta DAK values for Differential list. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSumOfDifferntialDelta(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId){

		log.info("InventoryController : Start getSumOfDifferntialDelta");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		InventoryDiferentialListDTO sumofDeltaValues = inventoryService.getSumOfDifferntialDelta( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), inventoryListId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(sumofDeltaValues);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to add single part in existing counting list into DB
	 * @return Object
	 */
	@Override
	public ResponseEntity<AlphaXResponse> addSinglePartInExistingCountingList(
			@Valid @RequestBody CreateNewCountingListDTO countingList_obj,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "invListId", required = true) String invListId) {

		log.info("InventoryController : Start addPartInExistingCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedFlag = inventoryService.addSinglePartInExistingCountingList(countingList_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(),
				decryptUtil.getLogedInUser(),invListId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get the parts with negative stock amount are existing or not.
	*/	
	@Override
	public ResponseEntity<AlphaXResponse> checkPartsWithNegativeStock(
			@NotBlank  @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber){

		log.info("InventoryController : Start checkPartsWithNegativeStock");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String,Boolean> negativeStockFlag = inventoryService.checkPartsWithNegativeStock(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), warehouseNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(negativeStockFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get the Status of Inventories.
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> getInventoryListStatus() {

		log.info("InventoryController : Start getInventoryListStatus");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		List<DropdownObject> invListStatus = inventoryService.getInventoryListStatus( decryptUtil.getDataLibrary(),	decryptUtil.getSchema() );

		response.setResultSet(invListStatus);
		response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale,ExceptionMessages.SUCCESS_MESSAGE_KEY));

		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

	/**
	 * This is method used to generate counting list name
	 */
	@Override
	public ResponseEntity<AlphaXResponse> generateCountingListName() {

		log.info("InventoryController : Start generateCountingListName");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		String countingListName = inventoryService.generateCountingListName( decryptUtil.getDataLibrary(), decryptUtil.getSchema());

		// set the new order number in result set
		response.setResultSet(countingListName);
		response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale,ExceptionMessages.SUCCESS_MESSAGE_KEY));

		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * This is method used to check for partly counted storage locations for a part
	 */
	@Override
	public ResponseEntity<AlphaXResponse> checkPartlyCountedStorageLocationOfPart( @NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId){

		log.info("InventoryController : Start checkPartlyCountedStorageLocationOfPart");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		boolean isStorageLocationPartlyCounted = inventoryService.checkPartlyCountedStorageLocationOfPart( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), inventoryListId);

		// set the new order number in result set
		response.setResultSet(isStorageLocationPartlyCounted);
		response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale,ExceptionMessages.SUCCESS_MESSAGE_KEY));

		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * This method is used to get the list type.
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> getInventoryListType() {

		log.info("InventoryController : Start getInventoryListType");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		List<DropdownObject> invListType = inventoryService.getInventoryListType( decryptUtil.getDataLibrary(),	decryptUtil.getSchema() );

		response.setResultSet(invListType);
		response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale,ExceptionMessages.SUCCESS_MESSAGE_KEY));

		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * This method is used to get created counting inventory List. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getWarehouseListForCreatedCountingList() {

		log.info("InventoryController : Start getWarehouseListForCreatedCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		List<CreatedCountingListDTO> createdCountingList = inventoryService.getWarehouseListForCreatedCountingList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				allowedApWarehouses);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createdCountingList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
}