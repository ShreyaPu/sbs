/**
 * 
 */
package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AddCostsDetailsDTO;
import com.alphax.vo.mb.AuditInvoiceDTO;
import com.alphax.vo.mb.BSNDeliveryNotesDTO;
import com.alphax.vo.mb.BookedDeliveryNoteDetailsDTO;
import com.alphax.vo.mb.DeliveryNotes_BA_DTO;
import com.alphax.vo.mb.InvoiceDeliveryNoteDTO;
import com.alphax.vo.mb.InvoiceDifferncesDTO;
import com.alphax.vo.mb.InvoiceOverviewDTO;
import com.alphax.vo.mb.RecordDifferences_DTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * @author A106744104
 *
 */
@Api(value = "ALPHAX",  tags = {"IncomingGoodsInvoiceApi"} )
@SwaggerDefinition(tags = {
		@Tag(name = "IncomingGoodsInvoiceApi", description = "REST controller that provides information regarding the Incoming Goods Invoice Verification.")
})
public interface IncomingGoodsInvoiceApi {

	@ApiOperation(value = "Returns a list of existing booking texts", notes = "Returns a list of existing booking texts", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get list of existing booking texts", response = InvoiceDifferncesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get list of existing booking texts due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVOICE_BOOKING_TEXT_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBookingTextList(
			@ApiParam(value = "Search string to get a list of booking text.", required = true) @NotBlank @RequestParam(value = "searchString", required = true) String searchString);

	
	@ApiOperation(value = "Returns a list of invoices Number", notes = "Returns a list of invoices Number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get list of invoices Number", response = InvoiceDifferncesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get list of invoices Number due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVOICE_NUMBER_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInvoiceNumberList(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "supplier Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@ApiParam(value = "Invoice Number", required = true) @NotBlank @RequestParam(value = "searchString", required = true) String searchString);
	
	@ApiOperation(value = "Returns a list of record differences (Differenzen erfassen)", notes = "Returns a list of record differences (Differenzen erfassen)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get list of record differences (Differenzen erfassen)", response = RecordDifferences_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get list of record differences (Differenzen erfassen) due to server Error")
	})
	@GetMapping( value = RestURIConstants.RECORD_DIFFERENCES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getRecordDifferencesList(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "supplier Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@ApiParam(value = "Delivery Note Number", required = true)  @NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo);

	
	@ApiOperation(value = "Capture record differences (Differenzen erfassen) ", notes = "Update existing TNR Number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Capture record differences (Differenzen erfassen)", response = RecordDifferences_DTO.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to capture record differences (Differenzen erfassen) due to server Error") 
	})
	@PostMapping(value = RestURIConstants.RECORD_DIFFERENCES)
	public ResponseEntity<AlphaXResponse> saveRecordDifferences(
			@ApiParam(value = "Record differences (Differenzen erfassen) Object", required = true) @Valid @RequestBody List<RecordDifferences_DTO> recordDifferencesListObj);
	
	@ApiOperation(value = "Corrections in booking for delivery notes related to invoices data (Komigieren). ", notes = "Corrections in booking for delivery notes", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Corrections in booking for delivery notes related to invoices data (Komigieren).", response = BookedDeliveryNoteDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to do corrections in booking for delivery notes related to invoices data (Komigieren) due to server Error") 
	})
	@PostMapping(value = RestURIConstants.CORRECTION_IN_BOOKING)
	public ResponseEntity<AlphaXResponse> correctionsInBookingForDeliveryNotes(
			@ApiParam(value = "corrections in booking for delivery notes Object", required = true) @Valid @RequestBody BookedDeliveryNoteDetailsDTO correctionsInBooking_DTO);
	
