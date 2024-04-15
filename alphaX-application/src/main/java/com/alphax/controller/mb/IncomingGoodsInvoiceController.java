/**
 * 
 */
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.IncomingGoodsInvoiceService;
import com.alphax.api.mb.IncomingGoodsInvoiceApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.ActivatedCountingListDTO;
import com.alphax.vo.mb.AddCostsDetailsDTO;
import com.alphax.vo.mb.AuditAssignDeliveryNotesDTO;
import com.alphax.vo.mb.AuditInvoiceDTO;
import com.alphax.vo.mb.BookedDeliveryNoteDetailsDTO;
import com.alphax.vo.mb.DeliveryNotes_BA_DTO;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.InvoiceDeliveryNoteDTO;
import com.alphax.vo.mb.InvoiceDifferncesDTO;
import com.alphax.vo.mb.InvoiceOverviewDTO;
import com.alphax.vo.mb.RecordDifferences_DTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class IncomingGoodsInvoiceController implements IncomingGoodsInvoiceApi {
	
	@Autowired
	IncomingGoodsInvoiceService incomingGoodsInvoiceService;

	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This is IncomingGoodsInvoiceController method used to get the List of existing Booking Texts.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBookingTextList(
			@NotBlank @RequestParam(value = "searchString", required = true) String searchString) {
		
		log.info("IncomingGoodsInvoiceController : Start getBookingTextList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		GlobalSearch bookingTextList = incomingGoodsInvoiceService.getBookingTextList(decryptUtil.getSchema(), searchString);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(bookingTextList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the List of invoices Number.
	 * @return List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getInvoiceNumberList(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@NotBlank @RequestParam(value = "searchString", required = true) String searchString) {
		
		log.info("IncomingGoodsInvoiceController : Start getInvoiceNumberList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<InvoiceDifferncesDTO> reasonsList = incomingGoodsInvoiceService.getInvoiceNumberList(decryptUtil.getDataLibrary(),
				decryptUtil.getSchema(),warehouseNo, manufacturer,supplierNo, searchString );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reasonsList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**
	 * This method is used to get the List of record differences (Differenzen erfassen).
	 * @return List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getRecordDifferencesList(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo) {
		
		log.info("IncomingGoodsInvoiceController : Start getRecordDifferencesList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<RecordDifferences_DTO> reasonsList = incomingGoodsInvoiceService.getRecordDifferencesList(decryptUtil.getDataLibrary(),
				decryptUtil.getSchema(),deliveryNoteNo,manufacturer,warehouseNo,supplierNo );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reasonsList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**
	 * This method is used to Capture record differences (Differenzen erfassen) list.
	 * @return List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> saveRecordDifferences(
			@Valid @RequestBody List<RecordDifferences_DTO> recordDifferencesListObj) {
		
		log.info("IncomingGoodsInvoiceController : Start saveRecordDifferences");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<RecordDifferences_DTO> reasonsList = incomingGoodsInvoiceService.saveRecordDifferences(recordDifferencesListObj, 
				decryptUtil.getDataLibrary(), decryptUtil.getSchema());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reasonsList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**
	 * This method is used to do corrections in booking of delivery notes related to invoices data (Komigieren).
	 * @return boolean
	 */
	@Override
	public ResponseEntity<AlphaXResponse> correctionsInBookingForDeliveryNotes(
			@Valid @RequestBody BookedDeliveryNoteDetailsDTO correctionsInBooking_DTO) {
		
		log.info("IncomingGoodsInvoiceController : Start correctionsInBookingForDeliveryNotes");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, String> corrections_DTO = incomingGoodsInvoiceService.correctionsInBookingForDeliveryNotes(correctionsInBooking_DTO, 
				decryptUtil.getDataLibrary(), decryptUtil.getSchema(),decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser());
		
		if (corrections_DTO != null && !corrections_DTO.isEmpty()) {

			if (corrections_DTO.get("returnCode").equalsIgnoreCase("00000")) {
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}else {
				alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
						corrections_DTO.get("returnMsg"), corrections_DTO.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Departures BA (Abgänge BA)"));
		}
		
		alphaxResponse.setResultSet(corrections_DTO);
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**
	 * This method is used to get calculated differance value.
	 * @return List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCalculatedDifferanceValue(
			@NotBlank @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber) {
		
		log.info("IncomingGoodsInvoiceController : Start getCalculatedDifferanceValue");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String differanceValue = incomingGoodsInvoiceService.getCalculatedDifferanceValue(decryptUtil.getDataLibrary(),
				decryptUtil.getSchema(),invoiceNumber );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(differanceValue);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the List of Additional cost for invoices Number.
	 * @return List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAdditionalCostList(
			@NotBlank @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber) {
		
		log.info("IncomingGoodsInvoiceController : Start getAdditionalCostList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<AddCostsDetailsDTO> addCostList = incomingGoodsInvoiceService.getAdditionalCostList(decryptUtil.getSchema(), invoiceNumber );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(addCostList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for Save / Update the Additional cost details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> saveAdditionalCostDetails(
			@Valid @RequestBody List<AddCostsDetailsDTO> additionalCostDtoList,
			@NotBlank @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber) {
		
		log.info("IncomingGoodsInvoiceController : Start saveAdditionalCostDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> savedAddCost = incomingGoodsInvoiceService.saveAdditionalCostDetails(additionalCostDtoList, invoiceNumber, 
				decryptUtil.getSchema(), decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(savedAddCost);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**s
	 * This is method is used to create invoice manually.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createInvoiceManually(@Valid AuditInvoiceDTO auditInvoiceObj) {
		log.info("IncomingGoodsInvoiceController : Start createInvoiceManually");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		 Map<String, Boolean> createInvoiceManually = incomingGoodsInvoiceService.createInvoiceManually( auditInvoiceObj, decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getLogedInUser());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createInvoiceManually);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**
	 * This method is used to import invoice data from O_ARPDC from DB.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> importInvoiceDataFromO_ARPDC() {
		
		log.info("IncomingGoodsInvoiceController : Start importInvoiceDataFromO_ARPDC");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> savedAddCost = incomingGoodsInvoiceService.importInvoiceDataFromO_ARPDC(decryptUtil.getSchema(),decryptUtil.getDataLibrary(), decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(savedAddCost);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to Overview the Invoices data List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getOverviewOfInvoiceList(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo) {

		log.info("IncomingGoodsInvoiceController : Start getOverviewOfInvoiceList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<InvoiceOverviewDTO> invoiceList = incomingGoodsInvoiceService.getOverviewOfInvoiceList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				warehouseNo);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(invoiceList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is method is used to get the unassigned list of delivery Notes.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAudit_NotAssignedDeliveryNotes(@NotBlank String manufacturer,
			@NotBlank String warehouse) {
		// TODO Auto-generated method stub
		log.info("IncomingGoodsInvoiceController : Start getAudit_NotAssignedDeliveryNotes");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<AuditAssignDeliveryNotesDTO> notAssignedeliveryNoteList = incomingGoodsInvoiceService.getAudit_NotAssignedDeliveryNotes( decryptUtil.getSchema(), decryptUtil.getDataLibrary() ,manufacturer ,warehouse);
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(notAssignedeliveryNoteList);
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
		
	}
	
	
	 /** 
	 *This is method is used to get the assigned list of delivery Notes.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAudit_AssignedDeliveryNotes(@NotBlank String manufacturer,
			@NotBlank String invoiceNumber) {
		// TODO Auto-generated method stub
		log.info("IncomingGoodsInvoiceController : Start getAudit_AssignedDeliveryNotes");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		List<AuditAssignDeliveryNotesDTO> assignedeliveryNoteList = incomingGoodsInvoiceService.getAudit_AssignedDeliveryNotes( decryptUtil.getSchema(), decryptUtil.getDataLibrary() ,manufacturer ,invoiceNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(assignedeliveryNoteList);
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the Invoices detail data List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getInvoiceDetailList(
			@NotBlank @RequestParam(value = "invoiceNo", required = true) String invoiceNo,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer ) {

		log.info("IncomingGoodsInvoiceController : Start getInvoiceDetailList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<InvoiceOverviewDTO> invoiceDetailList = incomingGoodsInvoiceService.getInvoiceDetailList( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				invoiceNo, warehouseNo, manufacturer);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(invoiceDetailList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

	/**
	 * This method is used to get invoiced details and delivery note details for items of a delivery note.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBookedDeliveryNoteDetails(
			@NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo) {

		log.info("IncomingGoodsInvoiceController : Start getBookedDeliveryNoteDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<BookedDeliveryNoteDetailsDTO> deliveryNoteDetailList = incomingGoodsInvoiceService.getBookedDeliveryNoteDetails( decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				deliveryNoteNo, warehouseNo);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(deliveryNoteDetailList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to update the delivery note as Approved.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateInvoiceDeliveryNoteAsApproved(
			@Valid DeliveryNotes_BA_DTO deliveryNotes_BA_DTO) {
		// TODO Auto-generated method stub
		log.info("IncomingGoodsInvoiceController : Start updateInvoiceDeliveryNoteAsApproved");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, Boolean> updateinvoiceDeliveryNote = incomingGoodsInvoiceService.updateInvoiceDeliveryNoteAsApproved(deliveryNotes_BA_DTO, decryptUtil.getDataLibrary(),decryptUtil.getLogedInUser(),decryptUtil.getSchema());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updateinvoiceDeliveryNote);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		
	}

	/**
	 * This method is used to return the count of not imported Invoice Item (O_sa60 & O_ARPDC).
	 * @return Object
	 */

	@Override
	public ResponseEntity<AlphaXResponse> getCountOfNotImportedInvoiceItems() {
		// TODO Auto-generated method stub
		log.info("IncomingGoodsInvoiceController : Start getCountOfNotImportedInvoiceItems");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Map<String,String> counts = incomingGoodsInvoiceService.getCountOfNotImportedInvoiceItems(decryptUtil.getDataLibrary(), decryptUtil.getSchema());
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(counts);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		
	}
	

	/**
	 * This method is used to return the set/remove assigned delivery notes for an invoice.
	 * @return Object
	 */
	@Override
	public ResponseEntity<AlphaXResponse> setAssignDeliveryNoteForInvoice(
			@Valid @RequestBody List<InvoiceDeliveryNoteDTO> auditInvoiceObj)
			 {
		// TODO Auto-generated method stub
		log.info("IncomingGoodsInvoiceController : Start setAssignDeliveryNoteForInvoice");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> assignDeliveryNoteForInvoice  = incomingGoodsInvoiceService.setAssignDeliveryNoteForInvoice(auditInvoiceObj ,decryptUtil.getSchema(), decryptUtil.getDataLibrary(),decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(assignDeliveryNoteForInvoice);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	
	}
	
	
	/**
	 * This method is used to Update invoice Amount for Delivery Note for an invoice.
	 * @return Object
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateInvoiceAmountForDeliveryNote(
			@Valid BookedDeliveryNoteDetailsDTO BookedDeliveryNoteDetailsDTO) {
		// TODO Auto-generated method stub
		log.info("IncomingGoodsInvoiceController : Start updateInvoiceAmountForDeliveryNote");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();
		
		Map<String, Boolean> updateInvoiceAmountForDeliveryNote = incomingGoodsInvoiceService.updateInvoiceAmountForDeliveryNote(BookedDeliveryNoteDetailsDTO, decryptUtil.getDataLibrary(),decryptUtil.getSchema());
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updateInvoiceAmountForDeliveryNote);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		
	}
	
	/**
	 * This method is used to Returns warehouse list for Invoice.
	 * @return Object
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getWarehouseListForInvoice() {
		log.info("IncomingGoodsInvoiceController : Start getWarehouseListForInvoice");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ActivatedCountingListDTO> warehouseList = incomingGoodsInvoiceService.getWarehouseListForInvoice( decryptUtil.getDataLibrary(), decryptUtil.getSchema(),
				allowedApWarehouses);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(warehouseList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}



	
}