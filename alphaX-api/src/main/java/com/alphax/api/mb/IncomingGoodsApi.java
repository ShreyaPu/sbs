/**
 * 
 */
package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.BSNConflictsDetailDTO;
import com.alphax.vo.mb.BSNDeliveryNotesDTO;
import com.alphax.vo.mb.BSNDeliveryNotesDetailDTO;
import com.alphax.vo.mb.ConflictResolutionDTO;
import com.alphax.vo.mb.DeliveryNoteSparePartDTO;
import com.alphax.vo.mb.DeliveryNotesDTO;
import com.alphax.vo.mb.DeliveryNotesDetailDTO;

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
@Api(value = "ALPHAX",  tags = {"IncomingGoods"} )
@SwaggerDefinition(tags = {
		@Tag(name = "IncomingGoods", description = "REST controller that provides information regarding the Incoming Goods.")
})
public interface IncomingGoodsApi {

	@ApiOperation(value = "Returns a list of Lieferscheinliste (delivery notes List)", notes = "Returns a list of Lieferscheinliste (delivery notes List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Lieferscheinliste (delivery notes) List", response = DeliveryNotesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Lieferscheinliste list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_DELIVERY_NOTE_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDeliveryNoteList(
			@ApiParam(value = "the flag that need to be used to represent SA value. For SA = 53, it is 1 and for SA = 54 and 56 it is 2", required = true) @NotNull @RequestParam(value = "flag", required = true) Integer flag,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by that is use for column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = false) @RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos);
	
	
	@ApiOperation(value = "Returns a list of Lieferschein details (delivery notes Details List)", notes = "Returns a list of Lieferschein details (delivery notes Details List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Lieferschein Details (delivery notes Details) List", response = DeliveryNotesDetailDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Lieferschein details list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_DELIVERY_NOTE_DETAILS_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDeliveryNoteDetailsList(
			@ApiParam(value = "delivery Note Number", required = true) @NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@ApiParam(value = "the flag that need to be used to represent SA value. For SA = 53, it is 1 and for SA = 54 and 56 it is 2", required = true) @NotNull @RequestParam(value = "flag", required = true) Integer flag,
			@ApiParam(value = "order Number", required = true) @NotBlank  @RequestParam(value = "orderNumber", required = true) String orderNumber,
			@ApiParam(value = "order Reference", required = true)  @RequestParam(value = "orderReference", required = true) String orderReference);
	
	
	@ApiOperation(value = "Returns a list of BSN Lieferscheinliste (BSN delivery notes List)", notes = "Returns a list of BSN Lieferscheinliste (BSN delivery notes List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get BSN Lieferscheinliste (BSN delivery notes) List", response = BSNDeliveryNotesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get BNS Lieferscheinliste list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_BSN_DELIVERY_NOTE_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBSN_DeliveryNoteList(
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by that is use for column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = false) @RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos);
	
	
	@ApiOperation(value = "Returns a list of BSN Lieferschein details (BSN delivery notes Details)", notes = "Returns a list of BSN Lieferschein details (BSN delivery notes Details)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get BSN Lieferschein Details (BSN delivery notes Details) List", response = BSNDeliveryNotesDetailDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get BSN Lieferschein details list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_BSN_DELIVERY_NOTE_DETAILS_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBSN_DeliveryNoteDetailsList(
			@ApiParam(value = "delivery Note Number", required = true) @NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@ApiParam(value = "the Supplier Number", required = true) @NotBlank @RequestParam(value = "supplierNumber", required = true) String supplierNumber,
			@ApiParam(value = "the Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "the Warehouse Number", required = true) @NotBlank  @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber,
			@ApiParam(value = "the BSN Delivery notes date", required = true) @NotBlank  @RequestParam(value = "bsnDeliveryDate", required = true) String bsnDeliveryDate);
			
	
	@ApiOperation(value = "Update existing TNR Number", notes = "Update existing TNR Number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update delivery note spare part Object", response = DeliveryNoteSparePartDTO.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to update delivery note spare part number due to server Error") 
	})
	@PutMapping(value = RestURIConstants.UPDATE_DELIVERY_NOTE_SPARE_PART)
	public ResponseEntity<AlphaXResponse> updateDeliveryNoteSparePartNumber(
			@ApiParam(value = "part delivery note receipt Object", required = true) @Valid @RequestBody DeliveryNoteSparePartDTO partDeliveryNoteDtl);
	
	
	@ApiOperation(value = "Take over deliveryNotes list to BSN", notes = "Take over deliveryNotes list to BSN", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Take over deliveryNotes list to BSN", response = DeliveryNotesDTO.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to Take over deliveryNotes list to BSN due to server Error") 
	})		
	@PostMapping(value = RestURIConstants.DELIVERY_NOTE_TO_BSN)
	public ResponseEntity<AlphaXResponse> takeOverdeliveryNotetoBSN(
					@ApiParam(value = "part delivery note receipt Object", required = true) @Valid @RequestBody List<DeliveryNotesDTO> deliveryNoteList,
					@ApiParam(value = "BSN All flag", required = true) @RequestParam(value = "isBSNAll", required = true) boolean isBSNAll,
					@ApiParam(value = "searchText that need to use for filter value", required = false)  @RequestParam(value = "filterText", required = false) String filterText, 
					@ApiParam(value = "searchBy that need to use for filter criteria", required = false)  @RequestParam(value = "filterBy", required = false) String filterBy,
					@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos);
			
	
	@ApiOperation(value = "Take over deliveryNotes list from BSN to ET-Stamm ", notes = "Take over deliveryNotes list from BSN to ET-Stamm ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Take over deliveryNotes list from BSN to ET-Stamm", response = BSNDeliveryNotesDTO.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to Take over deliveryNotes list from BSN to ET-Stamm due to server Error") 
	})		
	@PostMapping(value = RestURIConstants.DELIVERY_NOTE_TO_ET_STAMM)
	public ResponseEntity<AlphaXResponse> takeOverdeliveryNoteFromBSNToETStamm(
					@ApiParam(value = "part delivery note receipt Object", required = true) @Valid @RequestBody List<BSNDeliveryNotesDTO> bsnDeliveryNoteList,
					@ApiParam(value = "ET All flag", required = true) @RequestParam(value = "isEtAll") boolean isEtAll,
					@ApiParam(value = "searchText that need to use for filter value", required = false)  @RequestParam(value = "filterText", required = false) String filterText, 
					@ApiParam(value = "searchBy that need to use for filter criteria", required = false) @RequestParam(value = "filterBy", required = false) String filterBy,
					@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos);
			
	
	@ApiOperation(value = "Returns delivery note list based on filter ", notes = "Returns delivery note list based on filter", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "delivery note list", response = BSNDeliveryNotesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get delivery note list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_DELIVERY_NOTE_LIST_BASED_ON_FILTER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDeliveryNoteListBasedOnFilter(
			@ApiParam(value = "searchText that need to use for filter value", required = true)  @RequestParam(value = "searchText", required = true) String searchText, 
			@ApiParam(value = "searchBy that need to use for filter criteria", required = true)  @RequestParam(value = "searchBy", required = true) String searchBy, 
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by that is use for column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = false) @RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos);
	
	
	@ApiOperation(value = "Returns BSN delivery note list based on filter ", notes = "Returns BSN delivery note list based on filter", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "BSN delivery note list", response = BSNDeliveryNotesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get BSN delivery note list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_BSN_DELIVERY_NOTE_LIST_BASED_ON_FILTER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBSN_DeliveryNoteListBasedOnFilter(
			@ApiParam(value = "searchText that need to use for filter value", required = true)  @RequestParam(value = "searchText", required = true) String searchText, 
			@ApiParam(value = "searchBy that need to use for filter criteria", required = true)  @RequestParam(value = "searchBy", required = true) String searchBy, 
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by that is use for column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = false) @RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos);
	
	
	@ApiOperation(value = "Delete selected delivery Notes from list", notes = "Delete selected delivery Notes from list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete selected delivery Notes from list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to delete delivery Notes list due to server Error")
	})		
	@DeleteMapping(value = RestURIConstants.GET_DELIVERY_NOTE_LIST)
	public ResponseEntity<AlphaXResponse> deleteDeliveryNotes(
					@ApiParam(value = "part delivery note receipt Object", required = true) @Valid @RequestBody List<DeliveryNotesDTO> deliveryNoteList);
	
	
	@ApiOperation(value = "Delete selected Announced and Cancelled delivery Notes from list", notes = "Delete selected Announced and Cancelled delivery Notes from list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete selected Announced and Cancelled delivery Notes from list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to delete Announced and Cancelled delivery Notes list due to server Error")
	})		
	@DeleteMapping(value = RestURIConstants.ANNOUNCED_CANCELED_DELIVERY_NOTE)
	public ResponseEntity<AlphaXResponse> deleteAnnouncedAndCancelledNotes(
					@ApiParam(value = "part delivery note receipt Object", required = true) @Valid @RequestBody List<DeliveryNotesDTO> deliveryNoteList);
	
	
	@ApiOperation(value = "Returns a list of filter values for Delivert note ", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get filter values for DeliveryNote", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get filter values for DeliveryNote due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_FILTER_VALUE_DELIVERY_NOTE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getFilterValuesForDeliveryNote();
	
	
	@ApiOperation(value = "Returns Lieferscheine mit Konflikten list (Conflict delivery notes List)", notes = "Lieferscheine mit Konflikten list (Conflict delivery notes List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Lieferscheine mit Konflikten (Conflict delivery notes) List", response = BSNDeliveryNotesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Conflict Lieferscheinliste list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_CONFLICT_DELIVERY_NOTE_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getConflict_DeliveryNoteList(
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by that is use for column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = false) @RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos);
	
	
	@ApiOperation(value = "Returns Lieferscheine mit Konflikten details (Conflict delivery notes Details)", notes = "Returns Lieferscheine mit Konflikten details (Conflict delivery notes Details)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Lieferscheine mit Konflikten Details (Conflict delivery notes Details) List", response = BSNDeliveryNotesDetailDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Conflict Lieferschein details list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_CONFLICT_DELIVERY_NOTE_DETAILS_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getConflict_DeliveryNoteDetailsList(
			@ApiParam(value = "delivery Note Number", required = true) @NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@ApiParam(value = "the Supplier Number", required = true) @NotBlank @RequestParam(value = "supplierNumber", required = true) String supplierNumber,
			@ApiParam(value = "the Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "the Warehouse Number", required = true) @NotBlank  @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber,
			@ApiParam(value = "the BSN Delivery notes date", required = true) @NotBlank  @RequestParam(value = "bsnDeliveryDate", required = true) String bsnDeliveryDate);
	

	@ApiOperation(value = "Create delivery note history", notes = "Create delivery note history", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create delivery note history", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to create delivery note history due to server Error")
	})
	@PutMapping( value = RestURIConstants.CREATE_DELIVERY_NOTE_HISTORY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createDeliveryNoteHistory();
	

	@ApiOperation(value = "Returns konfliktbehebung details (Conflict Resolution Details) list", notes = "Returns konfliktbehebung details (Conflict Resolution Details) list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get konfliktbehebung details (Conflict Resolution Details) List", response = ConflictResolutionDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get konfliktbehebung details (Conflict Resolution Details) list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_CONFLICT_RESOLUTION, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getConflictResolution(
			@ApiParam(value = "the Supplier Number", required = true) @NotBlank @RequestParam(value = "supplierNumber", required = true) String supplierNumber,
			@ApiParam(value = "the Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "the Warehouse Number", required = true) @NotBlank  @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber,
			@ApiParam(value = "delivery Note Number", required = true) @NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@ApiParam(value = "part Number", required = true) @NotBlank  @RequestParam(value = "partNo", required = true) String partNo,
			@ApiParam(value = "order Number", required = true) @NotBlank  @RequestParam(value = "orderNumber", required = true) String orderNumber,
			@ApiParam(value = "dispopos", required = true) @NotBlank  @RequestParam(value = "dispopos", required = true) String dispopos,
			@ApiParam(value = "dispoup", required = true) @NotBlank  @RequestParam(value = "dispoup", required = true) String dispoup,
			@ApiParam(value = "conflict Codes", required = true) @NotBlank  @RequestParam(value = "conflict_Codes", required = true) String conflict_Codes);
	
	
	@ApiOperation(value = "Conflict Resolution in deliveryNote / partMaster (Teilestamm )", notes = "Conflict Resolution in deliveryNote / partMaster (Teilestamm )", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Conflict Resolution in deliveryNote / partMaster (Teilestamm )", response = ConflictResolutionDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update Conflict Resolution in deliveryNote / partMaster (Teilestamm ) due to server Error")
	})
	@PostMapping( value = RestURIConstants.GET_CONFLICT_RESOLUTION, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateConflictResolution(
			@ApiParam(value = "Conflict Resolution Object", required = true) @Valid @RequestBody List<ConflictResolutionDTO> conflictResolutionList);

	
	@ApiOperation(value = "Returns Lieferscheine behobene Konflikten list (Solved Conflict delivery notes List)", notes = "Lieferscheine behobene Konflikten list (Solved Conflict delivery notes List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Lieferscheine behobene Konflikten (Solved Conflict delivery notes) List", response = BSNConflictsDetailDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get solved Conflict Lieferscheinliste list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SOLVED_CONFLICT_DN_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSolvedConflicts_DeliveryNoteList(
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by that is use for column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = false) @RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos);


	@ApiOperation(value = "Delete selected BSN delivery Notes from list (Lieferscheine Wareneingang) ", notes = "Delete selected BSN delivery Notes from list (Lieferscheine Wareneingang)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete selected bsn delivery Notes from list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to delete bsn delivery Notes list due to server Error")
	})		
	@DeleteMapping(value = RestURIConstants.GET_BSN_DELIVERY_NOTE_LIST)
	public ResponseEntity<AlphaXResponse> deleteBSNDeliveryNotes(
			@ApiParam(value = "BSN delivery note receipt Object", required = true) @Valid @RequestBody List<BSNDeliveryNotesDTO> bsnDeliveryNoteList);
	
	@ApiOperation(value = "Cancel delivered parts against their order (Rückstandsauflösung)  ", notes = "cancel delivered parts against their order (Rückstandsauflösung)  by COBOL Call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Cancel delivered parts against their order (Rückstandsauflösung)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to Cancel delivered parts against their order (Rückstandsauflösung) due to server Error")
	})		
	@PutMapping(value = RestURIConstants.BACKLOG_RESOLUTION)
	public ResponseEntity<AlphaXResponse> backlogResolution(
			@ApiParam(value = "Cancel delivered parts against their order (Rückstandsauflösung) ", required = true)  @NotEmpty @Size(max = 1) @RequestParam(value = "backlogType", required = true) String backlogType);
	
	
}