	@ApiOperation(value = "Returns differences amount (Differenzen)", notes = "Returns differences amount (Differenzen)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get differences amount", response = RecordDifferences_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get differences amount due to server Error")
	})
	@GetMapping( value = RestURIConstants.DIFFERENCES_VALUE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCalculatedDifferanceValue(
			@ApiParam(value = "Invoice Number", required = true)  @NotBlank @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber);
	
	
	@ApiOperation(value = "Returns a list of Additional Costs for invoices", notes = "Returns a list of Additional Costs for invoices", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Additional Costs list", response = AddCostsDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Additional Costs list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADDITIONAL_COSTS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAdditionalCostList(
			@ApiParam(value = "Invoice Number", required = true)  @NotBlank @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber);
	
	
	@ApiOperation(value = "Save / Update the Additional cost details", notes = "Save / Update the Additional cost details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Save Additional cost for InvoiceNo", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to save Additional cost for InvoiceNo due to server Error")
	})
	@PutMapping(value = RestURIConstants.ADDITIONAL_COSTS, produces = {"application/JSON"})
	public ResponseEntity<AlphaXResponse> saveAdditionalCostDetails(
			@ApiParam(value = "Additional Cost detail List Object", required = true) @Valid @RequestBody List<AddCostsDetailsDTO> additionalCostDto,
			@ApiParam(value = "Invoice Number", required = true) @NotBlank @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber);
	
	@ApiOperation(value = "Create invoice manually ", notes = "Create invoice manually ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create invoice manually.", response = BSNDeliveryNotesDTO.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to create invoice manually due to server Error") 
	})
	@PostMapping(value = RestURIConstants.CREATE_INVOICE_ENTRY_MANUALLY)
	public ResponseEntity<AlphaXResponse> createInvoiceManually(
					@ApiParam(value = "Invoice Object", required = true) @Valid @RequestBody AuditInvoiceDTO auditInvoiceObj);
	
	@ApiOperation(value = "Import delivery note / invoice data from O_ARPDC", notes = "Import delivery note / invoice data from O_ARPDC", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Import delivery note / invoice data from O_ARPDC to SA53, SA60 and SA61 table", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to import delivery note / invoice data from O_ARPDC  due to server Error")
	})
	@PutMapping(value = RestURIConstants.IMPORT_INVOICE_DATA_FROM_O_ARPDC, produces = {"application/JSON"})
	public ResponseEntity<AlphaXResponse> importInvoiceDataFromO_ARPDC();
	
	
	@ApiOperation(value = "Overview of Invoice data list", notes = "Overview of Invoice data list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Invoice lists", response = InvoiceOverviewDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all invoice lists due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVOICE_OVERVIEW, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getOverviewOfInvoiceList(
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	

	@ApiOperation(value = "Returns a list of delivery Notes that are not assigned to invoice", notes = "Returns a list of delivery Notes that are not assigned to invoice", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "list of delivery Notes that are not assigned to invoice ", response = AlphaXResponse.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get list of delivery Notes that are not assigned to invoice due to server Error")
	})
	@GetMapping( value = RestURIConstants.AUDIT_NOT_ASSIGNED_DELIVERY_NOTES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAudit_NotAssignedDeliveryNotes(
			@ApiParam(value = "Manufacturer ", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Warehouse",required  = true) @NotBlank  @RequestParam(value = "warehouse", required = true) String warehouse);
	
	@ApiOperation(value = "Returns a list of delivery Notes that are  assigned to invoice", notes = "Returns a list of delivery Notes that are  assigned to invoice", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "list of delivery Notes that are  assigned to invoice ", response = AlphaXResponse.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get list of delivery Notes that are  assigned to invoice due to server Error")
	})
	@GetMapping( value = RestURIConstants.AUDIT_ASSIGNED_DELIVERY_NOTES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAudit_AssignedDeliveryNotes(
			@ApiParam(value = "Manufacturer ", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Invoice  Number", required = true) @NotBlank @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber);
	
	
	@ApiOperation(value = "Invoice details list", notes = "Invoice details list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Invoice details lists", response = InvoiceOverviewDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all invoice details lists due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVOICE_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInvoiceDetailList(
			@ApiParam(value = "Invoice number", required = true) @NotBlank @RequestParam(value = "invoiceNo", required = true) String invoiceNo,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer);
	
	@ApiOperation(value = "Booked delivery note details", notes = "Booked delivery note details from DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get booked delivery note details", response = BookedDeliveryNoteDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get booked delivery note details due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_BOOKED_DELIVERY_NOTE_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBookedDeliveryNoteDetails(
			@ApiParam(value = "delivery Note No", required = true) @NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	@ApiOperation(value = "Update invoice Delivery Note as Approved ", notes = "Update invoice Delivery Note as Approved based on input object", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update invoice Delivery Note as Approved ", response = DeliveryNotes_BA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update invoice Delivery Note as Approved due to server Error")
	})
	@PutMapping( value = RestURIConstants.UPDATE_INVOICE_DELIVERY_NOTE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateInvoiceDeliveryNoteAsApproved(
			@ApiParam(value = "Object of Invoice delivery notes", required = true) @Valid @RequestBody DeliveryNotes_BA_DTO  deliveryNotes_BA_DTO );

	@ApiOperation(value = "return the count of not Imported Invoice Item", notes = "return the count of not Imported Invoice Item", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "return the count of not Imported Invoice Item", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get a return the count of not Imported Invoice Item due to server Error")
	})
	@GetMapping( value = RestURIConstants.RETURN_COUNT_OF_NOT_IMPORTED_INCOICE_ITEM, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCountOfNotImportedInvoiceItems();
	

	@ApiOperation(value = "set/remove assigned delivery notes for an invoice", notes = "set/remove assigned delivery notes for an invoice", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "set/remove assigned delivery notes for an invoice", response = InvoiceDeliveryNoteDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to set/remove assigned delivery notes for an invoice due to server Error")
	})
	@PutMapping( value = RestURIConstants.SET_ASSIGN_DELIVERY_NOTE_FOR_INVOICE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> setAssignDeliveryNoteForInvoice(
			@ApiParam(value = "Object of set/remove assigned delivery notes for an invoice ", required = true) @Valid @RequestBody List<InvoiceDeliveryNoteDTO> auditInvoiceObj	);
	
	
	@ApiOperation(value = "Update invoice Amount for Delivery Note ", notes = "Update invoice Amount for Delivery Note", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update invoice Amount for Delivery Note ", response = BookedDeliveryNoteDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update invoice Amount for Delivery Note due to server Error")
	})
	@PutMapping( value = RestURIConstants.UPDATE_INVOICE_AMOUNT_DELIVERY_NOTE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateInvoiceAmountForDeliveryNote(
			@ApiParam(value = "Object of Invoice delivery notes", required = true) @Valid @RequestBody BookedDeliveryNoteDetailsDTO  BookedDeliveryNoteDetailsDTO );

	@ApiOperation(value = "Returns warehouse list for Invoice", notes = "Returns warehouse list for Invoice", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Returns warehouse list for Invoice", response = InvoiceOverviewDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Returns warehouse list for Invoice due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_WAREHOUSE_LIST_FOR_INVOICE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getWarehouseListForInvoice();
}

