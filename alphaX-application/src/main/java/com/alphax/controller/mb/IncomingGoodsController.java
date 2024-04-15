/**
 * 
 */
package com.alphax.controller.mb;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
import com.alphax.service.mb.IncomingGoodsService;
import com.alphax.api.mb.IncomingGoodsApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.BSNDeliveryNotesDTO;
import com.alphax.vo.mb.BSNDeliveryNotesDetailDTO;
import com.alphax.vo.mb.ConflictResolutionDTO;
import com.alphax.vo.mb.DeliveryNoteSparePartDTO;
import com.alphax.vo.mb.DeliveryNotesDTO;
import com.alphax.vo.mb.DeliveryNotesDetailDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class IncomingGoodsController implements IncomingGoodsApi {
	
	@Autowired
	IncomingGoodsService incomingGoodsService;

	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This is IncomingGoodsController method used to get the Lieferscheinliste (delivery notes) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDeliveryNoteList(
			@NotNull @RequestParam(value = "flag", required = true) Integer flag,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos) {
		
		log.info("IncomingGoodsController : Start getDeliveryNoteList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch deliveryNoteList = incomingGoodsService.getDeliveryNoteList(decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), flag, allowedApWarehouses, pageSize, pageNumber, sortingBy, sortingType,
				warehouseNos);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(deliveryNoteList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to get the Lieferschein details (delivery notes Details)  List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDeliveryNoteDetailsList(
			@NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@NotNull @RequestParam(value = "flag", required = true) Integer flag,
			@NotBlank  @RequestParam(value = "orderNumber", required = true) String orderNumber,
			@RequestParam(value = "orderReference", required = true) String orderReference) {
		
		log.info("IncomingGoodsController : Start getDeliveryNoteDetailsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DeliveryNotesDetailDTO> deliveryNoteDetilsList = incomingGoodsService.getDeliveryNoteDetailsList(deliveryNoteNo, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), flag, orderNumber, orderReference );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(deliveryNoteDetilsList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to get the BSN Lieferscheinliste (BSN delivery notes) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBSN_DeliveryNoteList(
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@RequestParam(value = "sortingType", required = true) String sortingType,
			@RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos) {
		
		log.info("IncomingGoodsController : Start getBSN_DeliveryNoteList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch bsnDeliveryNoteList = incomingGoodsService.getBSN_DeliveryNoteList(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), allowedApWarehouses, pageSize, pageNumber, sortingBy, sortingType, warehouseNos);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(bsnDeliveryNoteList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to get the BSN Lieferschein details (BSN delivery notes Details)  List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBSN_DeliveryNoteDetailsList(
			@NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@NotBlank @RequestParam(value = "supplierNumber", required = true) String supplierNumber,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber,
			@NotBlank @RequestParam(value = "bsnDeliveryDate", required = true) String bsnDeliveryDate) {
		
		log.info("IncomingGoodsController : Start getBSN_DeliveryNoteDetailsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<BSNDeliveryNotesDetailDTO> bsnDeliveryNoteDetilsList = incomingGoodsService.getBSN_DeliveryNoteDetailsList(deliveryNoteNo, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), supplierNumber, manufacturer, warehouseNumber, bsnDeliveryDate);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(bsnDeliveryNoteDetilsList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to update the BSN delivery notes Part Number.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateDeliveryNoteSparePartNumber( @Valid @RequestBody DeliveryNoteSparePartDTO partDeliveryNoteDtl ) {
		
		log.info("IncomingGoodsController : Start updateDeliveryNoteSparePartNumber.");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		DeliveryNoteSparePartDTO partDeliveryNoteReceipt = null;
		Locale locale = LocaleContextHolder.getLocale();
		
		partDeliveryNoteReceipt = incomingGoodsService.updateDeliveryNoteSparePartNumber(partDeliveryNoteDtl, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId() );
		
		if (partDeliveryNoteReceipt != null) {
			if (partDeliveryNoteReceipt.getReturnCode().equalsIgnoreCase("0000")) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						partDeliveryNoteReceipt.getReturnMsg(), "200", RestURIConstants.MSG_TYPE_SUCCESS, locale.getLanguage().toUpperCase()));
			} else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						partDeliveryNoteReceipt.getReturnMsg().trim(), "1111", RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 

		} else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.VEHICLE_CREATE_FAIL_MSG_KEY));
		}
		response.setResultSet(partDeliveryNoteReceipt);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to move/take over the delivery notes to BSN.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> takeOverdeliveryNotetoBSN(
			@Valid @RequestBody List<DeliveryNotesDTO> deliveryNoteList,
			@RequestParam(value = "isBSNAll", required = true) boolean isBSNAll,
			@RequestParam(value = "filterText", required = false) String filterText,
			@RequestParam(value = "filterBy", required = false) String filterBy,
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos) {
		
		log.info("IncomingGoodsController : Start takeOverdeliveryNotetoBSN");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, String> deliveryNoteToBSNReceipt = incomingGoodsService.takeOverdeliveryNotetoBSN( deliveryNoteList, decryptUtil.getDataLibrary(), 
				isBSNAll, decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), filterText, filterBy, warehouseNos );
		
		if (deliveryNoteToBSNReceipt != null && !deliveryNoteToBSNReceipt.isEmpty()) {
			
			Map.Entry<String,String> entry = deliveryNoteToBSNReceipt.entrySet().iterator().next();
			
			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.DELIVERYNOTE_TO_BSN_FAILED_MSG_KEY));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to move/take over the delivery notes BSN to ETStamm.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> takeOverdeliveryNoteFromBSNToETStamm(
			@Valid @RequestBody List<BSNDeliveryNotesDTO> bsnDeliveryNoteList,
			@RequestParam(value = "isEtAll", required = true) boolean isEtAll,
			@RequestParam(value = "filterText", required = false) String filterText,
			@RequestParam(value = "filterBy", required = false) String filterBy,
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos) {
		
		log.info("IncomingGoodsController : Start takeOverdeliveryNoteFromBSNToETStamm");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, String> deliveryNoteToETStamm = incomingGoodsService.takeOverdeliveryNoteFromBSNToETStamm( bsnDeliveryNoteList, isEtAll, 
				decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), filterText, filterBy, warehouseNos );
		
		if (deliveryNoteToETStamm != null && !deliveryNoteToETStamm.isEmpty()) {
			Map.Entry<String,String> entry = deliveryNoteToETStamm.entrySet().iterator().next();
			
			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else if(entry.getKey().equalsIgnoreCase("00010")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.BSN_TO_ETSTAMM_SUCCESS_MSG_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.DELIVERYNOTE_FROM_BSN_TO_ETSTAMM_FAILED_MSG_KEY));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to get the delivery note list based on filter.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDeliveryNoteListBasedOnFilter(
			@RequestParam(value = "searchText", required = true) String searchText,
			@RequestParam(value = "searchBy", required = true) String searchBy,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@RequestParam(value = "sortingType", required = true) String sortingType,
			@RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos) {
		
		log.info("IncomingGoodsController : Start getAllDeliveryNoteListBasedOnFilter");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch allDeliveryNoteList = incomingGoodsService.getDeliveryNoteListBasedOnFilter(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), allowedApWarehouses, pageSize, pageNumber, searchText,  searchBy, sortingBy, sortingType, warehouseNos);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(allDeliveryNoteList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<AlphaXResponse> getBSN_DeliveryNoteListBasedOnFilter(
			@RequestParam(value = "searchText", required = true) String searchText,
			@RequestParam(value = "searchBy", required = true) String searchBy,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@RequestParam(value = "sortingType", required = true) String sortingType,
			@RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos) {
		
		log.info("IncomingGoodsController : Start getAllDeliveryNoteListBasedOnFilter");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch allDeliveryNoteList = incomingGoodsService.getBSN_DeliveryNoteListBasedOnFilter(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), allowedApWarehouses, pageSize, pageNumber, searchText, searchBy, sortingBy, sortingType, warehouseNos);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(allDeliveryNoteList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to delete the Delivery Notes from table. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteDeliveryNotes(
			@Valid @RequestBody List<DeliveryNotesDTO> deliveryNoteList ) {
		
		log.info("IncomingGoodsController : Start deleteDeliveryNotes");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, String> deliveryNoteToBSNReceipt = incomingGoodsService.deleteDeliveryNotes( deliveryNoteList, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId() );
		
		if (deliveryNoteToBSNReceipt != null && !deliveryNoteToBSNReceipt.isEmpty()) {
			
			Map.Entry<String,String> entry = deliveryNoteToBSNReceipt.entrySet().iterator().next();
			
			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.DELETE_DELIVERYNOTE_FAILED_MSG_KEY));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to delete the Announced and Cancelled Delivery Notes from table. 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteAnnouncedAndCancelledNotes(
			@Valid @RequestBody List<DeliveryNotesDTO> deliveryNoteList) {
		
		log.info("IncomingGoodsController : Start deleteAnnouncedAndCancelledNotes");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, String> deliveryNoteReceipt = incomingGoodsService.deleteAnnouncedAndCancelledNotes( deliveryNoteList, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId() );
		
		if (deliveryNoteReceipt != null && !deliveryNoteReceipt.isEmpty()) {
			
			Map.Entry<String,String> entry = deliveryNoteReceipt.entrySet().iterator().next();
			
			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.DELETE_ANN_CANCELED_DLIVRYNTE_FAILED_MSG_KEY));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get filter values for Delivery Note
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getFilterValuesForDeliveryNote() {
		
		log.info("IncomingGoodsController : Start getFilterValuesForDeliveryNote");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> orderFilterList = incomingGoodsService.getFilterValuesForDeliveryNote();
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderFilterList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to get the Conflict Lieferscheinliste (Conflict delivery notes) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getConflict_DeliveryNoteList(
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos) {
		
		log.info("IncomingGoodsController : Start getConflict_DeliveryNoteList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch conflictDeliveryNoteList = incomingGoodsService.getConflict_DeliveryNoteList(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), allowedApWarehouses, pageSize, pageNumber, sortingBy, sortingType, warehouseNos);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(conflictDeliveryNoteList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to get the Conflict Lieferschein details (Conflict delivery notes Details)  List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getConflict_DeliveryNoteDetailsList(
			@NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@NotBlank @RequestParam(value = "supplierNumber", required = true) String supplierNumber,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber,
			@NotBlank @RequestParam(value = "bsnDeliveryDate", required = true) String bsnDeliveryDate) {
		
		log.info("IncomingGoodsController : Start getConflict_DeliveryNoteDetailsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<BSNDeliveryNotesDetailDTO> conflictDeliveryNoteDetils = incomingGoodsService.getConflict_DeliveryNoteDetailsList(deliveryNoteNo, decryptUtil.getDataLibrary(), 
				decryptUtil.getSchema(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				supplierNumber, manufacturer, warehouseNumber, bsnDeliveryDate);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(conflictDeliveryNoteDetils);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method is used to Create delivery note  History.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createDeliveryNoteHistory(){
		
		log.info("IncomingGoodsController : Start createDeliveryNoteHistory");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, Boolean> createDeliveryNoteHistory = incomingGoodsService.createDeliveryNoteHistory( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getLogedInUser(), decryptUtil.getWsId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createDeliveryNoteHistory);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		
	}
	
	
	/**
	 * This is IncomingGoodsController method used to get the konfliktbehebung details (Conflict Resolution Details) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getConflictResolution(
			@NotBlank @RequestParam(value = "supplierNumber", required = true) String supplierNumber,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber,
			@NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@NotBlank @RequestParam(value = "orderNumber", required = true) String orderNumber,
			@NotBlank @RequestParam(value = "dispopos", required = true) String dispopos,
			@NotBlank @RequestParam(value = "dispoup", required = true) String dispoup,
			@NotBlank @RequestParam(value = "conflict_Codes", required = true) String conflict_Codes){
		
		log.info("IncomingGoodsController : Start getConflictResolution");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<ConflictResolutionDTO> conflictResolutionDetails = incomingGoodsService.getConflictResolution(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				supplierNumber, manufacturer, warehouseNumber,deliveryNoteNo, partNo, orderNumber, dispopos, dispoup, conflict_Codes  );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(conflictResolutionDetails);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to update Conflict Resolution in deliveryNote / partMaster (Teilestamm ).
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateConflictResolution(
			@Valid @RequestBody List<ConflictResolutionDTO> conflictResolutionList){

		log.info("IncomingGoodsController : Start updateConflictResolution");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, String> conflictResolutionDetails = incomingGoodsService.updateConflictResolution(conflictResolutionList, decryptUtil.getDataLibrary(), 
				decryptUtil.getSchema(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser(), decryptUtil.getWsId() );
		
		if (conflictResolutionDetails != null && !conflictResolutionDetails.isEmpty()) {
			
			Map.Entry<String,String> entry = conflictResolutionDetails.entrySet().iterator().next();
			
			if (entry.getKey().equalsIgnoreCase("00000")) {
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else if(entry.getKey().equalsIgnoreCase("00010")) {
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.BSN_TO_ETSTAMM_SUCCESS_MSG_KEY));
			}
			else {
				alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Conflict Resolution"));
		}
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to get the Solved Conflict Lieferscheinliste (Solved Conflict delivery notes) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSolvedConflicts_DeliveryNoteList(
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos) {
		
		log.info("IncomingGoodsController : Start getSolvedConflicts_DeliveryNoteList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch solvedConflictDeliveryNote = incomingGoodsService.getSolvedConflicts_DeliveryNoteList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), allowedApWarehouses, pageSize, pageNumber, sortingBy, sortingType, warehouseNos);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(solvedConflictDeliveryNote);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is IncomingGoodsController method used to delete the BSN Delivery Notes from table.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteBSNDeliveryNotes( @Valid @RequestBody List<BSNDeliveryNotesDTO> bsnDeliveryNoteList ) {
		
		log.info("IncomingGoodsController : Start deleteBSNDeliveryNotes");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		
		Map<String, Boolean> deletedBSNDeleiveryNotes = incomingGoodsService.deleteBSNDeliveryNotes( decryptUtil.getDataLibrary(), bsnDeliveryNoteList );
		
		response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		
		response.setResultSet(deletedBSNDeleiveryNotes);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This is method is used to cancel delivered parts against their order (Rückstandsauflösung) By COBOL call.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> backlogResolution(
			@NotEmpty  @Size(max = 1) @RequestParam(value = "backlogType", required = true) String backlogType) {
		
		log.info("IncomingGoodsController : Start backlogResolution");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, String> programOutput = incomingGoodsService.backlogResolution(decryptUtil.getDataLibrary(), backlogType, decryptUtil.getApAgencyId(), decryptUtil.getApCompanyId() );
		
		if (programOutput != null && !programOutput.isEmpty()) {
			Map.Entry<String,String> entry = programOutput.entrySet().iterator().next();
			
			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_SUCCESS, locale.getLanguage().toUpperCase()));
			}else if (entry.getKey().equalsIgnoreCase("90080")) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			}else if(entry.getKey().equalsIgnoreCase("90081")) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			}else if(entry.getKey().equalsIgnoreCase("90082")) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			}else if(entry.getKey().equalsIgnoreCase("90099")) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_WARN, locale.getLanguage().toUpperCase()));
			}else if(entry.getKey().equalsIgnoreCase("90098")) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			}else {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Backlog Resolution (Rückstandsauflösung)"));
			}
		}else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Backlog Resolution (Rückstandsauflösung)"));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
}